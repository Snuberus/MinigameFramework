/*
 * Created by Jan on 28.07.2018 23:05:51
 */
package de.eaglefamily.game.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class CheckGroupKickEvent.
 *
 * @author Jan
 */
public class CheckGroupKickEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	@Getter
	private final UUID uuid;
	
	/**
	 * Checks if is kicked.
	 *
	 * @return true, if is kicked
	 */
	@Getter
	
	/**
	 * Sets the kicked.
	 *
	 * @param kicked
	 *            the new kicked
	 */
	@Setter
	private boolean kicked = true;

	/**
	 * Instantiates a new check group kick event.
	 *
	 * @param uuid
	 *            the uuid
	 */
	public CheckGroupKickEvent(UUID uuid) {
		this.uuid = uuid;
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
