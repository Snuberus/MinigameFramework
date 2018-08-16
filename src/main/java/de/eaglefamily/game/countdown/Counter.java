/*******************************************************************************
 * Copyright (c) 2018 _BlackEagle_, EagleFamily.de - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * This file is proprietary and confidential.
 * Written by _BlackEagle_ on 2018
 *******************************************************************************/
package de.eaglefamily.game.countdown;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import lombok.Getter;

/**
 * Created by _BlackEagle_ on 06.01.2018 16:20:04
 */
public abstract class Counter {

	@Getter
	protected int counter;
	@Getter
	protected boolean isRunning;

	private final Runnable task = () -> {
		counter++;
		onTick();
		if (counter % 20 == 0) onSecond();
	};

	public final void start() {
		isRunning = true;
		counter = 0;
		TaskManager.runTaskTimer(0l, 1l, task);
	}

	public final void stop() {
		TaskManager.cancel(task);
		isRunning = false;
	}

	protected void onTick() {}

	protected void onSecond() {}

}
