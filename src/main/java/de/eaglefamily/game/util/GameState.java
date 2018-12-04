/*
 * Created by Jan on 24.07.2018 11:36:02
 */
package de.eaglefamily.game.util;

import lombok.Getter;
import lombok.Setter;

/**
 * The Enum GameState.
 *
 * @author Jan
 */
public enum GameState {

	/** The start up. */
	START_UP, /** The lobby. */
 LOBBY, /** The starting. */
 STARTING, /** The ingame. */
 INGAME, /** The ending. */
 ENDING, /** The end. */
 END, /** The shutdown. */
 SHUTDOWN;

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@Getter
	
	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	@Setter
	private static GameState status = GameState.values()[0];

}
