/*
 * Created by Jan on 24.07.2018 15:43:38
 */
package de.eaglefamily.game.manager;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;
import de.eaglefamily.game.util.Inventories;
import de.eaglefamily.game.util.Items;
import de.eaglefamily.game.util.Settings;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Class MapManager.
 *
 * @author Jan
 */
public class MapManager {

	private File worldFolder;
	
	/**
	 * Checks if is imported.
	 *
	 * @return true, if is imported
	 */
	@Getter
	private boolean imported;
	
	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	@Getter
	private Configuration config;
	
	/**
	 * Gets the map name.
	 *
	 * @return the map name
	 */
	@Getter
	private String mapName;
	
	/**
	 * Gets the map builder.
	 *
	 * @return the map builder
	 */
	@Getter
	private String mapBuilder;
	
	/**
	 * Gets the material.
	 *
	 * @return the material
	 */
	@Getter
	private Material material;

	/**
	 * Gets the voteable maps.
	 *
	 * @return the voteable maps
	 */
	@Getter
	private List<VoteableMap> voteableMaps = Lists.newArrayList();

	/**
	 * Select votable maps.
	 */
	public void selectVotableMaps() {
		for (int i = 0; i < Settings.voteableMaps; i++) {
			String map = selectRandomVotableMap();
			if (map == null) break;
			VoteableMap voteableMap = new VoteableMap(map,
					Game.getInstance().getConfig().getString("maps." + Settings.getSize() + "." + map + ".builder"),
					Material.valueOf(Game.getInstance().getConfig()
							.getString("maps." + Settings.getSize() + "." + map + ".material")));
			voteableMaps.add(voteableMap);
		}
	}

	private String selectRandomVotableMap() {
		double entireProbability = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize())
				.getKeys(false).stream()
				.filter(map -> !voteableMaps.stream().filter(voteMap -> voteMap.mapName.equals(map)).findFirst()
						.isPresent())
				.mapToDouble(mapper -> Game.getInstance().getConfig()
						.getDouble("maps." + Settings.getSize() + "." + mapper + ".probability"))
				.sum();
		double randomNumber = entireProbability * Math.random();
		double i = 0;
		Iterator<String> iterator = Game.getInstance().getConfig().getConfigurationSection("maps." + Settings.getSize())
				.getKeys(false).stream().filter(map -> !voteableMaps.stream()
						.filter(voteMap -> voteMap.mapName.equals(map)).findFirst().isPresent())
				.iterator();
		while (iterator.hasNext()) {
			String map = iterator.next();
			i += Game.getInstance().getConfig().getDouble("maps." + Settings.getSize() + "." + map + ".probability");
			if (randomNumber <= i) {
				return map;
			}
		}
		return null;
	}

	private void selectVotedMap() {
		VoteableMap map = voteableMaps.stream().sorted((m1, m2) -> m2.getVotes() - m1.getVotes()).findFirst()
				.orElse(voteableMaps.get(0));
		setMap(map.getMapName());
	}

	/**
	 * Import map.
	 */
	public void importMap() {
		if (imported) return;
		if (mapName == null) {
			removeVoteItem();
			selectVotedMap();
		}
		imported = true;

		TaskManager.runTaskAsync(() -> {
			worldFolder = new File(Bukkit.getServer().getWorldContainer(), mapName);
			if (Settings.globalMaps) {
				File globalMapsFolder = new File(Settings.mapsPath);
				File mapFolder = new File(new File(globalMapsFolder, Settings.getSize()), mapName);
				try {
					FileUtils.copyDirectory(mapFolder, worldFolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			File configFile = new File(worldFolder, "config.yml");
			config = YamlConfiguration.loadConfiguration(configFile);
			TaskManager.runTask(() -> Game.getInstance().getWorldManager().loadGameWorld());
		});
	}

	/**
	 * Delete map.
	 */
	public void deleteMap() {
		if (!imported) return;
		Bukkit.unloadWorld(Game.getInstance().getWorldManager().getGameWorld(), false);
		if (Settings.globalMaps) {
			try {
				FileUtils.deleteDirectory(worldFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileUtils.deleteDirectory(new File(worldFolder, "playerdata"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Delete lobby player data.
	 */
	public void deleteLobbyPlayerData() {
		File lobbyWorld = new File(Bukkit.getServer().getWorldContainer(),
				Game.getInstance().getConfig().getString("lobby.world"));
		try {
			FileUtils.deleteDirectory(new File(lobbyWorld, "playerdata"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the map.
	 *
	 * @param mapName
	 *            the new map
	 */
	public void setMap(String mapName) {
		if (imported) return;
		if (this.mapName == null) removeVoteItem();
		this.mapName = mapName;
		mapBuilder = Game.getInstance().getConfig()
				.getString("maps." + Settings.getSize() + "." + mapName + ".builder");
		Game.getInstance().getGamePlayers().forEach(GamePlayer::updateLobbySidebar);
	}

	/**
	 * Gets the voted map.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the voted map
	 */
	public VoteableMap getVotedMap(GamePlayer gamePlayer) {
		return voteableMaps.stream().filter(map -> map.playerVotes.contains(gamePlayer)).findFirst().orElse(null);
	}

	private void removeVoteItem() {
		Game.getInstance().getGamePlayers().forEach(gamePlayer -> {
			if (!Settings.teams) gamePlayer.removeLobbyContent(0);
			else gamePlayer.removeLobbyContent(1);
			gamePlayer.getPlayer().getInventory().remove(Items.vote(gamePlayer));
			if (gamePlayer.getPlayer().getOpenInventory().getTitle()
					.equals(Inventories.vote(gamePlayer).getTitle())) gamePlayer.getPlayer().closeInventory();
		});
	}

	/**
	 * Instantiates a new voteable map.
	 *
	 * @param mapName
	 *            the map name
	 * @param mapBuilder
	 *            the map builder
	 * @param material
	 *            the material
	 */
	@AllArgsConstructor
	public class VoteableMap {
		
		/**
		 * Gets the map name.
		 *
		 * @return the map name
		 */
		@Getter
		private final String mapName;
		
		/**
		 * Gets the map builder.
		 *
		 * @return the map builder
		 */
		@Getter
		private final String mapBuilder;
		
		/**
		 * Gets the material.
		 *
		 * @return the material
		 */
		@Getter
		private final Material material;
		
		/**
		 * Gets the player votes.
		 *
		 * @return the player votes
		 */
		@Getter
		private final List<GamePlayer> playerVotes = Lists.newArrayList();

		/**
		 * Vote.
		 *
		 * @param gamePlayer
		 *            the game player
		 */
		public void vote(GamePlayer gamePlayer) {
			if (playerVotes.contains(gamePlayer)) return;
			playerVotes.add(gamePlayer);
			voteableMaps.stream().filter(map -> map != this && map.playerVotes.contains(gamePlayer))
					.forEach(map -> map.unvote(gamePlayer));
		}

		/**
		 * Unvote.
		 *
		 * @param gamePlayer
		 *            the game player
		 */
		public void unvote(GamePlayer gamePlayer) {
			playerVotes.remove(gamePlayer);
		}

		/**
		 * Gets the votes.
		 *
		 * @return the votes
		 */
		public int getVotes() {
			return playerVotes.size();
		}
	}
}
