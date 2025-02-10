package net.mcdrop.common.protocolLib.hologram;


import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.mcdrop.common.protocolLib.impl.FakeArmorStand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class HologramLine {
    private String lineName;
    private Location location;
    private final FakeArmorStand stand;
    private BiConsumer<Player, PacketEvent> onClick;
    private ItemStack item;

    public HologramLine(String lineName, Location location) {
        this.lineName = lineName;
        this.location = location;
        this.stand = new FakeArmorStand(location);
        this.stand.createPacket();
        this.stand.setSmall(true);
        this.stand.setInvisible(true);
        this.stand.setArms(true);
        if (lineName == null) {
            this.stand.setMessage(null);
        } else {
            this.stand.setMessage(ChatColor.translateAlternateColorCodes('&', lineName));
        }
        if (this.onClick != null) {
            this.stand.setClickAction(this.onClick);
            this.stand.setReader();
        }
        this.stand.update();
        this.stand.setEquipment(this.item, EnumWrappers.ItemSlot.HEAD);
    }

    public HologramLine(String lineName, Location location, ItemStack item) {
        this.lineName = lineName;
        this.location = location;
        this.stand = new FakeArmorStand(location);
        this.stand.createPacket();
        this.stand.setSmall(true);
        this.stand.setInvisible(true);
        this.stand.setArms(true);
        if (lineName == null) {
            this.stand.setMessage(null);
        } else {
            this.stand.setMessage(ChatColor.translateAlternateColorCodes('&', lineName));
        }
        if (this.onClick != null) {
            this.stand.setClickAction(this.onClick);
            this.stand.setReader();
        }
        this.stand.update();
        if (item.getType() != Material.AIR)
            this.stand.setEquipment(item, EnumWrappers.ItemSlot.HEAD);
    }

    public void setClickAction(BiConsumer<Player, PacketEvent> onClick) {
        if (this.onClick != null) {
            throw new IllegalStateException("Click action is already set.");
        }
        this.onClick = onClick;
        stand.setClickAction(onClick);
        stand.setReader();
        stand.update();
    }

    public void show(Player player) {
        stand.show(player);
    }

    public FakeArmorStand getStand() {
        return stand;
    }

    public BiConsumer<Player, PacketEvent> getOnClick() {
        return onClick;
    }

    public void showAll() {
        Bukkit.getOnlinePlayers().forEach(stand::show);
    }

    public void unShow(Player player) {
        stand.unShow(player);
    }

    public void unShowAll() {
        Bukkit.getOnlinePlayers().forEach(stand::unShow);
    }

    public void teleport(Location newLocation) {
        this.location = newLocation;
        stand.setPosition(newLocation);
        stand.update();
    }

    public void removeLine() {
        stand.removeStand();
    }

    // Getters and setters for fields
    public String getLineName() {
        return lineName;
    }

    public Location getLocation() {
        return location;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName != null ? lineName : "";
        stand.setMessage(ChatColor.translateAlternateColorCodes('&', this.lineName));
        stand.update();
        showAll();
    }

    public void setItem(ItemStack item) {
        this.item = item;
        stand.setEquipment(item != null ? item : new ItemStack(Material.AIR), EnumWrappers.ItemSlot.HEAD);
        stand.update();
    }
}
