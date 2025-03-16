package net.mcdrop.bukkit.mythical.listener;

import net.mcdrop.common.base.chest.ChestListFiller;
import net.mcdrop.common.base.chest.fake.ChestFake;
import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class ControlDrop implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Chest chest)) return;

        for (ChestFake chestMythical : ChestListFiller.getAllChests().values())
            if (chestMythical.getLocation().equals(chest.getLocation()))
                Bukkit.getScheduler().runTaskLater(sxAirDrops.getInstance(), chestMythical::check, 5L);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block instanceof Chest)) return;

        for (ChestFake chestMythical : ChestListFiller.getAllChests().values())
            if (chestMythical.getLocation().equals(block.getLocation()))
                event.setCancelled(true);
    }
}
