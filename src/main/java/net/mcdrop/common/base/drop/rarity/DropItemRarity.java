package net.mcdrop.common.base.drop.rarity;

import net.mcdrop.sxAirDrops;
import net.mcdrop.common.base.impl.ChestItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class DropItemRarity {
    private final String name;
    private final double chance;
    private final List<ChestItem> items;
    private final String display;

    public DropItemRarity(String name, double chance, List<ChestItem> items, String display) {
        this.name = name;
        this.chance = chance;
        this.items = items;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }

    public List<ChestItem> getItems() {
        return items;
    }

    public String getDisplay() {
        return display;
    }

    public void saveToConfig(ConfigurationSection section) {
        section.set("chance", this.getChance());
        section.set("display", this.getDisplay());

        ConfigurationSection itemsSection = section.createSection("items");
        for (int i = 0; i < items.size(); i++) {
            ChestItem chestItem = items.get(i);
            chestItem.saveToConfig(itemsSection, "item" + i);
        }
    }

    public static DropItemRarity loadFromConfig(ConfigurationSection section) {
        String name = section.getName();
        double chance = section.getDouble("chance");
        List<ChestItem> items = new ArrayList<>();
        String display = section.getString("display");

        ConfigurationSection itemsSection = section.getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ChestItem item = ChestItem.loadFromConfig(itemsSection, key);
                if (item != null) {
                    items.add(item);
                } else {
                    Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.invalid_item")
                            .replace("{key}", key));
                }
            }
        }

        return new DropItemRarity(name, chance, items, display);
    }

}
