/*
 * Created by Jan on 28.07.2018 23:42:03
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class CheckGroupEvent.
 *
 * @author Jan
 */
public class CheckGroupEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	@Getter
	private final Player player;
	
	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	@Getter
	private final Player target;
	
	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	@Getter
	
	/**
	 * Sets the group.
	 *
	 * @param group
	 *            the new group
	 */
	@Setter
	private String group;

	/**
	 * Instantiates a new check group event.
	 *
	 * @param player
	 *            the player
	 * @param target
	 *            the target
	 */
	public CheckGroupEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
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
