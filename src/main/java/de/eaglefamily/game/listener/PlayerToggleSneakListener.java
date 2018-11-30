/**
 * Created by _BlackEagle_ on 30.07.2018 15:55:51
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;

/**
 * @author _BlackEagle_
 */
public class PlayerToggleSneakListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if (GameState.getStatus() != GameState.INGAME) return;
		if (!event.isSneaking()) return;
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (gamePlayer.getSpectating() == null) return;
		gamePlayer.removeCamera();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		if (GameState.getStatus() != GameState.INGAME) return;
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (gamePlayer.getSpectating() == null) return;
		event.setCancelled(true);
	}

}
