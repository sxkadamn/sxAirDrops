package net.mcdrop.common.schematic.file.manage;

import net.mcdrop.sxAirDrops;
import net.mcdrop.common.schematic.file.DirectoryManager;
import org.bukkit.Bukkit;

import java.io.File;

public class SchematicsDirectoryManager implements DirectoryManager {
    private final File directory;

    public SchematicsDirectoryManager(File parentDirectory, String folderName) {
        this.directory = new File(parentDirectory, folderName);
    }

    @Override
    public void createDirectoryIfNotExists() {
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Bukkit.getLogger().info(sxAirDrops.getInstance().getConfig()
                        .getString("messages.directory_created")
                        .replace("{path}", directory.getPath()));
            } else {
                Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig()
                        .getString("messages.directory_creation_failed")
                        .replace("{path}", directory.getPath()));
            }
        }
    }

    @Override
    public File getDirectory() {
        return directory;
    }
}
