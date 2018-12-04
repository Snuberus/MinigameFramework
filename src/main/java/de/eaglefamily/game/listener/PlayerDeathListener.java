/*
 * Created by Jan on 30.07.2018 23:21:46
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;

/**
 * The Object of a listener interface for receiving playerDeath events. When the
 * playerDeath event occurs, that object's appropriate method is invoked.
 *
 * @see PlayerDeathEvent
 */
public class PlayerDeathListener implements Listener {

	/**
	 * On player death.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = event.getEntity();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (player.isInsideVehicle()) player.leaveVehicle();
		gamePlayer.getSpectators().forEach(gP -> gP.resetCamera());
		TaskManager.runTaskLater(20l, () -> player.spigot().respawn());
	}
}
