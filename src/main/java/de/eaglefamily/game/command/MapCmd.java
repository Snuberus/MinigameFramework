/**
 * Created by _BlackEagle_ on 10.08.2018 17:02:19
 */
package de.eaglefamily.game.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.command.Command;
import de.eaglefamily.bukkitlibrary.command.TabCompleter;
import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;

/**
 * @author _BlackEagle_
 */
public class MapCmd {

	@Command(label = "map")
	public void onLabel(CommandSender sender, String[] args) {
		if (Game.getInstance().getMapManager().getMapName() != null) Message.send(sender, "map.show", "map",
				Game.getInstance().getMapManager().getMapName(), "builder",
				Game.getInstance().getMapManager().getMapBuilder());
		else Message.send(sender, "map.vote");
	}

	@TabCompleter(label = "map")
	public List<String> onMapTab(CommandSender sender, String[] args) {
		return Lists.newArrayList();
	}
}
