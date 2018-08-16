/**
 * Created by _BlackEagle_ on 10.08.2018 18:38:24
 */
package de.eaglefamily.game.manager;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.eaglefamily.bukkitlibrary.language.Translation;
import de.eaglefamily.bukkitlibrary.util.Hologram;
import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.RoundStats;
import de.eaglefamily.game.util.Settings;
import de.eaglefamily.game.util.Stats;
import de.eaglefamily.game.util.StatsParameter;
import de.eaglefamily.game.util.StatsReplaces;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;

/**
 * @author _BlackEagle_
 */
public class StatsManager {

	private final Map<GamePlayer, RoundStats> roundStats = Maps.newHashMap();
	private final Map<UUID, Stats> stats = Maps.newHashMap();

	public StatsManager() {
		for (int i = 1; i < 11; i++) {
			final int position = i;
			getStats(i, 30, stats -> TaskManager.runTask(() -> statsHead(position, stats, "month")));
			getStats(i, 30, stats -> TaskManager.runTask(() -> statsHead(position, stats, "all")));
		}
	}

	public void saveRoundStats() {
		roundStats.entrySet().forEach(
				entry -> Game.getInstance().getDatabase().setStats(entry.getKey().getUuid(), entry.getValue(), true));
	}

	public RoundStats getRoundStats(GamePlayer gamePlayer) {
		if (!roundStats.containsKey(gamePlayer)) roundStats.put(gamePlayer, RoundStats.getEmpty(gamePlayer));
		return roundStats.get(gamePlayer);
	}

	public void getStats(UUID uuid, long time, Consumer<Stats> consumer) {
		if (!stats.containsKey(uuid)) {
			Game.getInstance().getDatabase().getStats(uuid, time, stats -> {
				if (stats == null) {
					consumer.accept(null);
					return;
				}
				this.stats.put(uuid, stats);
				consumer.accept(
						unitedStats(stats, roundStats.get(Game.getInstance().getPlayerManager().getGamePlayer(uuid))));
			}, false);
			return;
		}
		consumer.accept(unitedStats(stats.get(uuid),
				roundStats.get(Game.getInstance().getPlayerManager().getGamePlayer(uuid))));
	}

	public void getStats(UUID uuid, int days, Consumer<Stats> consumer) {
		if (days == 0) getStats(uuid, 0l, consumer);
		else {
			long time = System.currentTimeMillis() - days * 86400000l;
			getStats(uuid, time, consumer);
		}
	}

	public void getStats(UUID uuid, Consumer<Stats> consumer) {
		getStats(uuid, 0, consumer);
	}

	public void getStats(int position, long time, Consumer<Stats> consumer) {
		if (!stats.values().stream().filter(stats -> stats.getPosition() == position).findFirst().isPresent()) {
			Game.getInstance().getDatabase().getStats(position, time, stats -> {
				if (stats == null) {
					consumer.accept(null);
					return;
				}
				this.stats.put(stats.getUuid(), stats);
				consumer.accept(unitedStats(stats,
						roundStats.get(Game.getInstance().getPlayerManager().getGamePlayer(stats.getUuid()))));
			}, false);
			return;
		}

		Stats databaseStats = stats.values().stream().filter(stats -> stats.getPosition() == position).findFirst()
				.orElse(null);
		consumer.accept(unitedStats(databaseStats,
				roundStats.get(Game.getInstance().getPlayerManager().getGamePlayer(databaseStats.getUuid()))));
	}

	public void getStats(int position, int days, Consumer<Stats> consumer) {
		if (days == 0) getStats(position, 0l, consumer);
		else {
			long time = System.currentTimeMillis() - days * 86400000l;
			getStats(position, time, consumer);
		}
	}

	public void getStats(int position, Consumer<Stats> consumer) {
		getStats(position, 0l, consumer);
	}

	private Stats unitedStats(Stats databaseStats, RoundStats currentStats) {
		if (databaseStats == null) return null;
		if (currentStats == null) return databaseStats;
		Map<StatsParameter, Object> statsValues = databaseStats.getStatsValues();
		for (Entry<StatsParameter, Object> entry : currentStats.getStatsValues().entrySet()) {
			if (!statsValues.containsKey(entry.getKey())) statsValues.put(entry.getKey(), 0);
			if (entry.getKey().getType() == int.class) statsValues.put(entry.getKey(),
					(int) statsValues.get(entry.getKey()) + (int) entry.getValue());
			if (entry.getKey().getType() == double.class) statsValues.put(entry.getKey(),
					(double) statsValues.get(entry.getKey()) + (double) entry.getValue());
			if (entry.getKey().getType() == boolean.class && (boolean) entry.getValue()) statsValues.put(entry.getKey(),
					(int) statsValues.get(entry.getKey()) + 1);
		}
		return new Stats(databaseStats.getUuid(), databaseStats.getPosition(), statsValues);
	}

	public void showStatsHologram(GamePlayer gamePlayer) {
		getStats(gamePlayer.getUuid(), 30, stats -> hologram(gamePlayer, stats, "month"));
		getStats(gamePlayer.getUuid(), stats -> hologram(gamePlayer, stats, "all"));
	}

	private void hologram(GamePlayer gamePlayer, Stats preStats, String key) {
		final Stats stats = preStats != null ? preStats : Stats.getEmpty(gamePlayer.getUuid());
		String loc[] = Game.getInstance().getConfig().getString("lobby.stats." + key + ".hologram").split(":");
		Location location = new Location(Game.getInstance().getWorldManager().getLobbyWorld(),
				Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));

