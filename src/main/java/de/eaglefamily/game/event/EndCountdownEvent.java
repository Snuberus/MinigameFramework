/**
 * Created by Jan on 13.11.2018 12:01:21
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * @author Jan
 */
public class EndCountdownEvent {

	public static class EndTickEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		@Getter
		private int counter;

		public EndTickEvent(int counter) {
			this.counter = counter;
		}

		public HandlerList getHandlers() {
			return HANDLERS;
		}

		public static HandlerList getHandlerList() {
			return HANDLERS;
		}
	}

	public static class EndSecondEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		@Getter
		private int counter;

		public EndSecondEvent(int counter) {
			this.counter = counter;
		}

		public int getSeconds() {
			return counter / 20;
		}

		public HandlerList getHandlers() {
			return HANDLERS;
		}

		public static HandlerList getHandlerList() {
			return HANDLERS;
		}
	}
}
