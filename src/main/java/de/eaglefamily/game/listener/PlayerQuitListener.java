/*
 * Created by Jan on 25.07.2018 11:09:59
 */
package de.eaglefamily.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.manager.MapManager.VoteableMap;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;

/**
 * The Object of a listener interface for receiving playerQuit events. When the
 * playerQuit event occurs, that object's appropriate method is invoked.
 *
 * @see PlayerQuitEvent
 */
public class PlayerQuitListener implements Listener {

	/**
	 * On player quit.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (gamePlayer.getSpectating() != null) gamePlayer.removeCamera();

		switch (GameState.getStatus()) {
		case LOBBY:
			Game.getInstance().getGamePlayers().forEach(gP -> gP.send("lobby.leave", gamePlayer.getReplaces(gP)));
			VoteableMap map = Game.getInstance().getMapManager().getVotedMap(gamePlayer);
			if (map != null) map.unvote(gamePlayer);
			TaskManager.runTask(() -> Game.getInstance().getLobbyCountdown().update());
			break;
		case INGAME:
			if (gamePlayer.isSpectator()) break;
			Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(player, Lists.newArrayList(), 0, ""));
			gamePlayer.getSpectators().forEach(gP -> gP.removeCamera());
			if (Settings.teams) Game.getInstance().getGamePlayers()
					.forEach(gP -> gP.send("game.leaveteam", gamePlayer.getReplaces(gP)));
			if (!Settings.teams) Game.getInstance().getGamePlayers()
					.forEach(gP -> gP.send("game.leave", gamePlayer.getReplaces(gP)));
			TaskManager.runTask(() -> Game.getInstance().checkEnd());
			break;
		case END:
			Game.getInstance().getGamePlayers().forEach(gP -> gP.send("end.leave", gamePlayer.getReplaces(gP)));
			break;
		default:
			break;
		}

		Game.getInstance().getPlayerManager().removeGamePlayer(gamePlayer);
		Game.getInstance().setLastDisconnected(gamePlayer);

	}
}
