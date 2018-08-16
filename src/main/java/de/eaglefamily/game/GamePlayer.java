/**
 * Created by _BlackEagle_ on 24.07.2018 11:19:43
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
 * @author _BlackEagle_
 */
public class GamePlayer {

	@Getter
	@Setter
	private Player player;
	@Getter
	private final UUID uuid;
	@Getter
	private GameTeam gameTeam;
	@Getter
	private boolean spectator;
	@Getter
	@Setter
	private boolean gamePlayer;
	@Setter
	private Location spawn;

	@Getter
	private GamePlayer spectating;
	@Getter
	@Setter
	private boolean cameraReseted;
	@Getter
	private GamePlayer compassTarget;

	@Getter
	private final Map<Integer, ItemStack> lobbyContents = Maps.newHashMap();
	@Getter
	private final Map<Integer, ItemStack> spectatorContents = Maps.newHashMap();
	@Getter
	private final Map<Integer, ItemStack> gameContents = Maps.newHashMap();

	public GamePlayer(Player player) {
		this.player = player;
		this.uuid = player.getUniqueId();
		setDefaultLobbyContents();
		setDefaultSpectatorContents();
	}

	public CraftPlayer getCraftPlayer() {
		return (CraftPlayer) player;
	}

	public EntityPlayer getEntityPlayer() {
		return getCraftPlayer().getHandle();
	}

	public void send(final String key, final Object... replaces) {
		Message.send(player, key, replaces);
	}

	public BaseComponent[] translate(final String key, final Object... replaces) {
		return Translation.translate(player, key, replaces);
	}

	public String translateUTF(final String key, final Object... replaces) {
		return Translation.translateUTF(player, key, replaces);
	}

	public void sendPacket(final Packet<?> packet) {
		Packets.send(player, packet);
	}

	public void sendPackets(final Packet<?>... packets) {
		Packets.send(player, packets);
	}

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

	public void teleportToSpawn() {
		if (player.isInsideVehicle()) player.leaveVehicle();
		player.teleport(getSpawn());
	}

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

	public String getName(Player player) {
		CheckNameEvent event = new CheckNameEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getName();
	}

	public String getName(GamePlayer gamePlayer) {
		return getName(gamePlayer.getPlayer());
	}

	public String getName() {
		return getName(player);
	}

	public ListName getListName(Player player) {
		CheckListNameEvent event = new CheckListNameEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getListName();
	}

	public ListName getListName(GamePlayer gamePlayer) {
		return getListName(gamePlayer.getPlayer());
	}

	public ListName getListName() {
		return getListName(player);
	}

	public void updateTabList() {
		Game.getInstance().getGamePlayers().forEach(gP -> gP.updateTabListFor(this));
	}

	public void updateTabListForOthers() {
		Game.getInstance().getGamePlayers().forEach(gP -> updateTabListFor(gP));
	}

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

	public String getGroup(Player player) {
		CheckGroupEvent event = new CheckGroupEvent(player, this.player);
		Bukkit.getPluginManager().callEvent(event);
		return event.getGroup();
	}

	public String getGroup(GamePlayer gamePlayer) {
		return getGroup(gamePlayer.getPlayer());
	}

	public String getGroup() {
		return getGroup(player);
	}

	public Object[] getReplaces(Player player) {
		List<Object> replaces = Lists.newArrayList();
		replaces.add("player");
		replaces.add(getName(player) != null ? getName(player) : this.player.getName());
		replaces.add("group");
		replaces.add(getGroup(player) != null ? getGroup(player) : "");
		if (Settings.teams && gameTeam != null) replaces.addAll(Arrays.asList(gameTeam.getReplaces(this)));
		return replaces.toArray();
	}

	public Object[] getReplaces(GamePlayer gamePlayer) {
		return getReplaces(gamePlayer.getPlayer());
	}

	public Object[] getReplaces() {
		return getReplaces(player);
	}

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

		PacketPlayOutCamera camera = new PacketPlayOutCamera(gamePlayer.getEntityPlayer());
		sendPacket(camera);

		spectating = gamePlayer;
		cameraReseted = false;
	}

	public void removeCamera() {
		resetCamera();
		Actionbar.sendPermanentTranslation(player, "actionbar.spectator");
		spectating = null;
		cameraReseted = false;
	}

	public void resetCamera() {
		PacketPlayOutCamera packet = new PacketPlayOutCamera(getEntityPlayer());
		sendPacket(packet);
		player.teleport(spectating.getPlayer());
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		setSpectatorContents();
	}

	public List<GamePlayer> getSpectators() {
		return Game.getInstance().getGamePlayers().stream().filter(gP -> gP.getSpectating() == this)
				.collect(Collectors.toList());
	}

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

	public void setLobbyContents() {
		lobbyContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	public void setSpectatorContents() {
		spectatorContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	public void setGameContents() {
		gameContents.forEach((slot, itemStack) -> {
			player.getInventory().setItem(slot, itemStack);
		});
	}

	public void addLobbyContent(int slot, ItemStack itemStack) {
		lobbyContents.put(slot, itemStack);
	}

	public void removeLobbyContent(int slot) {
		lobbyContents.remove(slot);
	}

	public void addSpectatorContent(int slot, ItemStack itemStack) {
		spectatorContents.put(slot, itemStack);
	}

	public void removeSpectatorContent(int slot) {
		spectatorContents.remove(slot);
	}

	public void addGameContent(int slot, ItemStack itemStack) {
		gameContents.put(slot, itemStack);
	}

	public void removeGameContent(int slot) {
		gameContents.remove(slot);
	}

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
