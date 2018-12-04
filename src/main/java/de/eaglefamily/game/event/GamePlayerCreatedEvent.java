/*
 * Created by Jan on 13.11.2018 10:27:53
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;

/**
 * The Class GamePlayerCreatedEvent.
 *
 * @author Jan
 */
public class GamePlayerCreatedEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	/**
	 * Gets the game player.
	 *
	 * @return the game player
	 */
	@Getter
	private final GamePlayer gamePlayer;

	/**
	 * Instantiates a new game player created event.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public GamePlayerCreatedEvent(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	/**
	 * Gets the handler list.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
