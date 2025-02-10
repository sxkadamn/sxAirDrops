package net.mcdrop.common.protocolLib;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class PacketSender {
    private PacketSender() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void sendPacket(Player receiver, PacketContainer packet) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, packet);
    }

    public static void broadcastPacket(PacketContainer packet) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            protocolManager.sendServerPacket(player, packet);
        }
    }

    public static void receivePacket(Player sender, PacketContainer packet) {
        try {
            ProtocolLibrary.getProtocolManager().receiveClientPacket(sender, packet);
        } catch (Exception e) {
            throw new RuntimeException("Cannot recieve packet.", e);
        }
    }
}