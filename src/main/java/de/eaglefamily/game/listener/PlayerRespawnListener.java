/*
 * Created by Jan on 30.07.2018 23:25:15
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.Settings;

/**
 * The Object of a listener interface for receiving playerRespawn events. When
 * the playerRespawn event occurs, that object's appropriate method is invoked.
 *
 * @see PlayerRespawnEvent
 */
public class PlayerRespawnListener implements Listener {

	/**
	 * On player respawn.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		event.setRespawnLocation(gamePlayer.getSpawn());
		if (gamePlayer.isSpectator()) return;
		gamePlayer.getSpectators().forEach(gP -> {
			gP.getPlayer().teleport(event.getRespawnLocation());
			gP.setCameraReseted(true);
		});
		if (Settings.gameContents) gamePlayer.setGameContents();

	}
}
