/**
 * Created by _BlackEagle_ on 12.08.2018 13:57:44
 */
package de.eaglefamily.game.util;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
@AllArgsConstructor
@Getter
public abstract class StatsReplaces {

	private final UUID uuid;
	private final Stats playerStats;

	public abstract void accept(List<Object> replaces);
}
