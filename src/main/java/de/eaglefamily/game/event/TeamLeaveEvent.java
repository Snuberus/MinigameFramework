/**
 * Created by _BlackEagle_ on 31.07.2018 18:53:20
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.GameTeam;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
public class TeamLeaveEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final GamePlayer gamePlayer;
	@Getter
	private final GameTeam team;

	public TeamLeaveEvent(GamePlayer gamePlayer, GameTeam team) {
		this.gamePlayer = gamePlayer;
		this.team = team;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
