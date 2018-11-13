/**
 * Created by Jan on 13.11.2018 12:00:48
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * @author Jan
 */
public class GameCountdownEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public static class GameTickEvent extends Event {

		@Getter
		private int counter;

		public GameTickEvent(int counter) {
			this.counter = counter;
		}

		public HandlerList getHandlers() {
			return HANDLERS;
		}
	}

	public static class GameSecondEvent extends Event {

		@Getter
		private int counter;

		public GameSecondEvent(int counter) {
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
