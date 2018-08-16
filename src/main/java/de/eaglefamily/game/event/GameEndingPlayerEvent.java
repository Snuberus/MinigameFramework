/**
 * Created by _BlackEagle_ on 13.08.2018 11:45:45
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
public class GameEndingPlayerEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final GamePlayer winner;

	public GameEndingPlayerEvent(GamePlayer winner) {
		this.winner = winner;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
