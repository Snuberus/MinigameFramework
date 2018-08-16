/**
 * Created by _BlackEagle_ on 28.07.2018 23:05:51
 */
package de.eaglefamily.game.event;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class CheckGroupKickEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final UUID uuid;
	@Getter
	@Setter
	private boolean kicked = true;

	public CheckGroupKickEvent(UUID uuid) {
		this.uuid = uuid;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
