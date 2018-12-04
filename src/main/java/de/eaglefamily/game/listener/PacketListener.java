/*
 * Created by Jan on 30.07.2018 15:39:55
 */
package de.eaglefamily.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import de.eaglefamily.bukkitlibrary.packet.PacketReader;
import de.eaglefamily.bukkitlibrary.util.Reflections;
import de.eaglefamily.game.Game;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

/**
 * The Object of a listener interface for receiving packet events. When the
 * packet event occurs, that object's appropriate method is invoked.
 *
 * @see PacketEvent
 */
public class PacketListener implements Listener {

	/**
	 * Instantiates a new packet listener.
	 */
	public PacketListener() {
		onUseEntity();
	}

	// Bugfix : Cannot interact self
	private void onUseEntity() {
		PacketReader.onReceiving(Game.getPlugin(), PacketPlayInUseEntity.class, event -> {
			Player player = event.getPlayer();
			PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
			int id = (int) Reflections.getDeclaredFieldObject(packet, int.class, 0);
			if (id == player.getEntityId()) event.setCancelled(true);
		});
	}
}
