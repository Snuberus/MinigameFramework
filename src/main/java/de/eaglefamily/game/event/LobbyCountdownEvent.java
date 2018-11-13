/**
 * Created by Jan on 13.11.2018 11:40:03
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * @author Jan
 */
public class LobbyCountdownEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public static class LobbyTickEvent extends Event {

		@Getter
		private int counter;

		public LobbyTickEvent(int counter) {
			this.counter = counter;
		}

		public HandlerList getHandlers() {
			return HANDLERS;
		}
	}

	public static class LobbySecondEvent extends Event {
		@Getter
		private int counter;

		public LobbySecondEvent(int counter) {
			this.counter = counter;
		}

		public int getSeconds() {
			return counter / 20;
		}

		public HandlerList getHandlers() {
			return HANDLERS;
		}
	}

}
