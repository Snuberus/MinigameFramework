/**
 * Created by _BlackEagle_ on 24.07.2018 15:43:38
 */
package de.eaglefamily.game.manager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.Inventories;
import de.eaglefamily.game.util.Items;
import de.eaglefamily.game.util.Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
public class MapManager {

	private File worldFolder;
	@Getter
	private boolean imported;
	@Getter
	private Configuration config;
	@Getter
	private String mapName;
	@Getter
	private String mapBuilder;
	@Getter
	private Material material;

	@Getter
	private List<VoteableMap> voteableMaps = Lists.newArrayList();

	public void selectVotableMaps() {
		for (int i = 0; i < Settings.voteableMaps; i++) {
			String map = selectRandomVotableMap();
			if (map == null) break;
			VoteableMap voteableMap = new VoteableMap(map,
					Game.getInstance().getConfig().getString("maps." + Settings.getSize() + "." + map + ".builder"),
					Material.valueOf(Game.getInstance().getConfig()
							.getString("maps." + Settings.getSize() + "." + map + ".material")));
			voteableMaps.add(voteableMap);
		}
	}

	private String selectRandomVotableMap() {
		double entireProbability = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize())
				.getKeys(false).stream()
				.filter(map -> !voteableMaps.stream().filter(voteMap -> voteMap.mapName.equals(map)).findFirst()
						.isPresent())
				.mapToDouble(mapper -> Game.getInstance().getConfig()
						.getDouble("maps." + Settings.getSize() + "." + mapper + ".probability"))
				.sum();
		double randomNumber = entireProbability * Math.random();
		double i = 0;
		Iterator<String> iterator = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize())
				.getKeys(false).stream().filter(map -> !voteableMaps.stream()
						.filter(voteMap -> voteMap.mapName.equals(map)).findFirst().isPresent())
				.iterator();
		while (iterator.hasNext()) {
			String map = iterator.next();
			i += Game.getInstance().getConfig().getDouble("maps." + Settings.getSize() + "." + map + ".probability");
			if (randomNumber <= i) {
				return map;
			}
		}
		return null;
	}

	private void selectVotedMap() {
		VoteableMap map = voteableMaps.stream().sorted((m1, m2) -> m2.getVotes() - m1.getVotes()).findFirst()
				.orElse(voteableMaps.get(0));
		setMap(map.getMapName());
	}

	public void importMap() {
		if (imported) return;
		if (mapName == null) {
			removeVoteItem();
			selectVotedMap();
		}
		imported = true;

		TaskManager.runTaskAsync(() -> {
			worldFolder = new File(Bukkit.getServer().getWorldContainer(), mapName);
			if (Settings.globalMaps) {
				File globalMapsFolder = new File(Settings.mapsPath);
				File mapFolder = new File(new File(globalMapsFolder, Settings.getSize()), mapName);
				try {
					FileUtils.copyDirectory(mapFolder, worldFolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File configFile = new File(worldFolder, "config.yml");
			config = YamlConfiguration.loadConfiguration(configFile);
			TaskManager.runTask(() -> Game.getInstance().getWorldManager().loadGameWorld());
		});
	}

	public void deleteMap() {
		if (!imported) return;
		Bukkit.unloadWorld(Game.getInstance().getWorldManager().getGameWorld(), false);
		if (Settings.globalMaps) {
			try {
				FileUtils.deleteDirectory(worldFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileUtils.deleteDirectory(new File(worldFolder, "playerdata"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteLobbyPlayerData() {
		File lobbyWorld = new File(Bukkit.getServer().getWorldContainer(),
				Game.getInstance().getConfig().getString("lobby.world"));
		try {
			FileUtils.deleteDirectory(new File(lobbyWorld, "playerdata"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setMap(String mapName) {
		if (imported) return;
		if (this.mapName == null) removeVoteItem();
		this.mapName = mapName;
		mapBuilder = Game.getInstance().getConfig()
				.getString("maps." + Settings.getSize() + "." + mapName + ".builder");
		Game.getInstance().getGamePlayers().forEach(GamePlayer::updateLobbySidebar);
	}

	public VoteableMap getVotedMap(GamePlayer gamePlayer) {
		return voteableMaps.stream().filter(map -> map.playerVotes.contains(gamePlayer)).findFirst().orElse(null);
	}

	private void removeVoteItem() {
		Game.getInstance().getGamePlayers().forEach(gamePlayer -> {
			if (!Settings.teams) gamePlayer.removeLobbyContent(0);
			else gamePlayer.removeLobbyContent(1);
			gamePlayer.getPlayer().getInventory().remove(Items.vote(gamePlayer));
			if (gamePlayer.getPlayer().getOpenInventory().getTitle()
					.equals(Inventories.vote(gamePlayer).getTitle())) gamePlayer.getPlayer().closeInventory();
		});
	}

	@AllArgsConstructor
	public class VoteableMap {
		@Getter
		private final String mapName;
		@Getter
		private final String mapBuilder;
		@Getter
		private final Material material;
		@Getter
		private final List<GamePlayer> playerVotes = Lists.newArrayList();

		public void vote(GamePlayer gamePlayer) {
			if (playerVotes.contains(gamePlayer)) return;
			playerVotes.add(gamePlayer);
			voteableMaps.stream().filter(map -> map != this && map.playerVotes.contains(gamePlayer))
					.forEach(map -> map.unvote(gamePlayer));
		}

		public void unvote(GamePlayer gamePlayer) {
			playerVotes.remove(gamePlayer);
		}

		public int getVotes() {
			return playerVotes.size();
		}
	}
}
