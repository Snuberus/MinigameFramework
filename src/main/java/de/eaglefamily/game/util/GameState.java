/**
 * Created by _BlackEagle_ on 24.07.2018 11:36:02
 */
package de.eaglefamily.game.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public enum GameState {

	START_UP, LOBBY, STARTING, INGAME, ENDING, END, SHUTDOWN;

	@Getter
	@Setter
	private static GameState status = GameState.values()[0];

}
