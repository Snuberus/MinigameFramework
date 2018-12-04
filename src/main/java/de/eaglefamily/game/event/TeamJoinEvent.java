/*
 * Created by Jan on 31.07.2018 18:52:30
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.GameTeam;
import lombok.Getter;

/**
 * The Class TeamJoinEvent.
 *
 * @author Jan
 */
public class TeamJoinEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Gets the game player.
	 *
	 * @return the game player
	 */
	@Getter
	private final GamePlayer gamePlayer;
	
	/**
	 * Gets the game team.
	 *
	 * @return the game team
	 */
	@Getter
	private final GameTeam gameTeam;

	/**
	 * Instantiates a new team join event.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @param gameTeam
	 *            the game team
	 */
	public TeamJoinEvent(GamePlayer gamePlayer, GameTeam gameTeam) {
		this.gamePlayer = gamePlayer;
		this.gameTeam = gameTeam;
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
