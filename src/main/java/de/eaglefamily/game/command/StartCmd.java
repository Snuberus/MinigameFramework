/**
 * Created by _BlackEagle_ on 10.08.2018 17:02:32
 */
package de.eaglefamily.game.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.command.Command;
import de.eaglefamily.bukkitlibrary.command.TabCompleter;
import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class StartCmd {

	@Command(label = "start")
	public void onStart(CommandSender sender, String[] args) {
		if (!sender.hasPermission("game.start")) {
			Message.send(sender, "cmd.nopermission");
			return;
		}

		if (GameState.getStatus() != GameState.LOBBY) {
			Message.send(sender, "cmd.start.alreadyrunning");
			return;
		}

		if (Game.getInstance().getLobbyCountdown().getCountdown() <= Settings.lobbyShortStart) {
			Message.send(sender, "cmd.start.alreadystarted");
			return;
		}

		if (Settings.playerForLobbyStart > Game.getInstance().getGamePlayers().size()) {
			Message.send(sender, "cmd.start.notenoughplayers");
			return;
		}

		Message.send(sender, "cmd.start.successful");
		Game.getInstance().getLobbyCountdown().lobbyShortStart();

	}

	@TabCompleter(label = "start")
	public List<String> onStartTab(CommandSender sender, String[] args) {
		return Lists.newArrayList();
	}
}
