/*
 * Created by Jan on 24.07.2018 11:19:43
 */
package de.eaglefamily.game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.eaglefamily.bukkitlibrary.language.Message;
import de.eaglefamily.bukkitlibrary.language.Translation;
import de.eaglefamily.bukkitlibrary.packet.Packets;
import de.eaglefamily.bukkitlibrary.player.Actionbar;
import de.eaglefamily.bukkitlibrary.player.Name;
import de.eaglefamily.bukkitlibrary.player.Title;
import de.eaglefamily.bukkitlibrary.scoreboard.Content;
import de.eaglefamily.bukkitlibrary.scoreboard.Sidebar;
import de.eaglefamily.bukkitlibrary.util.TaskManager;
import de.eaglefamily.game.event.CheckGroupEvent;
import de.eaglefamily.game.event.CheckListNameEvent;
import de.eaglefamily.game.event.CheckNameEvent;
import de.eaglefamily.game.event.TeamJoinEvent;
import de.eaglefamily.game.event.TeamLeaveEvent;
import de.eaglefamily.game.util.GameState;
import de.eaglefamily.game.util.Inventories;
import de.eaglefamily.game.util.Items;
import de.eaglefamily.game.util.ListName;
import de.eaglefamily.game.util.Settings;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;

/**
 * The Class GamePlayer.
 *
 * @author Jan
 */
public class GamePlayer {

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	@Getter
	
	/**
	 * Sets the player.
	 *
	 * @param player
	 *            the new player
	 */
	@Setter
	private Player player;
	
	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	@Getter
	private final UUID uuid;
	
	/**
	 * Gets the game team.
	 *
	 * @return the game team
	 */
	@Getter
	private GameTeam gameTeam;
	
	/**
	 * Checks if is spectator.
	 *
	 * @return true, if is spectator
	 */
	@Getter
	private boolean spectator;
	
	/**
	 * Checks if is game player.
	 *
	 * @return true, if is game player
	 */
	@Getter
	
	/**
	 * Sets the game player.
	 *
	 * @param gamePlayer
	 *            the new game player
	 */
	@Setter
	private boolean gamePlayer;
	
	/**
	 * Sets the spawn.
	 *
	 * @param spawn
	 *            the new spawn
	 */
	@Setter
	private Location spawn;

	/**
	 * Gets the spectating.
	 *
	 * @return the spectating
	 */
	@Getter
	private GamePlayer spectating;
	
	/**
	 * Checks if is camera reseted.
	 *
	 * @return true, if is camera reseted
	 */
	@Getter
	
	/**
	 * Sets the camera reseted.
	 *
	 * @param cameraReseted
	 *            the new camera reseted
	 */
	@Setter
	private boolean cameraReseted;
	
	/**
	 * Gets the compass target.
	 *
	 * @return the compass target
	 */
	@Getter
	private GamePlayer compassTarget;

	/**
	 * Gets the lobby contents.
	 *
	 * @return the lobby contents
	 */
	@Getter
	private final Map<Integer, ItemStack> lobbyContents = Maps.newHashMap();
	
	/**
	 * Gets the spectator contents.
	 *
	 * @return the spectator contents
	 */
	@Getter
	private final Map<Integer, ItemStack> spectatorContents = Maps.newHashMap();
	
	/**
	 * Gets the game contents.
	 *
	 * @return the game contents
	 */
	@Getter
	private final Map<Integer, ItemStack> gameContents = Maps.newHashMap();

