/*
 * Created by Jan on 28.07.2018 23:12:34
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class CheckNameEvent.
 *
 * @author Jan
 */
public class CheckNameEvent extends Event {

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
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Getter
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	@Setter
	private String name;

	/**
	 * Instantiates a new check name event.
	 *
	 * @param player
	 *            the player
	 * @param target
	 *            the target
	 */
	public CheckNameEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
		name = target.getName();
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
