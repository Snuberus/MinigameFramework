/**
 * Created by _BlackEagle_ on 25.07.2018 17:38:55
 */
package de.eaglefamily.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import de.eaglefamily.game.Game;

/**
 * @author _BlackEagle_
 */
public class PluginDisableListener implements Listener {

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin() != Game.getPlugin()) return;
		Game.getInstance().shutdown();
	}
}
