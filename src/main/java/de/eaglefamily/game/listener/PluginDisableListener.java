/*
 * Created by Jan on 25.07.2018 17:38:55
 */
package de.eaglefamily.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import de.eaglefamily.game.Game;

/**
 * The Object of a listener interface for receiving pluginDisable events. When
 * the pluginDisable event occurs, that object's appropriate method is invoked.
 *
 * @see PluginDisableEvent
 */
public class PluginDisableListener implements Listener {

	/**
	 * On plugin disable.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin() != Game.getPlugin()) return;
		Game.getInstance().shutdown();
	}
}
