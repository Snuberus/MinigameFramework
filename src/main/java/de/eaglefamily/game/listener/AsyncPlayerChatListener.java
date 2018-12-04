/*
 * Created by Jan on 29.07.2018 16:13:40
 */
package de.eaglefamily.game.listener;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.language.Translation;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * The Object of a listener interface for receiving asyncPlayerChat events. When
 * the asyncPlayerChat event occurs, that object's appropriate method is
 * invoked.
 *
 * @see AsyncPlayerChatEvent
 */
public class AsyncPlayerChatListener implements Listener {

	private final List<String> ALL = Lists.newArrayList("@all ", "@all", "@a ", "@a", "@ ", "@");

	/**
	 * On chat.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		GamePlayer gamePlayer = Game.getInstance().getGamePlayer(player);

		switch (GameState.getStatus()) {
		case LOBBY:
			Game.getInstance().getGamePlayers().forEach(gP -> send(gamePlayer, "chat.default", gP, event.getMessage()));
			break;
		case STARTING:
		case INGAME:
			if (gamePlayer.isSpectator()) {
				Game.getInstance().getGamePlayers().stream().filter(gP -> gP.isSpectator())
						.forEach(gP -> send(gamePlayer, "chat.spectator", gP, event.getMessage()));
				break;
			}
			if (!Settings.teams) {
				Game.getInstance().getGamePlayers()
						.forEach(gP -> send(gamePlayer, "chat.default", gP, event.getMessage()));
				break;
			}

			String all = isAll(event.getMessage());
			if (all == null) {
				gamePlayer.getGameTeam().getPlayers()
						.forEach(gP -> send(gamePlayer, "chat.team.private", gP, event.getMessage()));
				break;
			}

			String message = event.getMessage().substring(all.length());
			if (message.length() == 0) {
				gamePlayer.getGameTeam().getPlayers()
						.forEach(gP -> send(gamePlayer, "chat.team.private", gP, event.getMessage()));
				break;
			}
			Game.getInstance().getGamePlayers().forEach(gP -> send(gamePlayer, "chat.team.all", gP, message));
			break;
		case ENDING:
		case END:
			Game.getInstance().getGamePlayers().forEach(gP -> send(gamePlayer, "chat.default", gP, event.getMessage()));
			break;
		default:
			break;
		}
	}

	private String isAll(String message) {
		message = message.toLowerCase();
		final String msg = message;
		return ALL.stream().filter(text -> msg.startsWith(text)).sorted((s1, s2) -> s2.length() - s1.length())
				.findFirst().orElse(null);
	}

	private void send(GamePlayer gamePlayer, String key, GamePlayer gP, String message) {
		BaseComponent[] comp = Translation.translate(gP.getPlayer(), key, gamePlayer.getReplaces(gP));
		((TextComponent) comp[comp.length - 1]).addExtra(message);
		gP.getPlayer().spigot().sendMessage(comp);
	}
}
