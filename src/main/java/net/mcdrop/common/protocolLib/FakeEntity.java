package net.mcdrop.common.protocolLib;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

public interface FakeEntity {
    void show(Player paramPlayer);

    void unShow(Player paramPlayer);

    void remove(Player paramPlayer);

    void setMetadata();

    PacketContainer getMetaPacket();
}