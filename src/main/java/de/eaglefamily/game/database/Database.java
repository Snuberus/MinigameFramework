/**
 * Created by _BlackEagle_ on 24.07.2018 11:33:03
 */
package de.eaglefamily.game.database;

import java.util.UUID;
import java.util.function.Consumer;

import de.eaglefamily.game.util.RoundStats;
import de.eaglefamily.game.util.Stats;

/**
 * @author _BlackEagle_
 */
public interface Database {

	public void connect();

	public void disconnect();

	public void updatePlayer(UUID uuid, String name, String skin, boolean sync);

	public void getName(UUID uuid, Consumer<String> consumer, boolean sync);

	public void getUUID(String name, Consumer<UUID> consumer, boolean sync);

	public void getSkin(UUID uuid, Consumer<String> consumer, boolean sync);

	public void getStats(UUID uuid, long time, Consumer<Stats> consumer, boolean sync);

	public void getStats(int position, long time, Consumer<Stats> consumer, boolean sync);

	public void setStats(UUID uuid, RoundStats stats, boolean sync);

}
