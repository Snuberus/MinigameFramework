/*
 * Created by Jan on 31.07.2018 22:19:27
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The Class GameStartingEvent.
 *
 * @author Jan
 */
public class GameStartingEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

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
