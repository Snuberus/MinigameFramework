/*
 * Created by Jan on 24.07.2018 11:33:03
 */
package de.eaglefamily.game.database;

import java.util.UUID;
import java.util.function.Consumer;

import de.eaglefamily.game.util.RoundStats;
import de.eaglefamily.game.util.Stats;

/**
 * The Interface Database.
 *
 * @author Jan
 */
public interface Database {

	/**
	 * Connect.
	 */
	public void connect();

	/**
	 * Disconnect.
	 */
	public void disconnect();

	/**
	 * Update player.
	 *
	 * @param uuid
	 *            the uuid
	 * @param name
	 *            the name
	 * @param skin
	 *            the skin
	 * @param sync
	 *            the sync
	 */
	public void updatePlayer(UUID uuid, String name, String skin, boolean sync);

	/**
	 * Gets the name.
	 *
	 * @param uuid
	 *            the uuid
	 * @param consumer
	 *            the consumer
	 * @param sync
	 *            the sync
	 * @return the name
	 */
	public void getName(UUID uuid, Consumer<String> consumer, boolean sync);

	/**
	 * Gets the uuid.
	 *
	 * @param name
	 *            the name
	 * @param consumer
	 *            the consumer
	 * @param sync
	 *            the sync
	 * @return the uuid
	 */
	public void getUUID(String name, Consumer<UUID> consumer, boolean sync);

	/**
	 * Gets the skin.
	 *
	 * @param uuid
	 *            the uuid
	 * @param consumer
	 *            the consumer
	 * @param sync
	 *            the sync
	 * @return the skin
	 */
	public void getSkin(UUID uuid, Consumer<String> consumer, boolean sync);

	/**
	 * Gets the stats.
	 *
	 * @param uuid
	 *            the uuid
	 * @param time
	 *            the time
	 * @param consumer
	 *            the consumer
	 * @param sync
	 *            the sync
	 * @return the stats
	 */
	public void getStats(UUID uuid, long time, Consumer<Stats> consumer, boolean sync);

	/**
	 * Gets the stats.
	 *
	 * @param position
	 *            the position
	 * @param time
	 *            the time
	 * @param consumer
	 *            the consumer
	 * @param sync
	 *            the sync
	 * @return the stats
	 */
	public void getStats(int position, long time, Consumer<Stats> consumer, boolean sync);

	/**
	 * Sets the stats.
	 *
	 * @param uuid
	 *            the uuid
	 * @param stats
	 *            the stats
	 * @param sync
	 *            the sync
	 */
	public void setStats(UUID uuid, RoundStats stats, boolean sync);

}
