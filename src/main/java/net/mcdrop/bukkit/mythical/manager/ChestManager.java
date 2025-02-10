package net.mcdrop.bukkit.mythical.manager;

import net.mcdrop.bukkit.mythical.ChestMythical;
import net.mcdrop.bukkit.mythical.KeysMythical;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ChestManager {

    private final List<KeysMythical> mythicals = new ArrayList<>();
    private final List<ChestMythical> chestMythicals = new ArrayList<>();

    public ChestManager(FileConfiguration config) {
        loadKeys(config);
    }

    public void loadKeys(FileConfiguration config) {
        for (String key : config.getConfigurationSection("event.keys").getKeys(false)) {
            mythicals.add(new KeysMythical(
                    config.getString("event.keys." + key + ".id"),
                    config.getString("event.keys." + key + ".name"),
                    Material.matchMaterial(config.getString("event.keys." + key + ".material"))
            ));
        }
    }

    public void loadChests(FileConfiguration config) {
        for (String key : config.getConfigurationSection("event.chests").getKeys(false)) {
            Location loc = new Location(
                    Bukkit.getWorld(config.getString("event.chests." + key + ".world")),
                    config.getDouble("event.chests." + key + ".x"),
                    config.getDouble("event.chests." + key + ".y"),
                    config.getDouble("event.chests." + key + ".z")
            );

            String requiredKey = config.getString("event.chests." + key + ".key");
            chestMythicals.add(new ChestMythical(key, loc, requiredKey));
        }
    }

    public List<ChestMythical> getChestActives() {
        return chestMythicals;
    }

    public List<KeysMythical> getMythicals() {
        return mythicals;
    }

    public KeysMythical getKeyById(String id) {
        return mythicals.stream()
                .filter(k -> k.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public boolean areAllChestsActivated() {
            return chestMythicals.stream().allMatch(ChestMythical::isActivated);
    }

}
