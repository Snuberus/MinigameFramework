/**
 * Created by _BlackEagle_ on 28.07.2018 23:42:03
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class CheckGroupEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	@Getter
	private final Player player;
	@Getter
	private final Player target;
	@Getter
	@Setter
	private String group;

	public CheckGroupEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
