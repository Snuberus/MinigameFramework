/**
 * Created by _BlackEagle_ on 29.07.2018 01:00:58
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GameTeam;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
public class GameEndTeamEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final GameTeam winner;

	public GameEndTeamEvent(GameTeam winner) {
		this.winner = winner;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
