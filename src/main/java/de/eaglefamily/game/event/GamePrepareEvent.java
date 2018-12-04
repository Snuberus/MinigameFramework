/*
 * Created by Jan on 29.07.2018 01:00:02
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * The Class GamePrepareEvent.
 *
 * @author Jan
 */
public class GamePrepareEvent extends Event {
	
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
