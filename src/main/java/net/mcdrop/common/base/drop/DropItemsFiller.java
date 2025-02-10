package net.mcdrop.common.base.drop;

import net.mcdrop.common.base.ItemsBase;
import net.mcdrop.common.base.drop.rarity.DropItemRarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class DropItemsFiller {

    private static ItemsBase itemsBase;

    private static final Map<String, DropItemRarity> raritiesCache = new HashMap<>();

    public static void load() {
        itemsBase = new ItemsBase("chestItems.yml");
        FileConfiguration config = itemsBase.getConfig();

        raritiesCache.clear();
        ConfigurationSection raresSection = config.getConfigurationSection("rares");

        if (raresSection != null) {
            for (String rarityKey : raresSection.getKeys(false)) {
                ConfigurationSection raritySection = raresSection.getConfigurationSection(rarityKey);
                if (raritySection != null) {
                    DropItemRarity rarity = DropItemRarity.loadFromConfig(raritySection);
                    raritiesCache.put(rarityKey, rarity);
                }
            }
        }
    }

    public static void save() {
        FileConfiguration config = itemsBase.getConfig();
        config.set("rares", null);

        ConfigurationSection raresSection = config.createSection("rares");
        raritiesCache.forEach((rarityKey, rarity) -> {
            ConfigurationSection raritySection = raresSection.createSection(rarityKey);
            rarity.saveToConfig(raritySection);
        });

        itemsBase.save();
    }

    public static HashMap<String, DropItemRarity> getAllRarities() {
        return (HashMap<String, DropItemRarity>) raritiesCache;
    }

}
