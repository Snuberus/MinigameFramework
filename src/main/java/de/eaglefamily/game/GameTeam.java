/*
 * Created by Jan on 24.07.2018 15:46:46
 */
package de.eaglefamily.game;

import java.util.List;

import org.bukkit.Location;

import com.google.common.collect.Lists;

import de.eaglefamily.game.util.Settings;
import lombok.Getter;
import lombok.Setter;

/**
 * The Class GameTeam.
 *
 * @author Jan
 */
public class GameTeam {

	/**
	 * Gets the players.
	 *
	 * @return the players
	 */
	@Getter
	private final List<GamePlayer> players = Lists.newArrayList();
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	@Getter
	private final int index;
	
	/**
	 * Sets the spawn.
	 *
	 * @param spawn
	 *            the new spawn
	 */
	@Setter
	private Location spawn;

	/**
	 * Instantiates a new game team.
	 *
	 * @param index
	 *            the index
	 */
	public GameTeam(int index) {
		this.index = index;
	}

	/**
	 * Adds the player.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void addPlayer(GamePlayer gamePlayer) {
		if (!players.contains(gamePlayer)) players.add(gamePlayer);
	}

	/**
	 * Removes the player.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void removePlayer(GamePlayer gamePlayer) {
		players.remove(gamePlayer);
	}

	/**
	 * Checks if is full.
	 *
	 * @return true, if is full
	 */
	public boolean isFull() {
		return players.size() >= Settings.numberOfPlayersPerTeam;
	}

	/**
	 * Gets the spawn.
	 *
	 * @return the spawn
	 */
	public Location getSpawn() {
		return spawn.clone();
	}

	/**
	 * Gets the replaces.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the replaces
	 */
	public Object[] getReplaces(GamePlayer gamePlayer) {
		List<Object> replaces = Lists.newArrayList();
		replaces.add("index");
		replaces.add(index);
		replaces.add("team");
		replaces.add(gamePlayer.translateUTF("game.team." + index + ".name"));
		replaces.add("teamcolor");
		replaces.add(gamePlayer.translateUTF("game.team." + index + ".color"));
		replaces.add("current");
		replaces.add(players.size());
		replaces.add("size");
		replaces.add(Settings.numberOfPlayersPerTeam);
		return replaces.toArray();
	}

	/**
	 * Gets the prefix.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the prefix
	 */
	public String getPrefix(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".prefix");
	}

	/**
	 * Gets the suffix.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the suffix
	 */
	public String getSuffix(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".suffix");
	}

	/**
	 * Gets the sort id.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the sort id
	 */
	public String getSortId(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".sortId");
	}
}
