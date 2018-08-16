/**
 * Created by _BlackEagle_ on 29.07.2018 01:07:23
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.GamePlayer;
import lombok.Getter;

/**
 * @author _BlackEagle_
 */
public class GameEndPlayerEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final GamePlayer winner;

	public GameEndPlayerEvent(GamePlayer winner) {
		this.winner = winner;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
