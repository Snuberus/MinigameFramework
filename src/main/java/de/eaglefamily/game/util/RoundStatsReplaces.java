/**
 * Created by _BlackEagle_ on 15.08.2018 12:09:50
 */
package de.eaglefamily.game.util;

import java.util.List;

import de.eaglefamily.game.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
@AllArgsConstructor
@Getter
public abstract class RoundStatsReplaces {

	private final GamePlayer gamePlayer;
	private final RoundStats playerStats;

	public abstract void accept(List<Object> replaces);

}
