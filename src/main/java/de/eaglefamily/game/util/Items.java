/*
 * Created by _BlackEagle_ on 31.07.2018 16:52:46
 */
package de.eaglefamily.game.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.eaglefamily.bukkitlibrary.util.ItemBuilder;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;

/**
 * The Class Items.
 *
 * @author Jan
 */
public class Items {

	/**
	 * Leave.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the item stack
	 */
	public static ItemStack leave(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.WOOD_DOOR).setDisplayName(gamePlayer.translateUTF("item.leave"))
				.onRightInteract(interact -> interact.getPlayer()
						.openInventory(Inventories.leave(Game.getInstance().getGamePlayer(interact.getPlayer()))))
				.build();
	}

	/**
	 * Team selector.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the item stack
	 */
	public static ItemStack teamSelector(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.BED).setDisplayName(gamePlayer.translateUTF("item.team"))
				.onRightInteract(interact -> interact.getPlayer()
						.openInventory(Inventories.team(Game.getInstance().getGamePlayer(interact.getPlayer()))))
				.build();
	}

	/**
	 * Vote.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the item stack
	 */
	public static ItemStack vote(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.PAPER).setDisplayName(gamePlayer.translateUTF("item.vote"))
				.onRightInteract(interact -> interact.getPlayer()
						.openInventory(Inventories.vote(Game.getInstance().getGamePlayer(interact.getPlayer()))))
				.build();
	}

	/**
	 * Compass.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the item stack
	 */
	public static ItemStack compass(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.COMPASS).setDisplayName(gamePlayer.translateUTF("item.compass"))
				.onRightInteract(interact -> interact.getPlayer()
						.openInventory(Inventories.compass(Game.getInstance().getGamePlayer(interact.getPlayer()))))
				.build();
	}
}
