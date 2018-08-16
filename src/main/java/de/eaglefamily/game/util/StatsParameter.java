/**
 * Created by _BlackEagle_ on 10.08.2018 19:33:43
 */
package de.eaglefamily.game.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
@AllArgsConstructor
@Getter
public class StatsParameter {

	private final String name;
	private final Class<?> type;
	private final int sortId;
	private final boolean descending;

}
