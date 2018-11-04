/**
 * Created by _BlackEagle_ on 25.07.2018 11:09:51
 */
package de.eaglefamily.game.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.eaglefamily.bukkitlibrary.language.Translation;
import de.eaglefamily.bukkitlibrary.player.Actionbar;
import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.event.CheckGroupKickEvent;
import de.eaglefamily.game.event.PlayerRejoinEvent;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (Bukkit.getPlayer(event.getUniqueId()) != null) {
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Translation.translateUTF("", "alreadyonline"));
			return;
		}
		switch (GameState.getStatus()) {
		case START_UP:
		case STARTING:
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Translation.translateUTF("", "locked.starting"));
			return;
		case ENDING:
		case SHUTDOWN:
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Translation.translateUTF("", "locked.ending"));
			return;
		case LOBBY:
			if (Game.getInstance().getGamePlayers().size() < Settings.lobbyMaxPlayers) return;
			if (checkKick(event.getUniqueId())) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					Translation.translateUTF("", "lobby.full"));
			return;
		case INGAME:
			if (!Settings.spectators) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					Translation.translateUTF("", "game.full"));
			if (Game.getInstance().getGamePlayers().size() < Settings.gameMaxPlayers) return;
			if (checkKick(event.getUniqueId())) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					Translation.translateUTF("", "game.full"));
			return;
		case END:
			if (Game.getInstance().getGamePlayers().size() < Settings.gameMaxPlayers) return;
			if (checkKick(event.getUniqueId())) event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL,
					Translation.translateUTF("", "end.full"));
			return;
		default:
			break;
		}
	}

	private boolean checkKick(UUID uuid) {
		CheckGroupKickEvent checkGroupKickEvent = new CheckGroupKickEvent(uuid);
		Bukkit.getPluginManager().callEvent(checkGroupKickEvent);
		return checkGroupKickEvent.isKicked();
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		Bukkit.getOnlinePlayers().forEach(target -> {
			target.hidePlayer(player);
			player.hidePlayer(target);
		});
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		GamePlayer createGamePlayer = Game.getInstance().getPlayerManager().getOfflinePlayer(player.getUniqueId());
		if (createGamePlayer == null) createGamePlayer = new GamePlayer(player);
		else createGamePlayer.setPlayer(player);
		Game.getInstance().getPlayerManager().addGamePlayer(createGamePlayer);
		GamePlayer gamePlayer = createGamePlayer;
		Game.getInstance().getPlayerManager().updatePlayerData(gamePlayer);

		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		gamePlayer.reset();

		switch (GameState.getStatus()) {
		case LOBBY:
			player.setGameMode(GameMode.ADVENTURE);
			Game.getInstance().getGamePlayers().forEach(gP -> {
				player.showPlayer(gP.getPlayer());
				gP.getPlayer().showPlayer(player);
			});
			gamePlayer.updateTabListForOthers();
			gamePlayer.updateTabList();
			gamePlayer.setLobbyContents();
			Game.getInstance().getGamePlayers().forEach(gP -> gP.send("lobby.join", gamePlayer.getReplaces(gP)));
			if (Settings.teams) Actionbar.sendPermanentTranslation(player, "actionbar.selectteam");
			gamePlayer.updateLobbySidebar();
			Game.getInstance().getLobbyCountdown().update();
			Game.getInstance().getStatsManager().showStatsHologram(gamePlayer);
			TaskManager.runTaskLater(10, () -> Game.getInstance().getStatsManager().showStatsSign(gamePlayer));
			break;
		case INGAME:
			if (Settings.rejoin) {
				PlayerRejoinEvent playerRejoinEvent = new PlayerRejoinEvent(gamePlayer);
				Bukkit.getPluginManager().callEvent(playerRejoinEvent);
				if (!playerRejoinEvent.isCancelled()) {
					gamePlayer.updateTabListForOthers();
					gamePlayer.updateTabList();
					if (Settings.gameContents) gamePlayer.setGameContents();
					if (Settings.teams) Game.getInstance().getGamePlayers()
							.forEach(gP -> gP.send("game.rejointeam", gamePlayer.getReplaces(gP)));
					else Game.getInstance().getGamePlayers()
							.forEach(gP -> gP.send("game.rejoin", gamePlayer.getReplaces(gP)));

					Game.getInstance().getGamePlayers().stream().filter(gP -> gP.isSpectator())
							.forEach(gP -> player.hidePlayer(gP.getPlayer()));
					Game.getInstance().getGamePlayers().stream().filter(gP -> !gP.isSpectator())
							.forEach(gP -> player.showPlayer(gP.getPlayer()));
					Game.getInstance().getGamePlayers().forEach(gP -> gP.getPlayer().showPlayer(player));
					break;
				}
			}
			player.setGameMode(GameMode.SURVIVAL);
			gamePlayer.setSpectator();
			Game.getInstance().getGamePlayers().stream().filter(gP -> gP.isSpectator())
					.forEach(gP -> player.hidePlayer(gP.getPlayer()));
			gamePlayer.updateTabList();
			break;
		case END:
			player.setGameMode(GameMode.SURVIVAL);
			gamePlayer.setSpectator();
			Game.getInstance().getGamePlayers().forEach(gP -> {
				player.showPlayer(gP.getPlayer());
				gP.getPlayer().showPlayer(player);
			});
			gamePlayer.updateTabListForOthers();
			gamePlayer.updateTabList();
			Game.getInstance().getGamePlayers().forEach(gP -> gP.send("end.join", gamePlayer.getReplaces(gP)));
			break;
		default:
			break;
		}

		gamePlayer.teleportToSpawn();

	}
}
