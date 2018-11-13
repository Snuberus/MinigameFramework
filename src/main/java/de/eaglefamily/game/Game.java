/**
 * Created by _BlackEagle_ on 24.07.2018 11:08:49
 */
package de.eaglefamily.game;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.BukkitLibrary;
import de.eaglefamily.bukkitlibrary.command.CommandManager;
import de.eaglefamily.bukkitlibrary.language.LanguageManager;
import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.bukkitlibrary.player.Actionbar;
import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.command.ForcemapCmd;
import de.eaglefamily.game.command.GamemasterCmd;
import de.eaglefamily.game.command.MapCmd;
import de.eaglefamily.game.command.StartCmd;
import de.eaglefamily.game.command.StatsAllCmd;
import de.eaglefamily.game.command.StatsCmd;
import de.eaglefamily.game.countdown.EndCountdown;
import de.eaglefamily.game.countdown.GameCountdown;
import de.eaglefamily.game.countdown.LobbyCountdown;
import de.eaglefamily.game.database.Database;
import de.eaglefamily.game.database.MySql;
import de.eaglefamily.game.event.GameEndPlayerEvent;
import de.eaglefamily.game.event.GameEndTeamEvent;
import de.eaglefamily.game.event.GameEndingPlayerEvent;
import de.eaglefamily.game.event.GameEndingTeamEvent;
import de.eaglefamily.game.event.GamePrepareEvent;
import de.eaglefamily.game.event.GameStartEvent;
import de.eaglefamily.game.event.GameStartingEvent;
import de.eaglefamily.game.listener.AsyncPlayerChatListener;
import de.eaglefamily.game.listener.EntityDamageListener;
import de.eaglefamily.game.listener.GeneralListener;
import de.eaglefamily.game.listener.InventoryListener;
import de.eaglefamily.game.listener.PacketListener;
import de.eaglefamily.game.listener.PlayerDeathListener;
import de.eaglefamily.game.listener.PlayerInteractListener;
import de.eaglefamily.game.listener.PlayerJoinListener;
import de.eaglefamily.game.listener.PlayerMoveListener;
import de.eaglefamily.game.listener.PlayerQuitListener;
import de.eaglefamily.game.listener.PlayerRespawnListener;
import de.eaglefamily.game.listener.PlayerToggleSneakListener;
import de.eaglefamily.game.listener.PluginDisableListener;
import de.eaglefamily.game.manager.MapManager;
import de.eaglefamily.game.manager.PlayerManager;
import de.eaglefamily.game.manager.StatsManager;
import de.eaglefamily.game.manager.WorldManager;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.RoundStatsReplaces;
import de.eaglefamily.game.util.Settings;
import de.eaglefamily.game.util.StatsParameter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class Game {

	@Getter
	private static Game instance;
	@Getter
	private static JavaPlugin plugin;
	@Getter
	private Configuration config;
	@Getter
	private Database database;
	@Getter
	private List<GameTeam> gameTeams = Lists.newArrayList();
	@Getter
	private PlayerManager playerManager;
	@Getter
	private MapManager mapManager;
	@Getter
	private WorldManager worldManager;
	@Getter
	private StatsManager statsManager;
	@Getter
	private LobbyCountdown lobbyCountdown = new LobbyCountdown();
	@Getter
	private GameCountdown gameCountdown = new GameCountdown();
	@Getter
	private EndCountdown endCountdown = new EndCountdown();
	@Getter
	@Setter
	private GamePlayer lastDisconnected;

	protected Game(JavaPlugin plugin) {
		if (instance != null) return;
		instance = this;
		Game.plugin = plugin;
		BukkitLibrary.setPlugin(plugin);
	}

	public void initialize() {
		Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(""));
		((CraftServer) Bukkit.getServer()).getHandle().getServer().setAllowFlight(true);
		fixBow();

		loadConfig();
		loadLanguages();
		registerListener();
		registerCommands();

		database = new MySql(config.getString("database.host"), config.getInt("database.port"),
				config.getString("database.database"), config.getString("database.username"),
				config.getString("database.password"), config.getString("database.usertable"),
				config.getString("database.statstable"));
		database.connect();

		playerManager = new PlayerManager();
		mapManager = new MapManager();
		worldManager = new WorldManager();
		mapManager.deleteLobbyPlayerData();
		worldManager.loadLobbyWorld();
		if (!Settings.voteMap) mapManager.setMap(Settings.map);
		else mapManager.selectVotableMaps();
		statsManager = new StatsManager();

		if (Settings.teams) {
			for (int i = 0; i < Settings.numberOfTeams; i++) {
				gameTeams.add(new GameTeam(i));
			}
		}
		GameState.setStatus(GameState.LOBBY);

	}

	public void prepareGame() {
		if (GameState.getStatus() != GameState.LOBBY) return;
		mapManager.importMap();
		Message.send("map.show", "map", mapManager.getMapName(), "builder", mapManager.getMapBuilder());
		Bukkit.getPluginManager().callEvent(new GamePrepareEvent());
	}

	public void startGame() {
		if (GameState.getStatus() != GameState.LOBBY) return;
		GameState.setStatus(GameState.STARTING);
		Bukkit.getPluginManager().callEvent(new GameStartingEvent());

		if (Settings.teams
				&& !Settings.actionbarTeam) getGamePlayers().forEach(gP -> Actionbar.stopPermanentSend(gP.getPlayer()));

		if (Settings.teams) getGamePlayers().stream().filter(gP -> gP.getGameTeam() == null).forEach(gP -> {
			gP.setGameTeam(gameTeams.stream().sorted((t1, t2) -> t1.getPlayers().size() - t2.getPlayers().size())
					.findFirst().orElse(gameTeams.get(0)));
			if (Settings.actionbarTeam) Actionbar.sendPermanentTranslation(gP.getPlayer(), "actionbar.team",
					gP.getReplaces());
		});

		getGamePlayers().forEach(gP -> {
			gP.setGamePlayer(true);
			gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 0.7f);
			gP.send("game.start");
			if (Settings.teams) gP.send("game.inteam", gP.getReplaces());
			gP.reset();
			gP.updateLobbySidebar();
			gP.getPlayer().teleport(gP.getSpawn());
			if (Settings.gameContents) gP.setGameContents();
		});

		gameCountdown.start();

		GameState.setStatus(GameState.INGAME);
		Bukkit.getPluginManager().callEvent(new GameStartEvent());
		TaskManager.runTaskLater(20l, () -> checkEnd());
	}

	public void checkEnd() {
		if (GameState.getStatus() != GameState.INGAME) return;
		if (getGamePlayers().stream().filter(gP -> !gP.isSpectator()).count() <= 1) {
			endPlayer(getGamePlayers().stream().filter(gP -> !gP.isSpectator()).findFirst().orElse(lastDisconnected));
			return;
		}
		if (!Settings.teams) return;
		List<GameTeam> lastTeams = gameTeams.stream()
				.filter(gameTeam -> gameTeam.getPlayers().stream()
						.filter(gP -> !gP.isSpectator() && gP.getPlayer().isOnline()).count() > 0)
				.collect(Collectors.toList());

		if (lastTeams.isEmpty()) endTeam(null);
		if (lastTeams.size() == 1) endTeam(lastTeams.get(0));
	}

	public void endPlayer(GamePlayer gamePlayer) {
		if (Settings.teams) {
			if (gamePlayer == null) endTeam(null);
			else endTeam(gamePlayer.getGameTeam());
			return;
		}
		if (GameState.getStatus() != GameState.INGAME) return;
		GameState.setStatus(GameState.ENDING);

		Bukkit.getPluginManager().callEvent(new GameEndingPlayerEvent(gamePlayer));

		if (gamePlayer != null) getGamePlayers().forEach(gP -> {
			SimpleDateFormat format = new SimpleDateFormat(gP.translateUTF("end.wontimeformat"));
			List<Object> replaces = Lists.newArrayList(gamePlayer.getReplaces(gP));
			replaces.add("time");
			replaces.add(format.format(getGameCountdown().getCounter() / 20 * 1000));
			gP.send("end.won", replaces.toArray());
			showSpectators();
		});
		sendRoundStats();
		showSpectators();

		gameCountdown.stop();
		endCountdown.start();

		GameState.setStatus(GameState.END);
		Bukkit.getPluginManager().callEvent(new GameEndPlayerEvent(gamePlayer));
	}

	public void endTeam(GameTeam gameTeam) {
		if (GameState.getStatus() != GameState.INGAME) return;
		GameState.setStatus(GameState.ENDING);

		Bukkit.getPluginManager().callEvent(new GameEndingTeamEvent(gameTeam));

		if (gameTeam != null) getGamePlayers().forEach(gP -> {
			SimpleDateFormat format = new SimpleDateFormat(gP.translateUTF("end.wontimeformat"));
			String playerList = "";
			String playerJoin = gP.translateUTF("end.wonplayerjoin");
			for (GamePlayer gamePlayer : gameTeam.getPlayers()) {
				playerList += playerJoin;
				playerList += gP.translateUTF("end.wonplayerlist", gamePlayer.getReplaces(gP));
			}
			if (playerList.length() > 0) playerList = playerList.substring(playerJoin.length());
			List<Object> replaces = Lists.newArrayList(gameTeam.getReplaces(gP));
			replaces.add("time");
			replaces.add(format.format(getGameCountdown().getCounter() / 20 * 1000));
			replaces.add("players");
			replaces.add(playerList);
			gP.send("end.wonteam", replaces.toArray());
			gP.updateTabListForOthers();
		});
		else getGamePlayers().forEach(gP -> {
			SimpleDateFormat format = new SimpleDateFormat(gP.translateUTF("end.wontimeformat"));
			List<Object> replaces = Lists.newArrayList(gP.getReplaces());
			replaces.add("time");
			replaces.add(format.format(getGameCountdown().getCounter() / 20 * 1000));
			gP.send("end.undecided", replaces.toArray());
			gP.updateTabListForOthers();
		});
		sendRoundStats();
		showSpectators();

		gameCountdown.stop();
		endCountdown.start();

		GameState.setStatus(GameState.END);
		Bukkit.getPluginManager().callEvent(new GameEndTeamEvent(gameTeam));
	}

	private void sendRoundStats() {
		List<StatsParameter> statsParameters = Lists.newArrayList(Settings.statsParameter);
		statsParameters.sort((s1, s2) -> s1.getSortId() - s2.getSortId());

		List<GamePlayer> gamePlayers = getGamePlayers().stream().filter(gP -> gP.isGamePlayer())
				.collect(Collectors.toList());
		for (StatsParameter statsParameter : statsParameters) {
			if (statsParameter.getType() == int.class) gamePlayers.sort((g1,
					g2) -> (int) statsManager.getRoundStats(statsParameter.isDescending() ? g2 : g1)
							.getStats(statsParameter)
							- (int) statsManager.getRoundStats(statsParameter.isDescending() ? g1 : g2)
									.getStats(statsParameter));
			if (statsParameter.getType() == double.class) gamePlayers.sort((g1,
					g2) -> (int) ((double) statsManager.getRoundStats(statsParameter.isDescending() ? g2 : g1)
							.getStats(statsParameter)
							- (double) statsManager.getRoundStats(statsParameter.isDescending() ? g1 : g2)
									.getStats(statsParameter)));
			if (statsParameter.getType() == boolean.class) gamePlayers.sort((g1,
					g2) -> ((boolean) statsManager.getRoundStats(statsParameter.isDescending() ? g2 : g1)
							.getStats(statsParameter) ? 1 : 0)
							- ((boolean) statsManager.getRoundStats(statsParameter.isDescending() ? g1 : g2)
									.getStats(statsParameter) ? 1 : 0));
		}

		int position = 1;
		for (GamePlayer gamePlayer : gamePlayers) {
			statsManager.getRoundStats(gamePlayer).setPosition(position);
			Settings.roundStatsReplaces
					.accept(new RoundStatsReplaces(gamePlayer, statsManager.getRoundStats(gamePlayer)) {
						@Override
						public void accept(List<Object> replaces) {
							gamePlayer.send("cmd.stats.round", statsManager.getRoundStatsReplaces(gamePlayer,
									statsManager.getRoundStats(gamePlayer), gamePlayer.getName(), replaces));
						}
					});
			position++;
		}

	}

	private void showSpectators() {
		getGamePlayers().stream().filter(gP -> gP.isSpectator()).forEach(gP -> {
			if (gP.getSpectating() != null) gP.removeCamera();
			getGamePlayers().forEach(gamePlayer -> gamePlayer.getPlayer().showPlayer(gP.getPlayer()));
		});
	}

	public void shutdown() {
		GameState.setStatus(GameState.SHUTDOWN);

		getGamePlayers().forEach(gP -> gP.getPlayer().kickPlayer(gP.translateUTF("end.kick")));
		if (Settings.stats && Settings.statsScored) statsManager.saveRoundStats();

		mapManager.deleteMap();
		mapManager.deleteLobbyPlayerData();

		Bukkit.getServer().shutdown();
	}

	private void loadConfig() {
		File configFile;
		if (Settings.globalConfig) configFile = new File(Settings.configPath);
		else {
			if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
			File gameFolder = new File(plugin.getDataFolder(), "game");
			if (!gameFolder.exists()) gameFolder.mkdir();
			configFile = new File(gameFolder, "config.yml");
		}
		if (!configFile.exists()) {
			try (InputStream in = plugin.getResource("game/config.yml")) {
				Files.copy(in, configFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(configFile);
	}

	private void loadLanguages() {
		File languageFolder;
		if (Settings.globalLanguages) languageFolder = new File(Settings.languagePath);
		else {
			if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
			File gameFolder = new File(plugin.getDataFolder(), "game");
			if (!gameFolder.exists()) gameFolder.mkdir();
			languageFolder = new File(gameFolder, "language");
		}
		// load languages when language folder doesn't exists
		if (!languageFolder.exists()) {
			languageFolder.mkdir();
			File de_DE = new File(languageFolder, "de_DE.yml");
			try (InputStream in = plugin.getResource("game/language/de_DE.yml")) {
				Files.copy(in, de_DE.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		config.getStringList("language.all").forEach(language -> {
			File languageFile = new File(languageFolder, language + ".yml");
			if (languageFile.exists()) LanguageManager.loadLanguageFromConfig(language,
					YamlConfiguration.loadConfiguration(languageFile));
		});
		LanguageManager.setDefaultLanguage(config.getString("language.default"));
	}

	private void registerListener() {
		Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new GeneralListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PacketListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PlayerToggleSneakListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new PluginDisableListener(), plugin);
	}

	private void registerCommands() {
		CommandManager.register(plugin, new ForcemapCmd());
		CommandManager.register(plugin, new GamemasterCmd());
		CommandManager.register(plugin, new MapCmd());
		CommandManager.register(plugin, new StartCmd());
		if (Settings.stats) {
			CommandManager.register(plugin, new StatsCmd());
			CommandManager.register(plugin, new StatsAllCmd());
		}
	}

	private void fixBow() {
		World world = Bukkit.getServer().getWorlds().get(0);
		Entity edgar = world.spawnEntity(new Location(world, 0, 0, 0), EntityType.PIG);
		edgar.remove();
	}

	public List<GamePlayer> getGamePlayers() {
		return playerManager.getGamePlayers();
	}

	public GamePlayer getGamePlayer(Player player) {
		return playerManager.getGamePlayer(player);
	}

}
