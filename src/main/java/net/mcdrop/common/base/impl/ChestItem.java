package net.mcdrop.common.base.impl;

import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ChestItem {
    private final ItemStack itemStack;
    private final int amount;

    public ChestItem(ItemStack itemStack, int amount) {
        this.itemStack = itemStack;
        this.amount = amount;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void saveToConfig(ConfigurationSection section, String key) {
        section.set(key + ".amount", this.amount);
        section.set(key + ".stack", this.itemStack);
    }

    public static ChestItem loadFromConfig(ConfigurationSection section, String key) {
        int amount = section.getInt(key + ".amount");
        ItemStack itemStack = section.getItemStack(key + ".stack");

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.failed_to_load_item")
                    .replace("{key}", key));
            return null;
        }

        return new ChestItem(itemStack, amount);
    }

    @Override
    public String toString() {
        return "ChestItem{" +
                "itemStack=" + (itemStack != null ? itemStack.getType() : "null") +
                ", amount=" + amount +
                '}';
    }

}
