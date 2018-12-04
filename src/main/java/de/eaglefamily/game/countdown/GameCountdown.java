/*
 * Created by Jan on 28.07.2018 16:58:27
 */
package de.eaglefamily.game.countdown;

import org.bukkit.Bukkit;

import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.event.GameCountdownEvent.GameSecondEvent;
import de.eaglefamily.game.event.GameCountdownEvent.GameTickEvent;

/**
 * The Class GameCountdown.
 *
 * @author Jan
 */
public class GameCountdown extends Counter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.countdown.Counter#onTick()
	 */
	@Override
	protected void onTick() {
		Bukkit.getPluginManager().callEvent(new GameTickEvent(counter));
	}

	/* (non-Javadoc)
	 * @see de.eaglefamily.game.countdown.Counter#onSecond()
	 */
	@Override
	public void onSecond() {

		if (getCountdown() <= 0) {
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
			Message.send("game.undecidedcounter.xmin", "time", getCountdown() / 60);
			break;
		}

		Bukkit.getPluginManager().callEvent(new GameSecondEvent(counter));
	}

	private int getCountdown() {
		return Game.getInstance().getMapManager().getConfig().getInt("undecided") * 60 - counter / 20;
	}
}
