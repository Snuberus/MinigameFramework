/*
 * Created by Jan on 10.08.2018 18:47:09
 */
package de.eaglefamily.game.util;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Instantiates a new stats.
 *
 * @param uuid
 *            the uuid
 * @param position
 *            the position
 * @param statsValues
 *            the stats values
 */
@AllArgsConstructor

/**
 * Gets the stats values.
 *
 * @return the stats values
 */
@Getter
public class Stats {

	private final UUID uuid;
	private final int position;
	private final Map<StatsParameter, Object> statsValues;

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
	 * Gets the stats.
	 *
	 * @param stats
	 *            the stats
	 * @return the stats
	 */
	public Object getStats(String stats) {
		return getStats(getStatsParameter(stats));
	}

	private StatsParameter getStatsParameter(String stats) {
		return Settings.statsParameter.stream().filter(statsParameter -> statsParameter.getName().equals(stats))
				.findFirst().orElse(null);
	}

	/**
	 * Gets the empty.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the empty
	 */
	public static Stats getEmpty(UUID uuid) {
		return new Stats(uuid, 0,
				Settings.statsParameter.stream().collect(Collectors.toMap(Function.identity(), value -> 0)));
	}

}
