/**
 * Created by _BlackEagle_ on 24.07.2018 15:48:14
 */
package de.eaglefamily.game.countdown;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.event.LobbyCountdownEvent.LobbySecondEvent;
import de.eaglefamily.game.event.LobbyCountdownEvent.LobbyTickEvent;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Settings;

/**
 * @author _BlackEagle_
 */
public class LobbyCountdown extends Counter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.countdown.Counter#onTick()
	 */
	@Override
	protected void onTick() {
		Bukkit.getPluginManager().callEvent(new LobbyTickEvent(counter));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.countdown.Countdown#onSecond()
	 */
	@Override
	protected void onSecond() {

		Game.getInstance().getGamePlayers().forEach(gP -> {
			gP.getPlayer().setLevel(getCountdown());
			gP.getPlayer().setExp((1f / (Settings.lobbyCountdown * 20f)) * (Settings.lobbyCountdown * 20 - counter));
		});
		if (getCountdown() <= 0) {
			stop();
			Game.getInstance().startGame();
			return;
		}

		switch (getCountdown()) {
		case 1:
			Message.send("lobby.counter.1", "time", getCountdown());
			Game.getInstance().getGamePlayers()
					.forEach(gP -> gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1));
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 10:
		case 30:
		case 60:
			Message.send("lobby.counter.x", "time", getCountdown());
			Game.getInstance().getGamePlayers()
					.forEach(gP -> gP.getPlayer().playSound(gP.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1));
			break;
		}

		if (getCountdown() == Settings.lobbyShortStart - 1) Game.getInstance().prepareGame();

		Game.getInstance().getGamePlayers().forEach(GamePlayer::updateLobbySidebar);
		Bukkit.getPluginManager().callEvent(new LobbySecondEvent(counter));
	}

	public void update() {
		if (GameState.getStatus() != GameState.LOBBY && isRunning) stop();
		if (GameState.getStatus() != GameState.LOBBY) return;
		if (isRunning && Settings.playerForLobbyStart > Game.getInstance().getGamePlayers().size()) {
			stop();
			Game.getInstance().getGamePlayers().forEach(gP -> {
				gP.send("lobby.counter.stop");
				gP.getPlayer().setLevel(0);
				gP.getPlayer().setExp(1);
				gP.updateLobbySidebar();
			});
		}
		if (!isRunning && Settings.playerForLobbyStart <= Game.getInstance().getGamePlayers().size()) {
			start();
			if (Settings.playersForLobbyShortStart > Game.getInstance().getGamePlayers().size()) onSecond();
		}
		if (isRunning && Settings.playersForLobbyShortStart <= Game.getInstance().getGamePlayers().size()) {
			lobbyShortStart();
		}
	}

	public void lobbyShortStart() {
		if (getCountdown() > Settings.lobbyShortStart) counter = Settings.lobbyCountdown * 20
				- Settings.lobbyShortStart * 20 - 1;

	}

	public int getCountdown() {
		return Settings.lobbyCountdown - counter / 20;
	}

}
