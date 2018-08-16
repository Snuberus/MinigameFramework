/**
 * Created by _BlackEagle_ on 31.07.2018 19:07:54
 */
package de.eaglefamily.game.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.player.Actionbar;
import de.eaglefamily.bukkitlibrary.util.ItemBuilder;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.GameTeam;
import de.eaglefamily.game.manager.MapManager.VoteableMap;

/**
 * @author _BlackEagle_
 */
public class InventoryItems {

	public static ItemStack panel() {
		return new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 15).setDisplayName(" ").build();
	}

	public static ItemStack leaveConfirm(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14)
				.setDisplayName(gamePlayer.translateUTF("inventory.leave.confirm"))
				.onClick(click -> click.getPlayer().kickPlayer("")).build();
	}

	public static ItemStack leaveCancel(GamePlayer gamePlayer) {
		return new ItemBuilder(Material.BARRIER).setDisplayName(gamePlayer.translateUTF("inventory.leave.cancel"))
				.onClick(click -> click.getPlayer().closeInventory()).build();
	}

	public static ItemStack team(GamePlayer gamePlayer, GameTeam gameTeam) {
		List<String> lore = Lists.newArrayList();
		gameTeam.getPlayers().forEach(
				gP -> lore.add(gamePlayer.translateUTF("inventory.team.selectlore", gP.getReplaces(gamePlayer))));
		return new ItemBuilder(Material.STAINED_CLAY, 1,
				(short) Game.getInstance().getConfig().getInt("team." + gameTeam.getIndex() + ".color"))
						.setDisplayName(
								gamePlayer.translateUTF("inventory.team.select", gameTeam.getReplaces(gamePlayer)))
						.setLore(lore).onClick(click -> {
							GamePlayer gP = Game.getInstance().getGamePlayer(click.getPlayer());
							if (gP.getGameTeam() == gameTeam) {
								gP.setGameTeam(null);
								gP.send("team.leave", gP.getReplaces());
								if (Settings.teams) Actionbar.sendPermanentTranslation(gP.getPlayer(),
										"actionbar.selectteam");
								reloadTeam();
								gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
								return;
							}
							if (gameTeam.getPlayers().size() >= Settings.numberOfPlayersPerTeam) {
								gP.send("team.full", gameTeam.getReplaces(gP));
								gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
								return;
							}
							gP.setGameTeam(gameTeam);
							gP.send("team.join", gP.getReplaces());
							if (Settings.teams) Actionbar.sendPermanentTranslation(gP.getPlayer(), "actionbar.team",
									gP.getReplaces());
							reloadTeam();
							gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
						}).build();
	}

	private static void reloadTeam() {
		Game.getInstance().getGamePlayers().stream().filter(
				player -> player.getPlayer().getOpenInventory().getTitle().equals(Inventories.team(player).getTitle()))
				.forEach(player -> player.getPlayer().openInventory(Inventories.team(player)));
	}

	public static ItemStack voteMap(GamePlayer gamePlayer, VoteableMap map) {
		ItemBuilder builder = new ItemBuilder(map.getMaterial())
				.setDisplayName(gamePlayer.translateUTF("inventory.vote.mapname", "map", map.getMapName()))
				.setLore(gamePlayer
						.translateUTF("inventory.vote.lore", "votes", map.getVotes(), "builder", map.getMapBuilder())
						.split("\n"));
		if (map.getPlayerVotes().contains(
				gamePlayer)) builder.addEnchantment(Enchantment.DAMAGE_ALL, 1).addFlags(ItemFlag.HIDE_ENCHANTS);
		builder.onClick(click -> {
			GamePlayer gP = Game.getInstance().getGamePlayer(click.getPlayer());
			if (!map.getPlayerVotes().contains(gP)) {
				map.vote(Game.getInstance().getGamePlayer(click.getPlayer()));
				gP.send("vote.successful", "map", map.getMapName());
				gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
				reloadVote();
			} else {
				gP.send("vote.already");
				gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
			}

		});
		return builder.build();
	}

	private static void reloadVote() {
		Game.getInstance().getGamePlayers().stream().filter(
				player -> player.getPlayer().getOpenInventory().getTitle().equals(Inventories.vote(player).getTitle()))
				.forEach(player -> player.getPlayer().openInventory(Inventories.vote(player)));
	}

	public static ItemStack player(GamePlayer gamePlayer, GamePlayer target) {
		return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setOwner(target.getPlayer())
				.setDisplayName(gamePlayer.translateUTF("inventory.compass.playername", target.getReplaces(gamePlayer)))
				.setLore(gamePlayer.translateUTF("inventory.compass.playerlore").split("\n")).onLeftClick(click -> {
					click.getPlayer().closeInventory();
					Game.getInstance().getGamePlayer(click.getPlayer()).setCompassTarget(target);
					click.getPlayer().teleport(target.getPlayer());
				}).onRightClick(click -> {
					click.getPlayer().closeInventory();
					click.getPlayer().teleport(target.getPlayer());
					Game.getInstance().getGamePlayer(click.getPlayer()).setCamera(target);
				}).build();
	}
}
