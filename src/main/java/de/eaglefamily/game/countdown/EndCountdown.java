/**
 * Created by _BlackEagle_ on 28.07.2018 16:59:31
 */
package de.eaglefamily.game.countdown;

import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class EndCountdown extends Counter {

	@Override
	public void onSecond() {
		if (getCountdown() <= 0) {
			stop();
			Game.getInstance().shutdown();
			return;
		}

		switch (getCountdown()) {
		case 1:
			Message.send("end.counter.1", "time", getCountdown());
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 20:
			Message.send("end.counter.x", "time", getCountdown());
			break;
		}
	}

	private int getCountdown() {
		return Settings.endCountdown - counter / 20;
	}
}
