/**
 * Created by _BlackEagle_ on 29.07.2018 01:00:37
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author _BlackEagle_
 */
public class GameStartEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
