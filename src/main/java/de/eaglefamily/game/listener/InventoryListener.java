/*
 * Created by Jan on 29.07.2018 17:16:21
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;

/**
 * The Object of a listener interface for receiving inventory events. When the
 * inventory event occurs, that object's appropriate method is invoked.
 *
 * @see InventoryEvent
 */
public class InventoryListener implements Listener {

	/**
	 * On inventory click.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		case INGAME:
			GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
			if (gamePlayer.isSpectator()) event.setCancelled(true);
			break;
		default:
			break;
		}
	}

	/**
	 * On inventory drag.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		case INGAME:
			GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
			if (gamePlayer.isSpectator()) event.setCancelled(true);
			break;
		default:
			break;
		}
	}
}
