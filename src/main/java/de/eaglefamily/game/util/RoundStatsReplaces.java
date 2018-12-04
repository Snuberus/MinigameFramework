/*
 * Created by Jan on 15.08.2018 12:09:50
 */
package de.eaglefamily.game.util;

import java.util.List;

import de.eaglefamily.game.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Instantiates a new round stats replaces.
 *
 * @param gamePlayer
 *            the game player
 * @param playerStats
 *            the player stats
 */
@AllArgsConstructor

/**
 * Gets the player stats.
 *
 * @return the player stats
 */
@Getter
public abstract class RoundStatsReplaces {

	private final GamePlayer gamePlayer;
	private final RoundStats playerStats;

	/**
	 * Accept.
	 *
	 * @param replaces
	 *            the replaces
	 */
	public abstract void accept(List<Object> replaces);

}
