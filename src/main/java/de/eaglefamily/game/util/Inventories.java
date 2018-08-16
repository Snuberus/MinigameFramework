/**
 * Created by _BlackEagle_ on 31.07.2018 16:52:54
 */
package de.eaglefamily.game.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;

/**
 * @author _BlackEagle_
 */
public class Inventories {

	private static void fillPanel(Inventory inv) {
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null) continue;
			inv.setItem(i, InventoryItems.panel());
		}
	}

	public static Inventory leave(GamePlayer gamePlayer) {
		Inventory inv = Bukkit.createInventory(null, 9, gamePlayer.translateUTF("inventory.leave.title"));
		inv.setItem(4, InventoryItems.leaveConfirm(gamePlayer));
		inv.setItem(8, InventoryItems.leaveCancel(gamePlayer));
		fillPanel(inv);
		return inv;
	}

	public static Inventory team(GamePlayer gamePlayer) {
		int invSize = 1;
		if (Settings.numberOfTeams > 8) invSize = (Settings.numberOfTeams + 8) / 9;
		Inventory inv = Bukkit.createInventory(null, (invSize + 2) * 9,
				gamePlayer.translateUTF("inventory.team.title"));

		switch (Settings.numberOfTeams) {
		case 1:
			setTeam(gamePlayer, inv, 4, 0);
			break;
		case 2:
			setTeam(gamePlayer, inv, 2, 0);
			setTeam(gamePlayer, inv, 6, 1);
			break;
		case 3:
			setTeam(gamePlayer, inv, 1, 0);
			setTeam(gamePlayer, inv, 4, 1);
			setTeam(gamePlayer, inv, 7, 2);
			break;
		case 4:
			setTeam(gamePlayer, inv, 1, 0);
			setTeam(gamePlayer, inv, 3, 1);
			setTeam(gamePlayer, inv, 5, 2);
			setTeam(gamePlayer, inv, 7, 3);
			break;
		case 5:
			setTeam(gamePlayer, inv, 0, 0);
			setTeam(gamePlayer, inv, 2, 1);
			setTeam(gamePlayer, inv, 4, 2);
			setTeam(gamePlayer, inv, 6, 3);
			setTeam(gamePlayer, inv, 8, 4);
			break;
		case 6:
			setTeam(gamePlayer, inv, 1, 0);
			setTeam(gamePlayer, inv, 2, 1);
			setTeam(gamePlayer, inv, 3, 2);
			setTeam(gamePlayer, inv, 5, 3);
			setTeam(gamePlayer, inv, 6, 4);
			setTeam(gamePlayer, inv, 7, 5);
			break;
		case 7:
			setTeam(gamePlayer, inv, 1, 0);
			setTeam(gamePlayer, inv, 2, 1);
			setTeam(gamePlayer, inv, 3, 2);
			setTeam(gamePlayer, inv, 4, 3);
			setTeam(gamePlayer, inv, 5, 4);
			setTeam(gamePlayer, inv, 6, 5);
			setTeam(gamePlayer, inv, 7, 6);
			break;
		case 8:
			setTeam(gamePlayer, inv, 0, 0);
			setTeam(gamePlayer, inv, 1, 1);
			setTeam(gamePlayer, inv, 2, 2);
			setTeam(gamePlayer, inv, 3, 3);
			setTeam(gamePlayer, inv, 5, 4);
			setTeam(gamePlayer, inv, 6, 5);
			setTeam(gamePlayer, inv, 7, 6);
			setTeam(gamePlayer, inv, 8, 7);
			break;
		default:
			for (int i = 0; i < Settings.numberOfTeams; i++) {
				setTeam(gamePlayer, inv, i, i);
			}
			break;
		}
		fillPanel(inv);
		return inv;
	}

	private static void setTeam(GamePlayer gamePlayer, Inventory inv, int slot, int index) {
		inv.setItem(slot + 9, InventoryItems.team(gamePlayer, Game.getInstance().getGameTeams().get(index)));
	}

	public static Inventory vote(GamePlayer gamePlayer) {
		int invSize = 1;
		if (Game.getInstance().getMapManager().getVoteableMaps()
				.size() > 5) invSize = (Game.getInstance().getMapManager().getVoteableMaps().size() + 8) / 9;
		Inventory inv = Bukkit.createInventory(null, (invSize + 2) * 9,
				gamePlayer.translateUTF("inventory.vote.title"));
		switch (Game.getInstance().getMapManager().getVoteableMaps().size()) {
		case 1:
			setVoteMap(gamePlayer, inv, 4, 0);
			break;
		case 2:
			setVoteMap(gamePlayer, inv, 2, 0);
			setVoteMap(gamePlayer, inv, 6, 1);
			break;
		case 3:
			setVoteMap(gamePlayer, inv, 1, 0);
			setVoteMap(gamePlayer, inv, 4, 1);
			setVoteMap(gamePlayer, inv, 7, 2);
			break;
		case 4:
			setVoteMap(gamePlayer, inv, 1, 0);
			setVoteMap(gamePlayer, inv, 3, 1);
			setVoteMap(gamePlayer, inv, 5, 2);
			setVoteMap(gamePlayer, inv, 7, 3);
			break;
		case 5:
			setVoteMap(gamePlayer, inv, 0, 0);
			setVoteMap(gamePlayer, inv, 2, 1);
			setVoteMap(gamePlayer, inv, 4, 2);
			setVoteMap(gamePlayer, inv, 6, 3);
			setVoteMap(gamePlayer, inv, 8, 4);
			break;
		default:
			for (int i = 0; i < Game.getInstance().getMapManager().getVoteableMaps().size(); i++) {
				setVoteMap(gamePlayer, inv, i, i);
			}
			break;
		}
		fillPanel(inv);
		return inv;
	}

	private static void setVoteMap(GamePlayer gamePlayer, Inventory inv, int slot, int index) {
		inv.setItem(slot + 9,
				InventoryItems.voteMap(gamePlayer, Game.getInstance().getMapManager().getVoteableMaps().get(index)));
	}

	public static Inventory compass(GamePlayer gamePlayer) {
		int invSize = 1;
		if (Game.getInstance().getGamePlayers().stream().filter(GamePlayer::isGamePlayer)
				.count() > 0) invSize = (int) ((Game.getInstance().getGamePlayers().stream()
						.filter(GamePlayer::isGamePlayer).count() + 8) / 9);
		Inventory inv = Bukkit.createInventory(null, invSize * 9, gamePlayer.translateUTF("inventory.compass.title"));
		Game.getInstance().getGameTeams()
				.forEach(gameTeam -> gameTeam.getPlayers().stream()
						.filter(gP -> !gP.isSpectator() && gP.getPlayer().isOnline())
						.forEach(gP -> inv.addItem(InventoryItems.player(gamePlayer, gP))));
		return inv;
	}
}
