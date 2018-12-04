/*
 * Created by Jan on 10.08.2018 19:33:43
 */
package de.eaglefamily.game.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Instantiates a new stats parameter.
 *
 * @param name
 *            the name
 * @param type
 *            the type
 * @param sortId
 *            the sort id
 * @param descending
 *            the descending
 */
@AllArgsConstructor

/**
 * Checks if is descending.
 *
 * @return true, if is descending
 */
@Getter
public class StatsParameter {

	private final String name;
	private final Class<?> type;
	private final int sortId;
	private final boolean descending;

}
