/**
 * Created by _BlackEagle_ on 10.08.2018 18:47:09
 */
package de.eaglefamily.game.util;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
@AllArgsConstructor
@Getter
public class Stats {

	private final UUID uuid;
	private final int position;
	private final Map<StatsParameter, Object> statsValues;

	public Object getStats(StatsParameter statsParameter) {
		return statsValues.get(statsParameter);
	}

	public Object getStats(String stats) {
		return getStats(getStatsParameter(stats));
	}

	private StatsParameter getStatsParameter(String stats) {
		return Settings.statsParameter.stream().filter(statsParameter -> statsParameter.getName().equals(stats))
				.findFirst().orElse(null);
	}

	public static Stats getEmpty(UUID uuid) {
		return new Stats(uuid, 0,
				Settings.statsParameter.stream().collect(Collectors.toMap(Function.identity(), value -> 0)));
	}

}
