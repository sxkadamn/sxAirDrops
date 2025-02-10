package net.mcdrop.bukkit.mythical.listener;

import net.mcdrop.bukkit.mythical.ChestMythical;
import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class CheckingMyth implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof Chest chest)) return;

        for (ChestMythical chestMythical : sxAirDrops.getInstance().getChestManager().getChestActives())
                if (chestMythical.getLocation().equals(chest.getLocation()))
                    Bukkit.getScheduler().runTaskLater(sxAirDrops.getInstance(), () -> chestMythical.check(sxAirDrops.getInstance().getChestManager()), 5L);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block instanceof Chest chest)) return;

        for (ChestMythical chestMythical : sxAirDrops.getInstance().getChestManager().getChestActives())
            if (chestMythical.getLocation().equals(chest.getLocation()))
                event.setCancelled(true);
    }
}
