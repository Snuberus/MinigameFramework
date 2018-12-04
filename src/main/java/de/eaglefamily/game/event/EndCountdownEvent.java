/*
 * Created by Jan on 13.11.2018 12:01:21
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * The Class EndCountdownEvent.
 *
 * @author Jan
 */
public class EndCountdownEvent {

	/**
	 * The Class EndTickEvent.
	 *
	 * @author Jan
	 */
	public static class EndTickEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new end tick event.
		 *
		 * @param counter
		 *            the counter
		 */
		public EndTickEvent(int counter) {
			this.counter = counter;
		}

		/* (non-Javadoc)
		 * @see org.bukkit.event.Event#getHandlers()
		 */
		public HandlerList getHandlers() {
			return HANDLERS;
		}

		/**
		 * Gets the handler list.
		 *
		 * @return the handler list
		 */
		public static HandlerList getHandlerList() {
			return HANDLERS;
		}
	}

	/**
	 * The Class EndSecondEvent.
	 *
	 * @author Jan
	 */
	public static class EndSecondEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new end second event.
		 *
		 * @param counter
		 *            the counter
		 */
		public EndSecondEvent(int counter) {
			this.counter = counter;
		}

		/**
		 * Gets the seconds.
		 *
		 * @return the seconds
		 */
		public int getSeconds() {
			return counter / 20;
		}

		/* (non-Javadoc)
		 * @see org.bukkit.event.Event#getHandlers()
		 */
		public HandlerList getHandlers() {
			return HANDLERS;
		}

		/**
		 * Gets the handler list.
		 *
		 * @return the handler list
		 */
		public static HandlerList getHandlerList() {
			return HANDLERS;
		}
	}
}
