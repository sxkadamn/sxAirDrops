package net.mcdrop.common.base.chest;

import net.mcdrop.bukkit.mythical.QuestChest;
import net.mcdrop.common.base.ItemsBase;
import net.mcdrop.common.base.chest.fake.ChestFake;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ChestListFiller {
    private static ItemsBase chestConfig;
    private static final Map<String, ChestFake> chests = new HashMap<>();

    public static void load() {
        chestConfig = new ItemsBase("chestList.yml");
        FileConfiguration config = chestConfig.getConfig();

        chests.clear();
        ConfigurationSection chestsSection = config.getConfigurationSection("chests");

        if (chestsSection != null) {
            for (String chestKey : chestsSection.getKeys(false)) {
                ConfigurationSection chestSection = chestsSection.getConfigurationSection(chestKey);
                if (chestSection != null) {
                    String world = chestSection.getString("world");
                    int x = chestSection.getInt("x");
                    int y = chestSection.getInt("y");
                    int z = chestSection.getInt("z");
                    String key = chestSection.getString("key");

                    Location location = new Location(Bukkit.getWorld(world), x,y,z);
                    ChestFake chest = new QuestChest(key, location, key);
                    chests.put(chestKey, chest);
                }
            }
        }
    }

    public static void save() {
        FileConfiguration config = chestConfig.getConfig();
        config.set("chests", null);

        ConfigurationSection chestsSection = config.createSection("chests");
        chests.forEach((chestKey, chest) -> {
            ConfigurationSection chestSection = chestsSection.createSection(chestKey);
            chestSection.set("world", chest.getWorld());
            chestSection.set("x", chest.getX());
            chestSection.set("y", chest.getY());
            chestSection.set("z", chest.getZ());
            chestSection.set("key", chest.getRequiredKey());
        });

        chestConfig.save();
    }


    public static Map<String, ChestFake> getAllChests() {
        return chests;
    }

    public static void addChest(String chestKey, ChestFake chest) {
        chests.put(chestKey, chest);
    }

    public static void removeChest(String chestKey) {
        chests.remove(chestKey);
    }
}
