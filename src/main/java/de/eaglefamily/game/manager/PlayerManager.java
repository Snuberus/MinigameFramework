/*
 * Created by Jan on 24.07.2018 15:43:59
 */
package de.eaglefamily.game.manager;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.eaglefamily.game.Game;
import de.eaglefamily.game.GamePlayer;

/**
 * The Class PlayerManager.
 *
 * @author Jan
 */
public class PlayerManager {

	private List<GamePlayer> gamePlayers = Lists.newArrayList();
	private List<GamePlayer> offlinePlayers = Lists.newArrayList();

	/**
	 * Gets the game players.
	 *
	 * @return the game players
	 */
	public List<GamePlayer> getGamePlayers() {
		return Lists.newArrayList(gamePlayers);
	}

	/**
	 * Gets the offline players.
	 *
	 * @return the offline players
	 */
	public List<GamePlayer> getOfflinePlayers() {
		return Lists.newArrayList(offlinePlayers);
	}

	/**
	 * Adds the game player.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void addGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayers.contains(gamePlayer)) return;
		offlinePlayers.remove(gamePlayer);
		gamePlayers.add(gamePlayer);
	}

	/**
	 * Removes the game player.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void removeGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayers.remove(gamePlayer) && !offlinePlayers.contains(gamePlayer)) offlinePlayers.add(gamePlayer);
	}

	/**
	 * Gets the game player.
	 *
	 * @param player
	 *            the player
	 * @return the game player
	 */
	public GamePlayer getGamePlayer(Player player) {
		return gamePlayers.stream().filter(gP -> gP.getPlayer().equals(player)).findFirst().orElse(null);
	}

	/**
	 * Gets the game player.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the game player
	 */
	public GamePlayer getGamePlayer(UUID uuid) {
		return gamePlayers.stream().filter(gP -> gP.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	/**
	 * Gets the game player.
	 *
	 * @param name
	 *            the name
	 * @return the game player
	 */
	public GamePlayer getGamePlayer(String name) {
		if (name.length() > 16) return getGamePlayer(UUID.fromString(name));
		return gamePlayers.stream().filter(gP -> gP.getPlayer().getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	/**
	 * Gets the offline player.
	 *
	 * @param player
	 *            the player
	 * @return the offline player
	 */
	public GamePlayer getOfflinePlayer(Player player) {
		return offlinePlayers.stream().filter(gP -> gP.getPlayer().equals(player)).findFirst().orElse(null);
	}

	/**
	 * Gets the offline player.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the offline player
	 */
	public GamePlayer getOfflinePlayer(UUID uuid) {
		return offlinePlayers.stream().filter(gP -> gP.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	/**
	 * Gets the offline player.
	 *
	 * @param name
	 *            the name
	 * @return the offline player
	 */
	public GamePlayer getOfflinePlayer(String name) {
		if (name.length() > 16) return getGamePlayer(UUID.fromString(name));
		return offlinePlayers.stream().filter(gP -> gP.getPlayer().getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	/**
	 * Update player data.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void updatePlayerData(GamePlayer gamePlayer) {
		Player player = gamePlayer.getPlayer();
		String skin = "";
		GameProfile profile = gamePlayer.getEntityPlayer().getProfile();
		Iterator<Property> iterator = profile.getProperties().get("textures").iterator();
		if (iterator.hasNext()) {
			Property prop = iterator.next();
			skin = prop.getValue();
		}
		Game.getInstance().getDatabase().updatePlayer(player.getUniqueId(), player.getName(), skin, false);
	}

}