		Settings.statsReplaces.accept(new StatsReplaces(stats.getUuid(), stats) {
			@Override
			public void accept(List<Object> replaces) {
				new Hologram(gamePlayer.getPlayer(), location, 0.25, "hologram.stats." + key,
						getStatsReplaces(gamePlayer, stats, gamePlayer.getName(), replaces));
			}
		});
	}

	public void showStatsSign(GamePlayer gamePlayer) {
		descriptionSign(gamePlayer, "month");
		descriptionSign(gamePlayer, "all");
		for (int i = 1; i < 11; i++) {
			final int position = i;
			getStats(i, 30, stats -> statsSign(gamePlayer, position, stats, "month"));
			getStats(i, stats -> statsSign(gamePlayer, position, stats, "all"));
		}
	}

	private void descriptionSign(GamePlayer gamePlayer, String key) {
		String loc[] = Game.getInstance().getConfig().getString("lobby.stats." + key + ".topten.description")
				.split(":");
		Location location = new Location(Game.getInstance().getWorldManager().getLobbyWorld(),
				Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));
		gamePlayer.getPlayer().sendSignChange(location,
				gamePlayer.translateUTF("sign.stats." + key + ".description").split("\n"));
	}

	private void statsSign(GamePlayer gamePlayer, int position, Stats stats, String key) {
		String loc[] = Game.getInstance().getConfig().getString("lobby.stats." + key + ".topten." + position + ".sign")
				.split(":");
		Location location = new Location(Game.getInstance().getWorldManager().getLobbyWorld(),
				Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));

		if (stats == null) {
			gamePlayer.getPlayer().sendSignChange(location,
					gamePlayer.translateUTF("sign.stats." + key + ".notfound", "position", position).split("\n"));
			return;
		}

		Game.getInstance().getDatabase().getName(stats.getUuid(), name -> {
			Settings.statsReplaces.accept(new StatsReplaces(stats.getUuid(), stats) {
				@Override
				public void accept(List<Object> replaces) {
					gamePlayer.getPlayer().sendSignChange(location,
							gamePlayer.translateUTF("sign.stats." + key + ".show",
									getStatsReplaces(gamePlayer, stats, name, replaces)).split("\n"));
				}
			});

		}, false);
	}

	private void statsHead(int position, Stats stats, String key) {
		if (stats == null) return;
		String loc[] = Game.getInstance().getConfig().getString("lobby.stats." + key + ".topten." + position + ".head")
				.split(":");
		Location location = new Location(Game.getInstance().getWorldManager().getLobbyWorld(),
				Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));

		Game.getInstance().getDatabase().getSkin(stats.getUuid(), skin -> {
			Block head = location.getBlock();
			GameProfile skinProfile = new GameProfile(stats.getUuid(), null);
			skinProfile.getProperties().put("textures", new Property("textures", skin));
			BlockPosition pos = new BlockPosition(head.getX(), head.getY(), head.getZ());
			TileEntitySkull tile = (TileEntitySkull) ((CraftWorld) head.getWorld()).getHandle().getTileEntity(pos);
			tile.setGameProfile(skinProfile);
			head.getState().update();
		}, true);
	}

	public Object[] getStatsReplaces(CommandSender sender, Stats stats, String name, List<Object> statsReplaces) {
		statsReplaces.add("player");
		statsReplaces.add(name);
		statsReplaces.add("position");
		statsReplaces.add(stats.getPosition());
		for (StatsParameter statsParameter : Settings.statsParameter) {
			statsReplaces.add(statsParameter.getName());
			statsReplaces.add(stats.getStats(statsParameter));
		}
		return statsReplaces.toArray();
	}

	public Object[] getStatsReplaces(Player player, Stats stats, String name, List<Object> statsReplaces) {
		return getStatsReplaces((CommandSender) player, stats, name, statsReplaces);
	}

	public Object[] getStatsReplaces(GamePlayer gamePlayer, Stats stats, String name, List<Object> statsReplaces) {
		return getStatsReplaces(gamePlayer.getPlayer(), stats, name, statsReplaces);
	}

	public Object[] getRoundStatsReplaces(CommandSender sender, RoundStats stats, String name,
			List<Object> statsReplaces) {
		statsReplaces.add("player");
		statsReplaces.add(name);
		statsReplaces.add("position");
		statsReplaces.add(stats.getPosition());
		for (StatsParameter statsParameter : Settings.statsParameter) {
			statsReplaces.add(statsParameter.getName());
			if (statsParameter.getType() == boolean.class) statsReplaces.add(Translation.translateUTF(sender,
					"cmd.stats." + ((boolean) stats.getStats(statsParameter) ? "true" : "false")));
			else statsReplaces.add(stats.getStats(statsParameter));
		}
		return statsReplaces.toArray();
	}

	public Object[] getRoundStatsReplaces(Player player, RoundStats stats, String name, List<Object> statsReplaces) {
		return getRoundStatsReplaces((CommandSender) player, stats, name, statsReplaces);
	}

	public Object[] getRoundStatsReplaces(GamePlayer gamePlayer, RoundStats stats, String name,
			List<Object> statsReplaces) {
		return getRoundStatsReplaces(gamePlayer.getPlayer(), stats, name, statsReplaces);
	}
}
