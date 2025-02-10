package net.mcdrop.bukkit.chest.listeners.opened;


import net.mcdrop.common.Utility;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class OpenedListener implements Listener {


    @EventHandler
    public void openedChest(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block != null && block.getState()
                instanceof Chest
                && block.hasMetadata("opened")) {
//            Chest chest = (Chest) block.getState();
//            String chestLocation = chest.getLocation().toString();
//
//            event.setCancelled(true);
//                sxAirDrops.getInstance().getConfig().getStringList("messages.drop_looted")
//                        .forEach(s ->
//                                player.sendMessage(
//                                        Plugin.getWithColor().hexToMinecraftColor(s.replace("{player}", player.getDisplayName()))));

            Utility.playConfiguredSounds(player, "events.sounds.opened");
        }
    }
}