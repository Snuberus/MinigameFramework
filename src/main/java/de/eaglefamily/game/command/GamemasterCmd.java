/**
 * Created by _BlackEagle_ on 10.08.2018 17:03:08
 */
package de.eaglefamily.game.command;

import org.bukkit.command.CommandSender;

import de.eaglefamily.bukkitlibrary.command.Command;
import de.eaglefamily.bukkitlibrary.language.Message;

/**
 * @author _BlackEagle_
 */
public class GamemasterCmd {

	@Command(label = "gamemaster", aliases = { "gm", "master", "game" })
	public void onGamemaster(CommandSender sender, String[] args) {
		if (!sender.hasPermission("game.gamemaster")) {
			Message.send(sender, "cmd.nopermission");
			return;
		}

		sender.sendMessage("DEBUG Gamemaster");
	}
}