	/**
	 * Instantiates a new game player.
	 *
	 * @param player
	 *            the player
	 */
	public GamePlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		setDefaultLobbyContents();
		setDefaultSpectatorContents();
	}

	/**
	 * Gets the craft player.
	 *
	 * @return the craft player
	 */
	public CraftPlayer getCraftPlayer() {
		return (CraftPlayer) player;
	}

	/**
	 * Gets the entity player.
	 *
	 * @return the entity player
	 */
	public EntityPlayer getEntityPlayer() {
		return getCraftPlayer().getHandle();
	}

	/**
	 * Send.
	 *
	 * @param key
	 *            the key
	 * @param replaces
	 *            the replaces
	 */
	public void send(final String key, final Object... replaces) {
		Message.send(player, key, replaces);
	}

	/**
	 * Translate.
	 *
	 * @param key
	 *            the key
	 * @param replaces
	 *            the replaces
	 * @return the base component[]
	 */
	public BaseComponent[] translate(final String key, final Object... replaces) {
		return Translation.translate(player, key, replaces);
	}

	/**
	 * Translate UTF.
	 *
	 * @param key
	 *            the key
	 * @param replaces
	 *            the replaces
	 * @return the string
	 */
	public String translateUTF(final String key, final Object... replaces) {
		return Translation.translateUTF(player, key, replaces);
	}

	/**
	 * Send packet.
	 *
	 * @param packet
	 *            the packet
	 */
	public void sendPacket(final Packet<?> packet) {
		Packets.send(player, packet);
	}

	/**
	 * Send packets.
	 *
	 * @param packets
	 *            the packets
	 */
	public void sendPackets(final Packet<?>... packets) {
		Packets.send(player, packets);
	}

	/**
	 * Gets the spawn.
	 *
	 * @return the spawn
	 */
	public Location getSpawn() {
		switch (GameState.getStatus()) {
		case LOBBY:
			return Game.getInstance().getWorldManager().getLobbySpawn();
		case STARTING:
		case INGAME:
			if (spectator) return Game.getInstance().getWorldManager().getSpectatorSpawn();
			if (spawn != null) return spawn.clone();
			if (Game.getInstance().getWorldManager().getGameSpawn() != null) return Game.getInstance().getWorldManager()
					.getGameSpawn();
			return gameTeam.getSpawn();
		case ENDING:
		case END:
			return Game.getInstance().getWorldManager().getSpectatorSpawn();
		default:
			return Game.getInstance().getWorldManager().getLobbySpawn();
		}
	}

	/**
	 * Teleport to spawn.
	 */
	public void teleportToSpawn() {
		if (player.isInsideVehicle()) player.leaveVehicle();
		player.teleport(getSpawn());
	}

	/**
	 * Sets the game team.
	 *
	 * @param gameTeam
	 *            the new game team
	 */
	public void setGameTeam(GameTeam gameTeam) {
		if (this.gameTeam == gameTeam) return;
		if (this.gameTeam != null) {
			this.gameTeam.removePlayer(this);
			Bukkit.getPluginManager().callEvent(new TeamLeaveEvent(this, this.gameTeam));
		}
		this.gameTeam = gameTeam;
		if (gameTeam != null) {
			gameTeam.addPlayer(this);
			Bukkit.getPluginManager().callEvent(new TeamJoinEvent(this, gameTeam));
		}
		updateTabListFor(this);
		updateTabListForOthers();
	}

	/**
	 * Sets the spectator.
	 */
	public void setSpectator() {
		spectator = true;
		getSpectators().forEach(gP -> gP.removeCamera());
		reset();
		Game.getInstance().getGamePlayers().forEach(gP -> gP.getPlayer().hidePlayer(player));
		player.spigot().setCollidesWithEntities(false);
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setCompassTarget(Game.getInstance().getWorldManager().getSpectatorSpawn());
		Actionbar.sendPermanentTranslation(player, "actionbar.spectator");
		setSpectatorContents();
		TaskManager.runTask(() -> updateTabListForOthers());

		if (gamePlayer) Game.getInstance().getGamePlayers().stream()
				.filter(gP -> gP.isSpectator()
						&& gP.getPlayer().getOpenInventory().getTitle().equals(Inventories.compass(gP).getTitle()))
				.forEach(gP -> gP.getPlayer().openInventory(Inventories.compass(gP)));
	}

	/**
	 * Reset.
	 */
	public void reset() {
		player.setLevel(0);
		player.setExp(0);
		player.resetMaxHealth();
		player.setHealth(20);
		player.setExhaustion(0);
		player.setFoodLevel(20);
		player.setSaturation(0.0f);
		player.setFallDistance(0);
		player.setVelocity(new Vector());
		player.setFireTicks(0);
		player.leaveVehicle();
		player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
		player.getInventory().setHeldItemSlot(0);
		player.getInventory().clear();
		player.getEnderChest().clear();
		player.getInventory().setArmorContents(null);
		Title.reset(player);
	}

	/**
	 * Gets the name.
	 *
	 * @param player
	 *            the player
	 * @return the name
	 */
	public String getName(Player player) {
		CheckNameEvent event = new CheckNameEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getName();
	}

	/**
	 * Gets the name.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the name
	 */
	public String getName(GamePlayer gamePlayer) {
		return getName(gamePlayer.getPlayer());
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return getName(player);
	}

	/**
	 * Gets the list name.
	 *
	 * @param player
	 *            the player
	 * @return the list name
	 */
	public ListName getListName(Player player) {
		CheckListNameEvent event = new CheckListNameEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getListName();
	}

	/**
	 * Gets the list name.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the list name
	 */
	public ListName getListName(GamePlayer gamePlayer) {
		return getListName(gamePlayer.getPlayer());
	}

	/**
	 * Gets the list name.
	 *
	 * @return the list name
	 */
	public ListName getListName() {
		return getListName(player);
	}

	/**
	 * Update tab list.
	 */
	public void updateTabList() {
		Game.getInstance().getGamePlayers().forEach(gP -> gP.updateTabListFor(this));
	}

	/**
	 * Update tab list for others.
	 */
	public void updateTabListForOthers() {
		Game.getInstance().getGamePlayers().forEach(gP -> updateTabListFor(gP));
	}

	/**
	 * Update tab list for.
	 *
	 * @param gP
	 *            the g P
	 */
	public void updateTabListFor(GamePlayer gP) {
		switch (GameState.getStatus()) {
		case LOBBY:
			if (gameTeam == null) {
				ListName listName = getListName(gP);
				Name.set(gP.getPlayer(), getName(gP), listName.getPrefix(), listName.getSuffix(), listName.getSortId(),
						false);
			} else Name.set(gP.getPlayer(), getName(gP), gameTeam.getPrefix(gP), gameTeam.getSuffix(gP),
					gameTeam.getSortId(gP), false);
			break;
		case STARTING:
		case INGAME:
			if (spectator && gP != this) Name.remove(gP.getPlayer(), getName(gP), false);
			else if (spectator && gP == this) Name.set(player, getName(), translateUTF("game.spectatorlist.prefix"),
					translateUTF("game.spectatorlist.suffix"), translateUTF("game.spectatorlist.sortId"), false);
			else if (!spectator && gameTeam == null) {
				ListName listName = getListName(gP);
				Name.set(gP.getPlayer(), getName(gP), listName.getPrefix(), listName.getSuffix(), listName.getSortId(),
						false);
			} else if (!spectator && gameTeam != null) Name.set(gP.getPlayer(), getName(gP), gameTeam.getPrefix(gP),
					gameTeam.getSuffix(gP), gameTeam.getSortId(gP), false);
			break;
		case ENDING:
		case END:
			ListName listName = getListName(gP);
			Name.set(gP.getPlayer(), getName(gP), listName.getPrefix(), listName.getSuffix(), listName.getSortId(),
					false);
			break;
		default:
			break;
		}
	}

	/**
	 * Gets the group.
	 *
	 * @param player
	 *            the player
	 * @return the group
	 */
	public String getGroup(Player player) {
		CheckGroupEvent event = new CheckGroupEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getGroup();
	}

	/**
	 * Gets the group.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the group
	 */
	public String getGroup(GamePlayer gamePlayer) {
		return getGroup(gamePlayer.getPlayer());
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public String getGroup() {
		return getGroup(player);
	}

	/**
	 * Gets the replaces.
	 *
	 * @param player
	 *            the player
	 * @return the replaces
	 */
	public Object[] getReplaces(Player player) {
		List<Object> replaces = Lists.newArrayList();
		replaces.add("player");
		replaces.add(getName(player) != null ? getName(player) : this.player.getName());
		replaces.add("group");
		replaces.add(getGroup(player) != null ? getGroup(player) : "");
		if (Settings.teams && gameTeam != null) replaces.addAll(Arrays.asList(gameTeam.getReplaces(this)));
		return replaces.toArray();
	}

	/**
	 * Gets the replaces.
	 *
	 * @param gamePlayer
	 *            the game player
	 * @return the replaces
	 */
	public Object[] getReplaces(GamePlayer gamePlayer) {
		return getReplaces(gamePlayer.getPlayer());
	}

	/**
	 * Gets the replaces.
	 *
	 * @return the replaces
	 */
	public Object[] getReplaces() {
		return getReplaces(player);
	}

	/**
	 * Sets the camera.
	 *
	 * @param gamePlayer
	 *            the new camera
	 */
	public void setCamera(GamePlayer gamePlayer) {
		Player target = gamePlayer.getPlayer();
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		player.setLevel(0);
		player.setFlying(false);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
		player.teleport(target);
		setCompassTarget(gamePlayer);
		Actionbar.sendPermanentTranslation(player, "actionbar.spectating", gamePlayer.getReplaces(this));
		Title.sendTranslation(player, "title.spectating", "title.spectatingsub", 8, 12, 8,
				gamePlayer.getReplaces(this));
		updateCamera(gamePlayer);
		spectating = gamePlayer;
		cameraReseted = false;
	}

	/**
	 * Removes the camera.
	 */
	public void removeCamera() {
		resetCamera();
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		setSpectatorContents();
		Actionbar.sendPermanentTranslation(player, "actionbar.spectator");
		spectating = null;
		cameraReseted = false;
	}

	/**
	 * Update camera.
	 *
	 * @param gamePlayer
	 *            the game player
	 */
	public void updateCamera(GamePlayer gamePlayer) {
		PacketPlayOutCamera camera = new PacketPlayOutCamera(gamePlayer.getEntityPlayer());
		sendPacket(camera);
		player.teleport(gamePlayer.getPlayer());
	}

	/**
	 * Reset camera.
	 */
	public void resetCamera() {
		PacketPlayOutCamera packet = new PacketPlayOutCamera(getEntityPlayer());
		sendPacket(packet);
		player.teleport(spectating.getPlayer());
	}

	/**
	 * Gets the spectators.
	 *
	 * @return the spectators
	 */
	public List<GamePlayer> getSpectators() {
		return Game.getInstance().getGamePlayers().stream().filter(gP -> gP.getSpectating() == this)
				.collect(Collectors.toList());
	}

	/**
	 * Sets the compass target.
	 *
	 * @param gamePlayer
	 *            the new compass target
	 */
	public void setCompassTarget(GamePlayer gamePlayer) {
		this.compassTarget = gamePlayer;
		player.setCompassTarget(gamePlayer.getPlayer().getLocation());
	}

	private void setDefaultLobbyContents() {
		if (Settings.teams) addLobbyContent(0, Items.teamSelector(this));
		addLobbyContent(8, Items.leave(this));

		if (!Settings.teams
				&& Game.getInstance().getMapManager().getMapName() == null) addLobbyContent(0, Items.vote(this));
		if (Settings.teams
				&& Game.getInstance().getMapManager().getMapName() == null) addLobbyContent(1, Items.vote(this));
	}

	private void setDefaultSpectatorContents() {
		addSpectatorContent(0, Items.compass(this));
		addSpectatorContent(8, Items.leave(this));
	}

	/**
	 * Sets the lobby contents.
	 */
	public void setLobbyContents() {
		lobbyContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	/**
	 * Sets the spectator contents.
	 */
	public void setSpectatorContents() {
		spectatorContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	/**
	 * Sets the game contents.
	 */
	public void setGameContents() {
		gameContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	/**
	 * Adds the lobby content.
	 *
	 * @param slot
	 *            the slot
	 * @param itemStack
	 *            the item stack
	 */
	public void addLobbyContent(int slot, ItemStack itemStack) {
		lobbyContents.put(slot, itemStack);
	}

	/**
	 * Removes the lobby content.
	 *
	 * @param slot
	 *            the slot
	 */
	public void removeLobbyContent(int slot) {
		lobbyContents.remove(slot);
	}

	/**
	 * Adds the spectator content.
	 *
	 * @param slot
	 *            the slot
	 * @param itemStack
	 *            the item stack
	 */
	public void addSpectatorContent(int slot, ItemStack itemStack) {
		spectatorContents.put(slot, itemStack);
	}

	/**
	 * Removes the spectator content.
	 *
	 * @param slot
	 *            the slot
	 */
	public void removeSpectatorContent(int slot) {
		spectatorContents.remove(slot);
	}

	/**
	 * Adds the game content.
	 *
	 * @param slot
	 *            the slot
	 * @param itemStack
	 *            the item stack
	 */
	public void addGameContent(int slot, ItemStack itemStack) {
		gameContents.put(slot, itemStack);
	}

	/**
	 * Removes the game content.
	 *
	 * @param slot
	 *            the slot
	 */
	public void removeGameContent(int slot) {
		gameContents.remove(slot);
	}

	/**
	 * Update lobby sidebar.
	 */
	public void updateLobbySidebar() {
		if (GameState.getStatus() == GameState.STARTING) {
			Sidebar.set(player, null, null);
			return;
		}
		if (GameState.getStatus() != GameState.LOBBY) return;
		if (Game.getInstance().getLobbyCountdown().isRunning()) startingSidebar();
		else waitingSidebar();
	}

	private void waitingSidebar() {
		String mapName = Game.getInstance().getMapManager().getMapName();
		List<Content> contents = Lists.newArrayList();
		if (Game.getInstance().getMapManager().getMapName() == null) for (String line : translateUTF(
				"sidebar.votewaiting", "size", Settings.getSize()).split("\n"))
			contents.add(new Content(line, 0));
		else for (String line : translateUTF("sidebar.mapwaiting", "size", Settings.getSize(), "map", mapName,
				"builder", Game.getInstance().getMapManager().getMapBuilder()).split("\n"))
			contents.add(new Content(line, 0));
		Sidebar.set(player, translateUTF("sidebar.title"), contents);
	}

	private void startingSidebar() {
		String mapName = Game.getInstance().getMapManager().getMapName();
		List<Content> contents = Lists.newArrayList();
		if (Game.getInstance().getMapManager()
				.getMapName() == null) for (String line : translateUTF("sidebar.votestarting", "size",
						Settings.getSize(), "time", Game.getInstance().getLobbyCountdown().getCountdown()).split("\n"))
			contents.add(new Content(line, 0));
		else for (String line : translateUTF("sidebar.mapstarting", "size", Settings.getSize(), "map", mapName,
				"builder", Game.getInstance().getMapManager().getMapBuilder(), "time",
				Game.getInstance().getLobbyCountdown().getCountdown()).split("\n"))
			contents.add(new Content(line, 0));
		Sidebar.set(player, translateUTF("sidebar.title"), contents);
	}
}
