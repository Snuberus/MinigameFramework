/**
 * Created by _BlackEagle_ on 30.07.2018 15:39:07
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;

/**
 * @author _BlackEagle_
 */
public class PlayerMoveListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (GameState.getStatus() != GameState.INGAME) return;
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (gamePlayer.getSpectating() != null) {
			player.teleport(gamePlayer.getSpectating().getPlayer());
			return;
		}
		gamePlayer.getSpectators().stream().filter(gP -> gP.isCameraReseted())
				.forEach(gP -> gP.updateCamera(gamePlayer));
		gamePlayer.getSpectators().forEach(gP -> gP.getPlayer().teleport(event.getTo()));
		Game.getInstance().getGamePlayers().stream().filter(gP -> gP.getCompassTarget() == gamePlayer)
				.forEach(gP -> gP.getPlayer().setCompassTarget(event.getTo()));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (GameState.getStatus() != GameState.INGAME) return;
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		gamePlayer.getSpectators().forEach(gP -> gP.getPlayer().teleport(event.getTo()));
		Game.getInstance().getGamePlayers().stream().filter(gP -> gP.getCompassTarget() == gamePlayer)
				.forEach(gP -> gP.getPlayer().setCompassTarget(event.getTo()));
	}
}
