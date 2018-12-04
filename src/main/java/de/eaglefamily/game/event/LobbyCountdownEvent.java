/*
 * Created by Jan on 13.11.2018 11:40:03
 */
package de.eaglefamily.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * The Class LobbyCountdownEvent.
 *
 * @author Jan
 */
public class LobbyCountdownEvent {

	/**
	 * The Class LobbyTickEvent.
	 *
	 * @author Jan
	 */
	public static class LobbyTickEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new lobby tick event.
		 *
		 * @param counter
		 *            the counter
		 */
		public LobbyTickEvent(int counter) {
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
	 * The Class LobbySecondEvent.
	 *
	 * @author Jan
	 */
	public static class LobbySecondEvent extends Event {

		private static final HandlerList HANDLERS = new HandlerList();
		
		/**
		 * Gets the counter.
		 *
		 * @return the counter
		 */
		@Getter
		private int counter;

		/**
		 * Instantiates a new lobby second event.
		 *
		 * @param counter
		 *            the counter
		 */
		public LobbySecondEvent(int counter) {
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
