package net.mcdrop.bukkit.chest.listeners.touch;

import net.mcdrop.common.Utility;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class TouchListener implements Listener {

    @EventHandler
    public void touchChest(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block != null && block.getState()
                instanceof Chest
                && block.hasMetadata("close")) {

            event.setCancelled(true);

            Utility.playConfiguredSounds(player, "events.sounds.locked");
        }
    }
}
