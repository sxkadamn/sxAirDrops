package net.mcdrop.common.base;

import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ItemsBase {

    private final String fileName;
    private final File configFile;
    private FileConfiguration config;

    public ItemsBase(String fileName) {
        this.fileName = fileName;
        this.configFile = new File(sxAirDrops.getInstance().getDataFolder(), fileName);
        load();
    }

    private void load() {
        if (!configFile.exists()) {
            sxAirDrops.getInstance().saveResource(fileName, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }


    public void save() {
        try {
            if (config != null) {
                config.save(configFile);
            }
        } catch (IOException e) {
            Bukkit.getLogger().severe(sxAirDrops.getInstance().getConfig().getString("messages.failed_to_save_file")
                    .replace("{fileName}", fileName)
                    .replace("{error}", e.getMessage()));
        }
    }

    public void reload() {
        load();
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
