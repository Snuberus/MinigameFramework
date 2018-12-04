/*
 * Created by Jan on 12.08.2018 13:57:44
 */
package de.eaglefamily.game.util;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Instantiates a new stats replaces.
 *
 * @param uuid
 *            the uuid
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
public abstract class StatsReplaces {

	private final UUID uuid;
	private final Stats playerStats;

	/**
	 * Accept.
	 *
	 * @param replaces
	 *            the replaces
	 */
	public abstract void accept(List<Object> replaces);
}
