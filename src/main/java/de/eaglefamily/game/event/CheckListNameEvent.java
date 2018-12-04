/*
 * Created by Jan on 28.07.2018 23:27:14
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.util.ListName;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class CheckListNameEvent.
 *
 * @author Jan
 */
public class CheckListNameEvent extends Event {

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
	 * Gets the prefix.
	 *
	 * @return the prefix
	 */
	@Getter
	
	/**
	 * Sets the prefix.
	 *
	 * @param prefix
	 *            the new prefix
	 */
	@Setter
	private String prefix;
	
	/**
	 * Gets the suffix.
	 *
	 * @return the suffix
	 */
	@Getter
	
	/**
	 * Sets the suffix.
	 *
	 * @param suffix
	 *            the new suffix
	 */
	@Setter
	private String suffix;
	
	/**
	 * Gets the sort id.
	 *
	 * @return the sort id
	 */
	@Getter
	
	/**
	 * Sets the sort id.
	 *
	 * @param sortId
	 *            the new sort id
	 */
	@Setter
	private String sortId;

	/**
	 * Instantiates a new check list name event.
	 *
	 * @param player
	 *            the player
	 * @param target
	 *            the target
	 */
	public CheckListNameEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
	}

	/**
	 * Gets the list name.
	 *
	 * @return the list name
	 */
	public ListName getListName() {
		return new ListName(prefix, suffix, sortId);
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
