/*
 * Created by Jan on 10.08.2018 18:35:55
 */
package de.eaglefamily.game.command;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.eaglefamily.bukkitlibrary.command.Command;
import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.Settings;
import de.eaglefamily.game.util.Stats;
import de.eaglefamily.game.util.StatsReplaces;

/**
 * The Class StatsCmd.
 *
 * @author Jan
 */
public class StatsCmd {

	/**
	 * On stats.
	 *
	 * @param sender
	 *            the sender
	 * @param args
	 *            the args
	 */
	@Command(label = "stats")
	public void onStats(CommandSender sender, String[] args) {
		if (args.length > 1) {
			Message.send(sender, "cmd.stats.month.usage");
			return;
		}

		if (args.length == 0 || args.length == 1 && sender instanceof Player
				&& args[0].equalsIgnoreCase(((Player) sender).getName())) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cNot available for console.");
				return;
			}
			Player player = (Player) sender;
			Game.getInstance().getStatsManager().getStats(player.getUniqueId(), 30, stats -> {
				if (stats == null) {
					Message.send(player, "cmd.stats.month.ownnotfound");
					return;
				}
				sendStatsMessage(sender, player.getUniqueId(), stats, "cmd.stats.month.showown");
			});
			return;
		}

		if (args.length == 1 && args[0].startsWith("#") && args[0].substring(1).matches("[0-9]+")) {
			Game.getInstance().getStatsManager().getStats(Integer.parseInt(args[0].substring(1)), 30, stats -> {
				if (stats == null) {
					Message.send(sender, "cmd.stats.month.noposition", "position", args[0].substring(1));
					return;
				}
				sendStatsMessage(sender, stats.getUuid(), stats, "cmd.stats.month.show");
			});
			return;
		}

		if (args.length == 1) {
			Game.getInstance().getDatabase().getUUID(args[0], uuid -> {
				if (uuid == null) {
					Message.send(sender, "cmd.stats.month.notfound", "player", args[0]);
					return;
				}
				Game.getInstance().getStatsManager().getStats(uuid, 30, stats -> {
					if (stats == null) {
						Message.send(sender, "cmd.stats.month.notfound", "player", args[0]);
						return;
					}
					sendStatsMessage(sender, uuid, stats, "cmd.stats.month.show");
				});
			}, false);
			return;
		}
	}

	private void sendStatsMessage(CommandSender sender, UUID uuid, Stats stats, String key) {
		Settings.statsReplaces.accept(new StatsReplaces(stats.getUuid(), stats) {
			@Override
			public void accept(List<Object> replaces) {
				Game.getInstance().getDatabase().getName(uuid, name -> {
					Message.send(sender, key,
							Game.getInstance().getStatsManager().getStatsReplaces(sender, stats, name, replaces));
				}, false);
			}
		});
	}

}
