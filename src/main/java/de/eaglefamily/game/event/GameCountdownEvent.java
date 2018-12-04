/*
 * Created by Jan on 13.11.2018 12:00:48
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * The Class GameCountdownEvent.
 *
 * @author Jan
 */
public class GameCountdownEvent {

	/**
	 * The Class GameTickEvent.
	 *
	 * @author Jan
	 */
	public static class GameTickEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new game tick event.
		 *
		 * @param counter
		 *            the counter
		 */
		public GameTickEvent(int counter) {
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
	 * The Class GameSecondEvent.
	 *
	 * @author Jan
	 */
	public static class GameSecondEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new game second event.
		 *
		 * @param counter
		 *            the counter
		 */
		public GameSecondEvent(int counter) {
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
