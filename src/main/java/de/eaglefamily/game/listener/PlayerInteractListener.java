/**
 * Created by _BlackEagle_ on 29.07.2018 17:47:17
 */
package de.eaglefamily.game.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;

/**
 * @author _BlackEagle_
 */
public class PlayerInteractListener implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (isInteractAllowed(event.getAction(), event.getClickedBlock(), event.getItem())) return;
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		case INGAME:
			Player player = event.getPlayer();
			GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
			if (gamePlayer.isSpectator()) event.setCancelled(true);
			break;
		default:
			break;
		}
	}

	private boolean isInteractAllowed(Action action, Block block, ItemStack itemStack) {
		if (action == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.FLOWER_POT) return false;
		if (action == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.CAULDRON) return false;
		if (action == Action.RIGHT_CLICK_BLOCK && itemStack != null
				&& itemStack.getType() == Material.PAINTING) return false;
		if (action == Action.RIGHT_CLICK_BLOCK && itemStack != null
				&& itemStack.getType() == Material.ITEM_FRAME) return false;
		if (action == Action.RIGHT_CLICK_BLOCK && block.getState() instanceof InventoryHolder) return false;
		return true;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		case INGAME:
			Player player = event.getPlayer();
			GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);
			if (!gamePlayer.isSpectator() || gamePlayer.getSpectating() != null) break;
			event.setCancelled(true);
			if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) break;
			if (!(event.getRightClicked() instanceof Player)) break;
			Player target = (Player) event.getRightClicked();
			GamePlayer gameTarget = Game.getInstance().getGamePlayer(target);
			gamePlayer.setCamera(gameTarget);
			break;
		default:
			break;
		}
	}
}
