/**
 * Created by _BlackEagle_ on 28.07.2018 16:58:27
 */
package de.eaglefamily.game.countdown;

import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;

/**
 * @author _BlackEagle_
 */
public class GameCountdown extends Counter {

	@Override
	public void onSecond() {

		if (getCountdown() <= 0) {
			stop();
			Game.getInstance().endPlayer(null);
			return;
		}

		switch (getCountdown()) {
		case 1:
			Message.send("game.undecidedcounter.1", "time", getCountdown());
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 10:
		case 30:
		case 60:
			Message.send("game.undecidedcounter.xsec", "time", getCountdown());
			break;
		case 120:
		case 300:
			Message.send("game.undecidedcounter.xmin", "time", getCountdown());
			break;
		}
	}

	private int getCountdown() {
		return Game.getInstance().getMapManager().getConfig().getInt("undecided") * 60 - counter / 20;
	}
}
