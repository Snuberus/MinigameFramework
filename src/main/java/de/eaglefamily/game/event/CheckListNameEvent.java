/**
 * Created by _BlackEagle_ on 28.07.2018 23:27:14
 */
package de.eaglefamily.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.eaglefamily.game.util.ListName;
import lombok.Getter;
import lombok.Setter;

/**
 * @author _BlackEagle_
 */
public class CheckListNameEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final Player player;
	@Getter
	private final Player target;

	@Getter
	@Setter
	private String prefix;
	@Getter
	@Setter
	private String suffix;
	@Getter
	@Setter
	private String sortId;

	public CheckListNameEvent(Player player, Player target) {
		this.player = player;
		this.target = target;
	}

	public ListName getListName() {
		return new ListName(prefix, suffix, sortId);
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
