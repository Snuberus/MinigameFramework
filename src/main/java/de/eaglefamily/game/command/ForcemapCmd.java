/**
 * Created by _BlackEagle_ on 10.08.2018 17:02:38
 */
package de.eaglefamily.game.command;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.command.Command;
import de.eaglefamily.bukkitlibrary.command.TabCompleter;
import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.bukkitlibrary.language.Translation;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class ForcemapCmd {

	@Command(label = "forcemap", aliases = { "force" })
	public void onForcemap(CommandSender sender, String[] args) {
		if (!sender.hasPermission("game.forcemap")) {
			Message.send(sender, "cmd.nopermission");
			return;
		}

		if (args.length > 1) {
			Message.send(sender, "cmd.forcemap.usage");
			return;
		}

		if (Game.getInstance().getMapManager().isImported()) {
			Message.send(sender, "cmd.forcemap.alreadyimported");
			return;
		}

		if (args.length == 0) {
			String[] maps = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize())
					.getKeys(false).toArray(new String[0]);
			for (int i = 0; i < maps.length; i++) {
				maps[i] = Translation.translateUTF(sender, "cmd.forcemap.maplist", "map", maps[i]);
			}
			String mapList = String.join(Translation.translateUTF(sender, "cmd.forcemap.mapjoin"), maps);
			Message.send(sender, "cmd.forcemap.maps", "maps", mapList);
			return;
		}

		String map = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize()).getKeys(false)
				.stream().filter(key -> key.equalsIgnoreCase(args[0])).findFirst().orElse(null);

		if (map == null) {
			Message.send(sender, "cmd.forcemap.unknownmap");
			return;
		}

		if (map.equals(Game.getInstance().getMapManager().getMapName())) {
			Message.send(sender, "cmd.forcemap.alreadyselected");
			return;
		}

		Message.send(sender, "cmd.forcemap.successful", "map", map);
		Game.getInstance().getMapManager().setMap(map);

	}

	@TabCompleter(label = "forcemap")
	public List<String> onForcemapTab(CommandSender sender, String[] args) {
		if (args.length != 1 || !sender.hasPermission("game.forcemap")) return Lists.newArrayList();
		return Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize()).getKeys(false)
				.stream().filter(key -> key.toLowerCase().startsWith(args[0].toLowerCase()))
				.collect(Collectors.toList());
	}
}
