/*
 * Created by Jan on 29.07.2018 01:00:37
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The Class GameStartEvent.
 *
 * @author Jan
 */
public class GameStartEvent extends Event {

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
