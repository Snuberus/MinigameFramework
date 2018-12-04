/*
 * Created by Jan on 06.01.2018 16:20:04
 */
package de.eaglefamily.game.countdown;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import lombok.Getter;

/**
 * The Class Counter.
 *
 * @author Jan
 */
public abstract class Counter {

	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 */
	@Getter
	protected int counter;
	
	/**
	 * Checks if is running.
	 *
	 * @return true, if is running
	 */
	@Getter
	protected boolean isRunning;

	private final Runnable task = () -> {
		counter++;
		onTick();
		if (counter % 20 == 0) onSecond();
	};

	/**
	 * Start.
	 */
	public final void start() {
		isRunning = true;
		counter = 0;
		TaskManager.runTaskTimer(0l, 1l, task);
	}

	/**
	 * Stop.
	 */
	public final void stop() {
		TaskManager.cancel(task);
		isRunning = false;
	}

	/**
	 * On tick.
	 */
	protected void onTick() {}

	/**
	 * On second.
	 */
	protected void onSecond() {}

}
