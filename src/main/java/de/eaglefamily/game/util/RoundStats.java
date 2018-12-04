/*
 * Created by Jan on 10.08.2018 21:16:59
 */
package de.eaglefamily.game.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class RoundStats.
 *
 * @author Jan
 */
public class RoundStats {

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	@Getter
	
	/**
	 * Sets the position.
	 *
	 * @param position
	 *            the new position
	 */
	@Setter
	private int position;
	
	/**
	 * Gets the game player.
	 *
	 * @return the game player
	 */
	@Getter
	private final GamePlayer gamePlayer;
	
	/**
	 * Gets the stats values.
	 *
	 * @return the stats values
	 */
	@Getter
	private final Map<StatsParameter, Object> statsValues;

	/**
	 * Instantiates a new round stats.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @param statsValues
	 *            the stats values
	 */
	public RoundStats(GamePlayer gamePlayer, Map<StatsParameter, Object> statsValues) {
		this.gamePlayer = gamePlayer;
		this.statsValues = statsValues;
	}

	/**
	 * Gets the stats.
	 *
	 * @param stats
	 *            the stats
	 * @return the stats
	 */
	public Object getStats(String stats) {
		return getStats(getStatsParameter(stats));
	}

	/**
	 * Gets the stats.
	 *
	 * @param statsParameter
	 *            the stats parameter
	 * @return the stats
	 */
	public Object getStats(StatsParameter statsParameter) {
		return statsValues.get(statsParameter);
	}

	/**
	 * Sets the stats.
	 *
	 * @param stats
	 *            the stats
	 * @param value
	 *            the value
	 */
	public void setStats(String stats, Object value) {
		if (value.equals(statsValues.get(getStatsParameter(stats)))) return;
		statsValues.put(getStatsParameter(stats), value);
	}

	/**
	 * Adds the stats.
	 *
	 * @param stats
	 *            the stats
	 * @param value
	 *            the value
	 */
	public void addStats(String stats, int value) {
		if (value == 0) return;
		int oldValue = (int) statsValues.get(getStatsParameter(stats));
		setStats(stats, oldValue + value);
	}

	/**
	 * Adds the stats.
	 *
	 * @param stats
	 *            the stats
	 * @param value
	 *            the value
	 */
	public void addStats(String stats, double value) {
		if (value == 0) return;
		double oldValue = (double) statsValues.get(getStats(stats));
		statsValues.put(getStatsParameter(stats), oldValue + value);
	}

	private StatsParameter getStatsParameter(String stats) {
		StatsParameter statsParameter = Settings.statsParameter.stream().filter(param -> param.getName().equals(stats))
				.findFirst().orElse(null);
		if (statsParameter == null) throw new IllegalArgumentException("Unknown stats type: " + stats);
		return statsParameter;
	}

	/**
	 * Gets the empty.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the empty
	 */
	public static RoundStats getEmpty(GamePlayer gamePlayer) {
		return new RoundStats(gamePlayer, Settings.statsParameter.stream()
				.collect(Collectors.toMap(Function.identity(), value -> value.getType() == boolean.class ? false : 0)));
	}
}
