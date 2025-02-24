package net.mcdrop.common.base.key;

import net.mcdrop.common.base.ItemsBase;
import net.mcdrop.common.base.key.drop.DropKey;
import net.mcdrop.common.base.key.item.KeyItem;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class KeyChestFiller {
    private static ItemsBase keyConfig;
    private static final Map<String, KeyItem> keys = new HashMap<>();

    public static void load() {
        keyConfig = new ItemsBase("keyList.yml");
        FileConfiguration config = keyConfig.getConfig();

        keys.clear();
        ConfigurationSection keysSection = config.getConfigurationSection("keys");

        if (keysSection != null) {
            for (String keyName : keysSection.getKeys(false)) {
                ConfigurationSection keySection = keysSection.getConfigurationSection(keyName);
                if (keySection != null) {
                    String id = keySection.getString("id");
                    String name = keySection.getString("name");
                    Material material = Material.valueOf(keySection.getString("material", "TRIPWIRE_HOOK"));
                    double dropChance = keySection.getDouble("dropChance", 0.1);

                    KeyItem key = new DropKey(id, name, material, dropChance);
                    keys.put(keyName, key);
                }
            }
        }
    }

    public static void save() {
        FileConfiguration config = keyConfig.getConfig();
        config.set("keys", null);

        ConfigurationSection keysSection = config.createSection("keys");
        keys.forEach((keyName, key) -> {
            ConfigurationSection keySection = keysSection.createSection(keyName);
            keySection.set("id", key.getId());
            keySection.set("name", key.getName());
            keySection.set("material", key.getMaterial().name());
        });

        keyConfig.save();
    }

    public static Map<String, KeyItem> getAllKeys() {
        return keys;
    }

    public static void addKey(String keyName, KeyItem key) {
        keys.put(keyName, key);
    }

    public static KeyItem getKeyById(String id) {
        return keys.values().stream()
                .filter(k -> k.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static void removeKey(String keyName) {
        keys.remove(keyName);
    }
}
