/**
 * Created by _BlackEagle_ on 29.07.2018 17:05:27
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.world.StructureGrowEvent;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;

/**
 * @author _BlackEagle_
 */
public class GeneralListener implements Listener {

	private void checkCancelled(Cancellable event, Player player) {
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

	private void checkCancelled(Cancellable event) {
		switch (GameState.getStatus()) {
		case LOBBY:
		case STARTING:
		case ENDING:
		case END:
			event.setCancelled(true);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		checkCancelled(event, (Player) event.getEntity());
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onStructureGrow(StructureGrowEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (!(event.getAttacker() instanceof Player)) checkCancelled(event);
		else checkCancelled(event, (Player) event.getAttacker());
	}

	@EventHandler
	public void onVehicleDamage(VehicleDamageEvent event) {
		if (!(event.getAttacker() instanceof Player)) checkCancelled(event);
		else checkCancelled(event, (Player) event.getAttacker());
	}

	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		if (!(event.getEntered() instanceof Player)) checkCancelled(event);
		else checkCancelled(event, (Player) event.getEntered());
	}

	@EventHandler
	public void onHangingBreak(HangingBreakEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onPlayerBucket(PlayerBucketEmptyEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerBucket(PlayerBucketFillEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerEditBook(PlayerEditBookEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerFish(PlayerFishEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onPlayerShearEntity(PlayerShearEntityEvent event) {
		checkCancelled(event, event.getPlayer());
	}

	@EventHandler
	public void onEntityShootBow(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) checkCancelled(event);
		else checkCancelled(event, (Player) event.getEntity());
	}

	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event) {
		checkCancelled(event);
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		checkCancelled(event);
	}
}
