/*
 * Created by Jan on 13.08.2018 11:45:45
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GameTeam;
import lombok.Getter;

/**
 * The Class GameEndingTeamEvent.
 *
 * @author Jan
 */
public class GameEndingTeamEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Gets the winner.
	 *
	 * @return the winner
	 */
	@Getter
	private final GameTeam winner;

	/**
	 * Instantiates a new game ending team event.
	 *
	 * @param winner
	 *            the winner
	 */
	public GameEndingTeamEvent(GameTeam winner) {
		this.winner = winner;
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
