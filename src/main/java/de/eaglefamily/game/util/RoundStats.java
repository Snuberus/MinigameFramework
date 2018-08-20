/**
 * Created by _BlackEagle_ on 10.08.2018 21:16:59
 */
package de.eaglefamily.game.util;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class RoundStats {

	@Getter
	@Setter
	private int position;
	@Getter
	private final GamePlayer gamePlayer;
	@Getter
	private final Map<StatsParameter, Object> statsValues;

	public RoundStats(GamePlayer gamePlayer, Map<StatsParameter, Object> statsValues) {
		this.gamePlayer = gamePlayer;
		this.statsValues = statsValues;
	}

	public Object getStats(String stats) {
		return getStats(getStatsParameter(stats));
	}

	public Object getStats(StatsParameter statsParameter) {
		return statsValues.get(statsParameter);
	}

	public void setStats(String stats, Object value) {
		if (value.equals(statsValues.get(getStatsParameter(stats)))) return;
		statsValues.put(getStatsParameter(stats), value);
	}

	public void addStats(String stats, int value) {
		if (value == 0) return;
		int oldValue = (int) statsValues.get(getStatsParameter(stats));
		setStats(stats, oldValue + value);
	}

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

	public static RoundStats getEmpty(GamePlayer gamePlayer) {
		return new RoundStats(gamePlayer, Settings.statsParameter.stream()
				.collect(Collectors.toMap(Function.identity(), value -> value.getType() == boolean.class ? false : 0)));
	}
}
