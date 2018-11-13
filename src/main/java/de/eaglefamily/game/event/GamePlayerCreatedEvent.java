/**
 * Created by Jan on 13.11.2018 10:27:53
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;

/**
 * @author Jan
 */
public class GamePlayerCreatedEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final GamePlayer gamePlayer;

	public GamePlayerCreatedEvent(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
