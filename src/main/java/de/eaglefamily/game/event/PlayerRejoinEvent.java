/**
 * Created by Jan on 20.08.2018 20:21:44
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jan
 */
public class PlayerRejoinEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	@Setter
	private boolean cancelled;
	@Getter
	private final GamePlayer gamePlayer;

	public PlayerRejoinEvent(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public Player getPlayer() {
		return gamePlayer.getPlayer();
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
