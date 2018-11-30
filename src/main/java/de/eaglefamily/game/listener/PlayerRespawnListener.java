/**
 * Created by _BlackEagle_ on 30.07.2018 23:25:15
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
 * @author _BlackEagle_
 */
public class PlayerRespawnListener implements Listener {

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
