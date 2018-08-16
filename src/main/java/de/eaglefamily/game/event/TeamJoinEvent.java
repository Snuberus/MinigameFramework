/**
 * Created by _BlackEagle_ on 31.07.2018 18:52:30
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
public class TeamJoinEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final GamePlayer gamePlayer;
	@Getter
	private final GameTeam gameTeam;

	public TeamJoinEvent(GamePlayer gamePlayer, GameTeam gameTeam) {
		this.gamePlayer = gamePlayer;
		this.gameTeam = gameTeam;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
