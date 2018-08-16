/**
 * Created by _BlackEagle_ on 24.07.2018 15:46:46
 */
package de.eaglefamily.game;

import java.util.List;

import org.bukkit.Location;

import com.google.common.collect.Lists;

import de.eaglefamily.game.util.Settings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class GameTeam {

	@Getter
	private final List<GamePlayer> players = Lists.newArrayList();
	@Getter
	private final int index;
	@Setter
	private Location spawn;

	public GameTeam(int index) {
		this.index = index;
	}

	public void addPlayer(GamePlayer gamePlayer) {
		if (!players.contains(gamePlayer)) players.add(gamePlayer);
	}

	public void removePlayer(GamePlayer gamePlayer) {
		players.remove(gamePlayer);
	}

	public boolean isFull() {
		return players.size() >= Settings.numberOfPlayersPerTeam;
	}

	public Location getSpawn() {
		return spawn.clone();
	}

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

	public String getPrefix(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".prefix");
	}

	public String getSuffix(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".suffix");
	}

	public String getSortId(GamePlayer gamePlayer) {
		return gamePlayer.translateUTF("game.team." + index + ".sortId");
	}
}
