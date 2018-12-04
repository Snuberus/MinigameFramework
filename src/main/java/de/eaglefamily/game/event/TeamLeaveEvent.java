/*
 * Created by Jan on 31.07.2018 18:53:20
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.GameTeam;
import lombok.Getter;

/**
 * The Class TeamLeaveEvent.
 *
 * @author Jan
 */
public class TeamLeaveEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Gets the game player.
	 *
	 * @return the game player
	 */
	@Getter
	private final GamePlayer gamePlayer;
	
	/**
	 * Gets the team.
	 *
	 * @return the team
	 */
	@Getter
	private final GameTeam team;

	/**
	 * Instantiates a new team leave event.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @param team
	 *            the team
	 */
	public TeamLeaveEvent(GamePlayer gamePlayer, GameTeam team) {
		this.gamePlayer = gamePlayer;
		this.team = team;
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
