package net.mcdrop.bukkit.mythical;

import net.mcdrop.bukkit.mythical.manager.ChestManager;
import net.mcdrop.common.protocolLib.hologram.Hologram;
import net.mcdrop.common.protocolLib.hologram.HologramLine;
import net.mcdrop.sxAirDrops;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChestMythical {

    private final String id;
    private final Location location;
    private final String requiredKey;
    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final Hologram hologram;

    public ChestMythical(String id, Location location, String requiredKey) {
        this.id = id;
        this.location = location;
        this.requiredKey = requiredKey;

        location.getBlock().setType(Material.CHEST);
        hologram = new Hologram("Mythical chest not activated", location.clone().add(0, 2,0));
        hologram.addLine("Need key: " + requiredKey);
        hologram.showAll();
    }

    public void check(ChestManager chestManager) {
        if (!(location.getBlock().getState() instanceof Chest chest)) return;

        ItemStack item = chest.getBlockInventory().getItem(13);

        if (item != null && item.hasItemMeta()) {
            String itemName = item.getItemMeta().getDisplayName();
            KeysMythical key = chestManager.getKeyById(requiredKey);

            if (key != null && itemName.equalsIgnoreCase(key.getName())) {
                isActive.set(true);
                spawnParticles();

                if (hologram != null) {

                    HologramLine firstLine = hologram.getLines().get(0);
                    firstLine.setLineName("Mythical chest is active");
                    firstLine.showAll();
                }

                if (chestManager.areAllChestsActivated()) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "Все сундуки активированы!");
                }
            }
        }
    }

    public boolean isActivated() {
        return isActive.get();
    }

    private void spawnParticles() {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (!isActive.get() || count >= 100) {
                    cancel();
                    return;
                }
                location.getWorld().spawnParticle(Particle.HEART, location.clone().add(0.5, 1, 0.5), 10, 0.2, 0.2, 0.2, 0.01);
                count++;
            }
        }.runTaskTimer(sxAirDrops.getInstance(), 0, 10);
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getRequiredKey() {
        return requiredKey;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public AtomicBoolean getIsActive() {
        return isActive;
    }
}
