package net.mcdrop.bukkit.chest.listeners.joiner;

import net.mcdrop.bukkit.chest.bossbar.BossBarChest;
import net.mcdrop.bukkit.chest.bossbar.manager.BossBarManager;
import net.mcdrop.sxAirDrops;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BossBarChest bossBar = BossBarManager.getBossBar(event.getPlayer().getWorld().getUID());
        if (bossBar != null)
            bossBar.showToPlayer(event.getPlayer());

        sxAirDrops.getInstance().getChestManager().getChestActives().forEach(chestMythical -> chestMythical.getHologram().show(event.getPlayer()));
    }

}
