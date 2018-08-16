/**
 * Created by _BlackEagle_ on 24.07.2018 11:53:29
 */
package de.eaglefamily.game.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.RoundStats;
import de.eaglefamily.game.util.Settings;
import de.eaglefamily.game.util.Stats;
import de.eaglefamily.game.util.StatsParameter;

/**
 * @author _BlackEagle_
 */
public class MySql implements Database {

	private Connection connection;

	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	private String userTable;
	private String statsTable;

	public MySql(String host, int port, String database, String username, String password, String userTable,
			String statsTable) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.userTable = userTable;
		this.statsTable = statsTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#connect()
	 */
	@Override
	public void connect() {
		if (connection != null) return;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database
					+ "?autoReconnect=true&useInformationSchema=true", username, password);
			Game.getPlugin().getLogger()
					.info("Connection to the database '" + database + "' was successfully established!");
			createTable();
			createStatsTable();
		} catch (SQLException e) {
			Game.getPlugin().getLogger()
					.warning("Connection to the database '" + database + "' could not be established!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#disconnect()
	 */
	@Override
	public void disconnect() {
		if (connection == null) return;
		try {
			connection.close();
			connection = null;
			Game.getPlugin().getLogger().info("Connection to the database was closed successfully!");
		} catch (SQLException e) {
			Game.getPlugin().getLogger().warning("Connection to the database could not be closed!");
		}
	}

	private void createTable() {
		if (connection == null) return;
		String userStatement = "CREATE TABLE IF NOT EXISTS " + userTable
				+ " (uuid varchar(36) NOT NULL, PRIMARY KEY (uuid), " + "name varchar(16) NOT NULL, skin TEXT)";
		try (PreparedStatement ps = connection.prepareStatement(userStatement)) {
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#updatePlayer(java.util.UUID,
	 * java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void updatePlayer(UUID uuid, String name, String skin, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			if (!existsPlayer(uuid)) {
				createPlayer(uuid, name, skin);
				return;
			}
			String statement = "UPDATE " + userTable + " SET name = ?, skin = ? WHERE uuid = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setString(1, name);
				ps.setString(2, skin);
				ps.setString(3, uuid.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#getName(java.util.UUID,
	 * java.util.function.Consumer, boolean)
	 */
	@Override
	public void getName(UUID uuid, Consumer<String> consumer, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			String statement = "SELECT name FROM " + userTable + " WHERE uuid = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					consumer.accept(rs.getString("name"));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			consumer.accept(null);
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#getUUID(java.lang.String,
	 * java.util.function.Consumer, boolean)
	 */
	@Override
	public void getUUID(String name, Consumer<UUID> consumer, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			String statement = "SELECT uuid FROM " + userTable + " WHERE name = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setString(1, name);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					consumer.accept(UUID.fromString(rs.getString("uuid")));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			consumer.accept(null);
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#getSkin(java.util.UUID,
	 * java.util.function.Consumer, boolean)
	 */
	@Override
	public void getSkin(UUID uuid, Consumer<String> consumer, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			String statement = "SELECT skin FROM " + userTable + " WHERE uuid = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					consumer.accept(rs.getString("skin"));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			consumer.accept(null);
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	private boolean existsPlayer(UUID uuid) {
		String statement = "SELECT uuid FROM " + userTable + " WHERE uuid = ?";
		try (PreparedStatement ps = connection.prepareStatement(statement)) {
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void createPlayer(UUID uuid, String name, String skin) {
		String statement = "INSERT INTO " + userTable + "(uuid, name, skin) VALUES (?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(statement)) {
			ps.setString(1, uuid.toString());
			ps.setString(2, name);
			ps.setString(3, skin);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createStatsTable() {
		String statsColumn = "";
		for (StatsParameter statsParameter : Settings.statsParameter) {
			statsColumn += ", " + statsParameter.getName();
			if (statsParameter.getType() == int.class) statsColumn += " int(8)";
			if (statsParameter.getType() == double.class) statsColumn += " double";
			if (statsParameter.getType() == boolean.class) statsColumn += " tinyint(1)";
		}
		String statsStatement = "CREATE TABLE IF NOT EXISTS " + statsTable
				+ " (id int(11) NOT NULL AUTO_INCREMENT, PRIMARY KEY (id), uuid varchar(36) NOT NULL, statstime bigint(32) NOT NULL"
				+ statsColumn + ")";
		try (PreparedStatement ps = connection.prepareStatement(statsStatement)) {
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#getStats(java.util.UUID,
	 * java.util.function.Consumer, boolean)
	 */
	@Override
	public void getStats(UUID uuid, long time, Consumer<Stats> consumer, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {

			String statsColumn = "";
			String statsSum = "";
			String statsOrder = "";
			for (StatsParameter statsParameter : Settings.statsParameter) {
				statsColumn += ", " + statsParameter.getName();
				statsSum += ", SUM(" + statsParameter.getName() + ") AS `" + statsParameter.getName() + "`";
			}

			List<StatsParameter> statsParameters = Lists.newArrayList(Settings.statsParameter);
			statsParameters.sort((s1, s2) -> s1.getSortId() - s2.getSortId());

			for (StatsParameter statsParameter : statsParameters) {
				statsOrder += statsParameter.getName() + " " + (statsParameter.isDescending() ? "DESC, " : "ASC, ");
			}

			String statement = "SELECT position, uuid" + statsColumn + " FROM (SELECT (@pos := @pos+1) position, uuid"
					+ statsColumn + " FROM (SELECT @pos := 0) p, (SELECT uuid" + statsSum + " FROM " + statsTable
					+ " WHERE statstime >= ? GROUP BY uuid ORDER BY " + statsOrder + "uuid ASC) s) t WHERE uuid = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setLong(1, time);
				ps.setString(2, uuid.toString());
				ResultSet rs = ps.executeQuery();
				Map<StatsParameter, Object> statsValues = Settings.statsParameter.stream()
						.collect(Collectors.toMap(Function.identity(), value -> 0));

				if (rs.next()) {
					for (StatsParameter statsParameter : Settings.statsParameter) {
						if (statsParameter.getType() == int.class
								|| statsParameter.getType() == boolean.class) statsValues.put(statsParameter,
										(int) statsValues.get(statsParameter) + rs.getInt(statsParameter.getName()));
						if (statsParameter.getType() == double.class) statsValues.put(statsParameter,
								(double) statsValues.get(statsParameter) + rs.getDouble(statsParameter.getName()));
					}
					int position = rs.getInt("position");
					consumer.accept(new Stats(uuid, position, statsValues));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			consumer.accept(null);
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	@Override
	public void getStats(int position, long time, Consumer<Stats> consumer, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			String statsColumn = "";
			String statsSum = "";
			String statsOrder = "";
			for (StatsParameter statsParameter : Settings.statsParameter) {
				statsColumn += ", " + statsParameter.getName();
				statsSum += ", SUM(" + statsParameter.getName() + ") AS `" + statsParameter.getName() + "`";
			}

			List<StatsParameter> statsParameters = Lists.newArrayList(Settings.statsParameter);
			statsParameters.sort((s1, s2) -> s1.getSortId() - s2.getSortId());

			for (StatsParameter statsParameter : statsParameters) {
				statsOrder += statsParameter.getName() + " " + (statsParameter.isDescending() ? "DESC, " : "ASC, ");
			}

			String statement = "SELECT position, uuid" + statsColumn + " FROM (SELECT (@pos := @pos+1) position, uuid"
					+ statsColumn + " FROM (SELECT @pos := 0) p, (SELECT uuid" + statsSum + " FROM " + statsTable
					+ " WHERE statstime >= ? GROUP BY uuid ORDER BY " + statsOrder
					+ "uuid ASC) s) t WHERE position = ?";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setLong(1, time);
				ps.setInt(2, position);
				ResultSet rs = ps.executeQuery();
				Map<StatsParameter, Object> statsValues = Settings.statsParameter.stream()
						.collect(Collectors.toMap(Function.identity(), value -> 0));

				if (rs.next()) {
					for (StatsParameter statsParameter : Settings.statsParameter) {
						if (statsParameter.getType() == int.class
								|| statsParameter.getType() == boolean.class) statsValues.put(statsParameter,
										(int) statsValues.get(statsParameter) + rs.getInt(statsParameter.getName()));
						if (statsParameter.getType() == double.class) statsValues.put(statsParameter,
								(double) statsValues.get(statsParameter) + rs.getDouble(statsParameter.getName()));
					}
					UUID uuid = UUID.fromString(rs.getString("uuid"));
					consumer.accept(new Stats(uuid, position, statsValues));
					return;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			consumer.accept(null);
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.eaglefamily.game.database.Database#setStats(java.util.UUID,
	 * de.eaglefamily.game.util.Stats, boolean)
	 */
	@Override
	public void setStats(UUID uuid, RoundStats stats, boolean sync) {
		if (connection == null) return;
		Runnable run = () -> {
			String statsColumn = "";
			for (StatsParameter statsParameter : stats.getStatsValues().keySet()) {
				statsColumn += ", " + statsParameter.getName();
			}
			String questionMarks = "";
			for (int i = 0; i < stats.getStatsValues().size(); i++)
				questionMarks += ", ?";

			String statement = "INSERT INTO " + statsTable + "(uuid, statstime" + statsColumn + ") VALUES (?, ?"
					+ questionMarks + ")";
			try (PreparedStatement ps = connection.prepareStatement(statement)) {
				ps.setString(1, uuid.toString());
				ps.setLong(2, System.currentTimeMillis());
				Iterator<Entry<StatsParameter, Object>> iterator = stats.getStatsValues().entrySet().iterator();
				int i = 3;
				while (iterator.hasNext()) {
					Entry<StatsParameter, Object> entry = iterator.next();
					if (entry.getKey().getType() == int.class) ps.setInt(i, (int) entry.getValue());
					if (entry.getKey().getType() == double.class) ps.setDouble(i, (double) entry.getValue());
					if (entry.getKey().getType() == boolean.class) ps.setBoolean(i, (boolean) entry.getValue());
					i++;
				}
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		};
		if (sync) run.run();
		else TaskManager.runTaskAsync(run);
	}
}
