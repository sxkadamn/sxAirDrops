package net.mcdrop.common.protocolLib.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import com.google.common.base.Preconditions;
import net.mcdrop.sxAirDrops;
import net.mcdrop.common.Utility;
import net.mcdrop.common.protocolLib.FakeEntity;
import net.mcdrop.common.protocolLib.PacketSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FakeArmorStand implements FakeEntity {
    private final Location location;

    private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();

    private boolean isSmall = false;

    private boolean noBasePlate = false;

    private boolean isMarker = false;

    private boolean invisible = false;

    private boolean arms = false;

    private static final int CUSTOM_NAME_INDEX = 2;

    private static final int CUSTOM_NAME_VISIBLE_INDEX = 3;

    private static final int PARAM_ARMOR_STAND_INDEX;

    static {
        if (Utility.getVersion() >= 1.17F) {
            PARAM_ARMOR_STAND_INDEX = 15;
        } else if (Utility.getVersion() > 1.14F) {
            PARAM_ARMOR_STAND_INDEX = 14;
        } else if (Utility.getVersion() == 1.14F) {
            PARAM_ARMOR_STAND_INDEX = 13;
        } else {
            PARAM_ARMOR_STAND_INDEX = 11;
        }
    }

    private final WrappedDataWatcher.Serializer serBoolean = WrappedDataWatcher.Registry.get(Boolean.class);

    private final WrappedDataWatcher.Serializer serByte = WrappedDataWatcher.Registry.get(Byte.class);

    private final WrappedDataWatcher.Serializer serVector = WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass());

    private final WrappedDataWatcher metadata = new WrappedDataWatcher();

    private PacketContainer packet;

    private final EnumMap<EnumWrappers.ItemSlot, PacketContainer> equipmentMap = new EnumMap<>(EnumWrappers.ItemSlot.class);

    private int id;

    private String message = null;

    private final Set<Player> showPlayers = new HashSet<>();

    private BiConsumer<Player, PacketEvent> clickAction;

    private final List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipmentList = new ArrayList<>();

    public FakeArmorStand(Location location) {
        this.location = location;
        Preconditions.checkNotNull(location, "Location cannot be null");
    }

    public void show(Player player) {
        this.showPlayers.add(player);
        PacketContainer metaDataPacket = getMetaPacket();
        PacketSender.sendPacket(player, this.packet);
        PacketSender.sendPacket(player, metaDataPacket);
        if (Utility.getVersion() < 1.13F) {
            this.equipmentMap.forEach((itemSlot, packetContainer) -> PacketSender.sendPacket(player, packetContainer));
        } else {
            PacketContainer equipmentPacket = this.manager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            equipmentPacket.getIntegers().write(0, Integer.valueOf(this.id));
            equipmentPacket.getSlotStackPairLists().write(0, this.equipmentList);
            PacketSender.sendPacket(player, equipmentPacket);
        }
    }

    public void unShow(Player player) {
        this.showPlayers.remove(player);
        remove(player);
    }

    public void setPosition(final Location loc) {
        PacketContainer teleportPacket = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT) {

        };
        this.manager.broadcastServerPacket(teleportPacket);
    }

    public void remove(Player player) {
        PacketContainer removeStandPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        removeStandPacket.getModifier().write(0, new int[] { this.id });
        PacketSender.sendPacket(player, removeStandPacket);
    }

    public void removeStand() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            PacketContainer removeStandPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            removeStandPacket.getModifier().write(0, new int[] { this.id });
            PacketSender.sendPacket(all, removeStandPacket);
        }
    }

    public void setMetadata() {
        if (this.message != null) {
            Optional<?> opt;
            if (Utility.getVersion() <= 1.13F) {
                opt = Optional.of(WrappedChatComponent.fromText(this.message).getHandle());
            } else {
                opt = Optional.of(WrappedChatComponent.fromChatMessage(this.message)[0].getHandle());
            }
            if (Utility.getVersion() > 1.12F) {
                WrappedDataWatcher.Serializer serChatComponent = WrappedDataWatcher.Registry.getChatComponentSerializer(true);
                this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, serChatComponent), opt);
            } else {
                WrappedDataWatcher.Serializer serString = WrappedDataWatcher.Registry.get(String.class);
                this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, serString), this.message);
            }
            this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, this.serBoolean), Boolean.TRUE);
        }
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(PARAM_ARMOR_STAND_INDEX, this.serByte), (byte) ((this.isMarker ? 16 : 0) | (this.isSmall ? 1 : 0) | (this.noBasePlate ? 8 : 0) | (this.arms ? 4 : 0)));
        this.metadata.setObject(0, this.serByte, (byte) (this.invisible ? 32 : 0));
    }

    public PacketContainer getMetaPacket() {
        PacketContainer metaPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        metaPacket.getIntegers().write(0, this.id);
        metaPacket.getWatchableCollectionModifier().write(0, this.metadata.getWatchableObjects());
        return metaPacket;
    }

    public void createPacket() {
        Random random = new Random();
        this.id = random.nextInt(100000) + 10000;
        this.packet = this.manager.createPacket(PacketType.Play.Server.SPAWN_ENTITY_LIVING);
        this.packet.getIntegers().write(0, this.id);
        this.packet.getUUIDs().write(0, UUID.randomUUID());
        if (Utility.getVersion().floatValue() < 1.13F) {
            this.packet.getIntegers().write(1, (int) EntityType.ARMOR_STAND.getTypeId());
        } else {
            this.packet.getIntegers().write(1, 1);
        }
        this.packet.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());
        setMetadata();
    }

    public void setSmall(boolean small) {
        this.isSmall = small;
    }

    public void setMarker(boolean marker) {
        this.isMarker = marker;
    }

    public void setNoBasePlate(boolean noBasePlate) {
        this.noBasePlate = noBasePlate;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public void setClickAction(BiConsumer<Player, PacketEvent> onClick) {
        this.clickAction = onClick;
    }

    public void setReader() {
        this.manager.addPacketListener(new PacketAdapter(sxAirDrops.getInstance(), new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityId = packet.getIntegers().read(0);
                if (entityId == FakeArmorStand.this.id)
                    FakeArmorStand.this.clickAction.accept(event.getPlayer(), event);
            }
        });
    }

    public void setBodyRotation(Vector3F vector3F) {
        int index = 17;
        if (Utility.getVersion().floatValue() < 1.13F)
            index = 13;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setBodyRotation(float x, float y, float z) {
        setBodyRotation(new Vector3F(x, y, z));
    }

    public void setHeadRotation(Vector3F vector3F) {
        int index = 16;
        if (Utility.getVersion() < 1.13F)
            index = 12;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setHeadRotation(float x, float y, float z) {
        setHeadRotation(new Vector3F(x, y, z));
    }

    public void setLeftArmRotation(Vector3F vector3F) {
        int index = 18;
        if (Utility.getVersion() < 1.13F)
            index = 14;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setLeftArmRotation(float x, float y, float z) {
        setLeftArmRotation(new Vector3F(x, y, z));
    }

    public void setRightArmRotation(Vector3F vector3F) {
        int index = 19;
        if (Utility.getVersion() < 1.13F)
            index = 15;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setRightArmRotation(float x, float y, float z) {
        setRightArmRotation(new Vector3F(x, y, z));
    }

    public void setLeftLegRotation(Vector3F vector3F) {
        int index = 20;
        if (Utility.getVersion().floatValue() < 1.13F)
            index = 16;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setLeftLegRotation(float x, float y, float z) {
        setLeftLegRotation(new Vector3F(x, y, z));
    }

    public void setRightLegRotation(Vector3F vector3F) {
        int index = 21;
        if (Utility.getVersion().floatValue() < 1.13F)
            index = 17;
        this.metadata.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(index, this.serVector), vector3F);
    }

    public void setRightLegRotation(float x, float y, float z) {
        setRightLegRotation(new Vector3F(x, y, z));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setArms(boolean arms) {
        this.arms = arms;
    }

    public void update() {
        setMetadata();
        PacketContainer metaDataPacket = getMetaPacket();
        for (Player player : this.showPlayers)
            PacketSender.sendPacket(player, metaDataPacket);
    }

    public void setEquipment(ItemStack item, EnumWrappers.ItemSlot itemSlot) {
        setEquipment(new Pair<>(itemSlot, item));
    }

    public void setEquipment(Pair<EnumWrappers.ItemSlot, ItemStack> equipment) {
        PacketContainer equipmentPacket = this.manager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        equipmentPacket.getIntegers().write(0, this.id);
        if (Utility.getVersion() < 1.13F) {
            equipmentPacket.getItemSlots().write(0, equipment.getFirst());
            equipmentPacket.getItemModifier().write(0, equipment.getSecond());
        } else {
            this.equipmentList.add(equipment);
            equipmentPacket.getSlotStackPairLists().write(0, this.equipmentList);
        }
        for (Player player : this.showPlayers)
            PacketSender.sendPacket(player, equipmentPacket);
        this.equipmentMap.put(equipment.getFirst(), equipmentPacket);
    }
}
