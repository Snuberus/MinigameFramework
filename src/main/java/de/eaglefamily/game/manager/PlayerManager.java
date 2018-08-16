/**
 * Created by _BlackEagle_ on 24.07.2018 15:43:59
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
 * @author _BlackEagle_
 */
public class PlayerManager {

	private List<GamePlayer> gamePlayers = Lists.newArrayList();
	private List<GamePlayer> offlinePlayers = Lists.newArrayList();

	public List<GamePlayer> getGamePlayers() {
		return Lists.newArrayList(gamePlayers);
	}

	public List<GamePlayer> getOfflinePlayers() {
		return Lists.newArrayList(offlinePlayers);
	}

	public void addGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayers.contains(gamePlayer)) return;
		offlinePlayers.remove(gamePlayer);
		gamePlayers.add(gamePlayer);
	}

	public void removeGamePlayer(GamePlayer gamePlayer) {
		if (gamePlayers.remove(gamePlayer) && !offlinePlayers.contains(gamePlayer)) offlinePlayers.add(gamePlayer);
	}

	public GamePlayer getGamePlayer(Player player) {
		return gamePlayers.stream().filter(gP -> gP.getPlayer().equals(player)).findFirst().orElse(null);
	}

	public GamePlayer getGamePlayer(UUID uuid) {
		return gamePlayers.stream().filter(gP -> gP.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	public GamePlayer getGamePlayer(String name) {
		if (name.length() > 16) return getGamePlayer(UUID.fromString(name));
		return gamePlayers.stream().filter(gP -> gP.getPlayer().getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

	public GamePlayer getOfflinePlayer(Player player) {
		return offlinePlayers.stream().filter(gP -> gP.getPlayer().equals(player)).findFirst().orElse(null);
	}

	public GamePlayer getOfflinePlayer(UUID uuid) {
		return offlinePlayers.stream().filter(gP -> gP.getUuid().equals(uuid)).findFirst().orElse(null);
	}

	public GamePlayer getOfflinePlayer(String name) {
		if (name.length() > 16) return getGamePlayer(UUID.fromString(name));
		return offlinePlayers.stream().filter(gP -> gP.getPlayer().getName().equalsIgnoreCase(name)).findFirst()
				.orElse(null);
	}

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
