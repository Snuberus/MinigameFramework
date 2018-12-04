/**
 *Created by Jan on 24.07.2018 15:43:47
 */
package de.eaglefamily.game.manager;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.util.Settings;
import lombok.Getter;

/**
 * The Class WorldManager.
 *
 * @author Jan
 */
public class WorldManager {

	/**
	 * Gets the lobby world.
	 *
	 * @return the lobby world
	 */
	@Getter
	private World lobbyWorld;
	private Location lobbySpawn;
	
	/**
	 * Gets the game world.
	 *
	 * @return the game world
	 */
	@Getter
	private World gameWorld;
	private Location gameSpawn;
	private Location spectatorSpawn;

	/**
	 * Load lobby world.
	 */
	public void loadLobbyWorld() {
		lobbyWorld = Bukkit.getServer()
				.createWorld(new WorldCreator(Game.getInstance().getConfig().getString("lobby.world")));
		lobbyWorld.setAnimalSpawnLimit(0);
		lobbyWorld.setAmbientSpawnLimit(0);
		lobbyWorld.setMonsterSpawnLimit(0);
		lobbyWorld.setWaterAnimalSpawnLimit(0);
		lobbyWorld.setDifficulty(Difficulty.PEACEFUL);
		lobbyWorld.setAutoSave(false);
		lobbyWorld.setSpawnFlags(false, false);
		lobbyWorld.setKeepSpawnInMemory(false);
		lobbyWorld.setGameRuleValue("keepInventory", "true");
		lobbyWorld.setGameRuleValue("doDaylightCycle", "false");
		lobbyWorld.setGameRuleValue("doEntityDrops", "false");
		lobbyWorld.setGameRuleValue("doFireTick", "false");
		lobbyWorld.setGameRuleValue("doMobLoot", "false");
		lobbyWorld.setGameRuleValue("doMobSpawning", "false");
		lobbyWorld.setGameRuleValue("doTileDrops", "false");
		lobbyWorld.setGameRuleValue("mobGriefing", "false");
		lobbyWorld.setGameRuleValue("showDeathMessages", "false");
		lobbyWorld.setPVP(false);
		lobbyWorld.setFullTime(4000);
		lobbyWorld.setThundering(false);
		lobbyWorld.setStorm(false);
		lobbyWorld.setWeatherDuration(Integer.MAX_VALUE);

		String[] loc = Game.getInstance().getConfig().getString("lobby.spawn").split(":");
		lobbySpawn = new Location(lobbyWorld, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]),
				Double.parseDouble(loc[2]), Float.parseFloat(loc[3]), Float.parseFloat(loc[4]));
		lobbyWorld.setSpawnLocation(lobbySpawn.getBlockX(), lobbySpawn.getBlockY(), lobbySpawn.getBlockZ());
	}

	/**
	 * Load game world.
	 */
	public void loadGameWorld() {
		gameWorld = Bukkit.getServer()
				.createWorld(new WorldCreator(Game.getInstance().getMapManager().getConfig().getString("world")));
		gameWorld.setAutoSave(false);
		gameWorld.setKeepSpawnInMemory(false);

		String[] loc = Game.getInstance().getMapManager().getConfig().getString("spectatorSpawn").split(":");
		spectatorSpawn = new Location(gameWorld, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]),
				Double.parseDouble(loc[2]), Float.parseFloat(loc[3]), Float.parseFloat(loc[4]));

		if (Settings.teams && Settings.spawn) {
			Game.getInstance().getGameTeams().forEach(gameTeam -> {
				String[] teamLoc = Game.getInstance().getMapManager().getConfig()
						.getString("teams." + gameTeam.getIndex() + ".spawn").split(":");
				gameTeam.setSpawn(new Location(gameWorld, Double.parseDouble(teamLoc[0]),
						Double.parseDouble(teamLoc[1]), Double.parseDouble(teamLoc[2]), Float.parseFloat(teamLoc[3]),
						Float.parseFloat(teamLoc[4])));
			});
		}
		if (!Settings.teams && Settings.spawn) {
			loc = Game.getInstance().getMapManager().getConfig().getString("spawn").split(":");
			gameSpawn = new Location(gameWorld, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]),
					Double.parseDouble(loc[2]), Float.parseFloat(loc[3]), Float.parseFloat(loc[4]));
		}
	}

	/**
	 * Gets the lobby spawn.
	 *
	 * @return the lobby spawn
	 */
	public Location getLobbySpawn() {
		return lobbySpawn.clone();
	}

	/**
	 * Gets the spectator spawn.
	 *
	 * @return the spectator spawn
	 */
	public Location getSpectatorSpawn() {
		return spectatorSpawn.clone();
	}

	/**
	 * Gets the game spawn.
	 *
	 * @return the game spawn
	 */
	public Location getGameSpawn() {
		if (gameSpawn == null) return null;
		return gameSpawn.clone();
	}
}
