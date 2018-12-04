/*
 * Created by Jan on 13.08.2018 11:39:47
 */
package de.eaglefamily.game.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.google.common.collect.Lists;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.event.GameEndingPlayerEvent;
import de.eaglefamily.game.event.GameEndingTeamEvent;
import de.eaglefamily.game.event.GameStartEvent;

/**
 * The Object of a listener interface for receiving gameStartEnd events. When
 * the gameStartEnd event occurs, that object's appropriate method is invoked.
 *
 * @see GameStartEndEvent
 */
public class GameStartEndListener implements Listener {

	private final List<Listener> gameListener = Lists.newArrayList();

	/**
	 * Adds the listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addListener(Listener listener) {
		gameListener.add(listener);
	}

	/**
	 * On game start.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onGameStart(GameStartEvent event) {
		gameListener.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, Game.getPlugin()));
	}

	/**
	 * On game end player.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onGameEndPlayer(GameEndingPlayerEvent event) {
		gameListener.forEach(HandlerList::unregisterAll);
	}

	/**
	 * On game end team.
	 *
	 * @param event
	 *            the event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onGameEndTeam(GameEndingTeamEvent event) {
		gameListener.forEach(HandlerList::unregisterAll);
	}
}
