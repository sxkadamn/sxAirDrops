package net.mcdrop;

import net.mcdrop.bukkit.Tasks;
import net.mcdrop.bukkit.chest.listeners.joiner.JoinerListener;
import net.mcdrop.bukkit.chest.listeners.opened.OpenedListener;
import net.mcdrop.bukkit.chest.listeners.touch.TouchListener;
import net.mcdrop.bukkit.commands.AdminCommands;
import net.mcdrop.bukkit.mythical.listener.CheckingMyth;
import net.mcdrop.bukkit.mythical.manager.ChestManager;
import net.mcdrop.common.Utility;
import net.mcdrop.common.base.drop.DropItemsFiller;
import net.mcdrop.common.schematic.file.DirectoryManager;
import net.mcdrop.common.schematic.file.manage.SchematicsDirectoryManager;
import net.lielibrary.bukkit.command.BaseCommand;
import net.lielibrary.gui.impl.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public final class sxAirDrops extends JavaPlugin {

    private static sxAirDrops instance;

    private static DirectoryManager schematicsManager;

    private ChestManager chestManager;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        chestManager = new ChestManager(getConfig());

        schematicsManager = new SchematicsDirectoryManager(getDataFolder(), "schematics");
        schematicsManager.createDirectoryIfNotExists();

        Utility.checkPlugin("lieLibrary");
        Utility.checkPlugin("WorldGuard");
        Utility.checkPlugin("WorldEdit");
        Utility.checkPlugin("ProtocolLib");

        DropItemsFiller.load();

        BaseCommand.register(this, new AdminCommands("airdrop"));

        getServer().getPluginManager().registerEvents(new TouchListener(), this);
        getServer().getPluginManager().registerEvents(new OpenedListener(), this);
        getServer().getPluginManager().registerEvents(new JoinerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new CheckingMyth(),this);

        new Tasks();
    }

    @Override
    public void onDisable() {
        saveConfig();
        DropItemsFiller.save();
    }

    @NotNull
    public static File getSchematicsDirectory() {
        return schematicsManager.getDirectory();
    }

    public ChestManager getChestManager() {
        return chestManager;
    }

    public static sxAirDrops getInstance() {
        return instance;
    }
}
