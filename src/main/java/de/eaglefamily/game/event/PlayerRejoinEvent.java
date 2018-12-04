/*
 * Created by Jan on 20.08.2018 20:21:44
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class PlayerRejoinEvent.
 *
 * @author Jan
 */
public class PlayerRejoinEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#isCancelled()
	 */
	@Getter
	
	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#setCancelled(boolean)
	 */
	@Setter
	private boolean cancelled;
	
	/**
	 * Gets the game player.
	 *
	 * @return the game player
	 */
	@Getter
	private final GamePlayer gamePlayer;

	/**
	 * Instantiates a new player rejoin event.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public PlayerRejoinEvent(GamePlayer gamePlayer) {
		this.gamePlayer = gamePlayer;
	}

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return gamePlayer.getPlayer();
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
