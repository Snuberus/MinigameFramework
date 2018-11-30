/**
 * Created by _BlackEagle_ on 29.07.2018 17:27:21
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class EntityDamageListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		case INGAME:
			if (!(event.getEntity() instanceof Player)) break;
			Player player = (Player) event.getEntity();
			GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
			if (gamePlayer.isSpectator()) event.setCancelled(true);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (GameState.getStatus() != GameState.INGAME) return;
		Player damager = null;
		GamePlayer gameDamager = null;
		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
			gameDamager = Game.getInstance().getGamePlayer(damager);
			if (gameDamager.isSpectator()) {
				event.setCancelled(true);
				return;
			}
		}
		if (!Settings.teams) return;
		if (Settings.friendlyFire) return;
		if (!(event.getEntity() instanceof Player)) return;
		if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile) event.getDamager();
			if (!(projectile.getShooter() instanceof Player)) return;
			damager = (Player) projectile.getShooter();
			gameDamager = Game.getInstance().getGamePlayer(damager);
		}
		if (damager == null) return;

		Player player = (Player) event.getEntity();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
		if (gamePlayer.getGameTeam() == gameDamager.getGameTeam()) event.setCancelled(true);
	}

}
