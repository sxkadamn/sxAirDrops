package net.mcdrop;

import net.mcdrop.bukkit.Tasks;
import net.mcdrop.bukkit.chest.listeners.joiner.JoinerListener;
import net.mcdrop.bukkit.chest.listeners.opened.OpenedListener;
import net.mcdrop.bukkit.chest.listeners.touch.TouchListener;
import net.mcdrop.bukkit.commands.AdminCommands;
import net.mcdrop.bukkit.mythical.listener.ControlDrop;
import net.mcdrop.bukkit.mythical.listener.KeyDrop;
import net.mcdrop.common.Utility;
import net.mcdrop.common.base.chest.ChestListFiller;
import net.mcdrop.common.base.drop.DropItemsFiller;
import net.mcdrop.common.base.key.KeyChestFiller;
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



    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();


        schematicsManager = new SchematicsDirectoryManager(getDataFolder(), "schematics");
        schematicsManager.createDirectoryIfNotExists();

        Utility.checkPlugin("lieLibrary");
        Utility.checkPlugin("WorldGuard");
        Utility.checkPlugin("WorldEdit");
        Utility.checkPlugin("DecentHolograms");

        DropItemsFiller.load();
        KeyChestFiller.load();

        BaseCommand.register(this, new AdminCommands("airdrop"));

        getServer().getPluginManager().registerEvents(new TouchListener(), this);
        getServer().getPluginManager().registerEvents(new OpenedListener(), this);
        getServer().getPluginManager().registerEvents(new JoinerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ControlDrop(),this);
        getServer().getPluginManager().registerEvents(new KeyDrop(), this);

        new Tasks();
    }

    @Override
    public void onDisable() {
        saveConfig();
        DropItemsFiller.save();
        ChestListFiller.save();
        KeyChestFiller.save();
    }

    @NotNull
    public static File getSchematicsDirectory() {
        return schematicsManager.getDirectory();
    }

    public static sxAirDrops getInstance() {
        return instance;
    }
}
