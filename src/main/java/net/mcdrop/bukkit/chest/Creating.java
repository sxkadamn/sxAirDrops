package net.mcdrop.bukkit.chest;

import net.mcdrop.sxAirDrops;
import net.mcdrop.bukkit.Tasks;
import net.mcdrop.bukkit.chest.bossbar.BossBarChest;
import net.mcdrop.bukkit.chest.bossbar.manager.BossBarManager;
import net.mcdrop.bukkit.chest.bossbar.play.LockedBossBar;
import net.mcdrop.bukkit.chest.bossbar.play.OpenedBossBar;
import net.mcdrop.bukkit.chest.region.Region;
import net.mcdrop.common.Utility;
import net.mcdrop.common.protocolLib.hologram.Hologram;
import net.mcdrop.common.protocolLib.hologram.HologramLine;
import net.mcdrop.common.schematic.IEditSessionManager;
import net.mcdrop.common.schematic.ISchematicManager;
import net.mcdrop.common.schematic.manage.EditSessionManager;
import net.mcdrop.common.schematic.manage.SchematicManager;
import net.lielibrary.bukkit.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Creating {

    private final List<ItemStack> list;
    private final String chestId;
    private final Material chestMaterial;
    private final Location chestLocation;
    private final int chestTimeToOpen;
    private final int chestTimeToDestroy;

    private final String display;

    private Hologram hologram;
    private BossBarChest bossBarChest;

    private Region region;

    private ISchematicManager iSchematicManager;
    private IEditSessionManager iEditSessionManager;

    public Creating(List<ItemStack> list, String chestId, Material chestMaterial, Location chestLocation, int chestTimeToOpen, int chestTimeToDestroy, String display) {
        this.list = list;
        this.chestId = chestId;
        this.chestMaterial = chestMaterial;
        this.chestLocation = chestLocation;
        this.chestTimeToOpen = chestTimeToOpen;
        this.chestTimeToDestroy = chestTimeToDestroy;
        this.display = display;

        chestCreate();
    }

    private void chestCreate() {
        if (chestLocation == null || chestLocation.getWorld() == null) {
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.invalid_chest_location").replace("{chestLocation}", chestLocation.toString()));
            return;
        }

        chestLocation.subtract(0, 1, 0);
        if (chestLocation.getBlock().getType().isAir()) {
            String message = sxAirDrops.getInstance().getConfig().getString("messages.schematic_in_air");
            Bukkit.getLogger().warning(message);
        }

        iEditSessionManager = new EditSessionManager();
        iSchematicManager = new SchematicManager(iEditSessionManager);
        iSchematicManager.pasteSchematic(chestLocation,
                new File(sxAirDrops.getSchematicsDirectory(), sxAirDrops.getInstance().getConfig().getString("event.tools.schematic.file")));

        chestLocation.clone().add(
                sxAirDrops.getInstance().getConfig().getDouble("event.tools.block.setting.x"),
                sxAirDrops.getInstance().getConfig().getDouble("event.tools.block.setting.y"),
                sxAirDrops.getInstance().getConfig().getDouble("event.tools.block.setting.z"));
        Block block = chestLocation.getBlock();

        block.setType(chestMaterial);
        block.setMetadata(chestId, new FixedMetadataValue(sxAirDrops.getInstance(), chestId));

        region = new Region();
        region.regionCreate(block);


        hologram = new Hologram(
                sxAirDrops.getInstance().getConfig().getString("event.holograms.locked")
                        .replace("{time}", String.valueOf(chestTimeToOpen)),
                block.getLocation().add(0.5, 0.5, 0.5)
        );
        hologram.showAll();

        taskOpen();

        Utility.playParticleEffectBasedOnConfig(block, "locked");

        List<String> messages = sxAirDrops.getInstance().getConfig().getStringList("messages.drop_spawned");

        if (messages.isEmpty()) {
            return;
        }

        String x = String.valueOf(block.getX());
        String y = String.valueOf(block.getY());
        String z = String.valueOf(block.getZ());

        messages.stream()
                .map(message -> Plugin.getWithColor().hexToMinecraftColor(message)
                        .replace("%x%", x)
                        .replace("%y%", y)
                        .replace("%z%", z)
                        .replace("%rare%", Plugin.getWithColor().hexToMinecraftColor(display)))
                .forEach(formattedMessage -> Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(formattedMessage)));


        if (block.getState() instanceof Chest chest) {
            chest.setMetadata("close", new FixedMetadataValue(sxAirDrops.getInstance(), "close"));
            chest.close();
            chest.update();
        }
        else {
            Bukkit.getLogger().warning(
                    sxAirDrops.getInstance().getConfig().getString("messages.block_is_not_chest")
                            .replace("{chestLocation}", chestLocation.toString())
            );
        }

    }

    private void taskOpen() {
        AtomicInteger timeLeft = new AtomicInteger(chestTimeToOpen);
        bossBarChest = new LockedBossBar();
        new BossBarManager().addBossBar(chestLocation.getWorld().getUID(), bossBarChest);

        Bukkit.getScheduler().runTaskTimer(sxAirDrops.getInstance(), () -> {
            int remainingTime = timeLeft.decrementAndGet();

            if (remainingTime <= 0) {
                chestOpen();


                if (bossBarChest != null) {
                    bossBarChest.stop();
                }

                bossBarChest = new OpenedBossBar();
                new BossBarManager().addBossBar(chestLocation.getWorld().getUID(), bossBarChest);

                Bukkit.getScheduler().cancelTasks(sxAirDrops.getInstance());

                taskDelete(chestLocation.getBlock());

                Utility.playParticleEffectBasedOnConfig(chestLocation.getBlock(), "opened");
            }

            if (bossBarChest != null) {
                String updatedTitle = Plugin.getWithColor().hexToMinecraftColor(
                        bossBarChest.getName()
                                .replace("{time}", String.valueOf(remainingTime))
                                .replace("{x}", String.valueOf(chestLocation.getX()))
                                .replace("{y}", String.valueOf(chestLocation.getY()))
                                .replace("{z}", String.valueOf(chestLocation.getZ()))
                );
                bossBarChest.getBossBar().setTitle(updatedTitle);
                bossBarChest.getBossBar().setProgress((double) remainingTime / chestTimeToOpen);
            }

            if (hologram != null) {
                String hologramText = sxAirDrops.getInstance().getConfig().getString("event.holograms.locked")
                        .replace("{time}", String.valueOf(remainingTime));
                HologramLine firstLine = hologram.getLines().get(0);
                firstLine.setLineName(hologramText);
                firstLine.showAll();
            }

        }, 20L, 20L).getTaskId();
    }

    private void taskDelete(Block block) {
        AtomicInteger timeLeftAtomic = new AtomicInteger(chestTimeToDestroy);

        Chest chest = (Chest) block.getState();

        Bukkit.getScheduler().runTaskTimer(sxAirDrops.getInstance(), () -> {
            int currentTimeLeft = timeLeftAtomic.decrementAndGet();

            String text = sxAirDrops.getInstance().getConfig().getString("event.holograms.opened")
                    .replace("{time}", String.valueOf(currentTimeLeft));

            if (hologram != null) {
                hologram.setLine(text);
                HologramLine hologramLine = hologram.getLines().get(0);
                hologramLine.setLineName(text);
            }

            if (bossBarChest != null) {
                String newTitle = Plugin.getWithColor().hexToMinecraftColor(
                        bossBarChest.getName()
                                .replace("{time}", String.valueOf(currentTimeLeft))
                                .replace("{x}", String.valueOf(chestLocation.getX()))
                                .replace("{y}", String.valueOf(chestLocation.getY()))
                                .replace("{z}", String.valueOf(chestLocation.getZ()))

                );

                bossBarChest.getBossBar().setTitle(newTitle);
                bossBarChest.getBossBar().setProgress((double) currentTimeLeft / chestTimeToDestroy);
            }

            if (currentTimeLeft <= 0) {
                block.setType(Material.AIR);

                chest.removeMetadata("opened", sxAirDrops.getInstance());

                iSchematicManager.undoLastPaste();
                iEditSessionManager.clearAllSessions();

                block.removeMetadata(chestId, sxAirDrops.getInstance());
                if (hologram != null) {
                    hologram.remove();
                }
                if (bossBarChest != null) {
                    bossBarChest.stop();
                }

                Bukkit.getScheduler().cancelTasks(sxAirDrops.getInstance());

                new Tasks();
            }
        }, 20L, 20L);
    }

    private void chestOpen() {
        Block block = chestLocation.getBlock();

        if (!(block.getState() instanceof Chest chest)) {
            return;
        }

        if (region != null) {
            region.removeRegion(region.getRegion(), chestLocation.getWorld());
        }
        chest.removeMetadata("close", sxAirDrops.getInstance());
        chest.setMetadata("opened", new FixedMetadataValue(sxAirDrops.getInstance(), "opened"));

        list.forEach(itemStack -> {
            int randomSlot = ThreadLocalRandom.current().nextInt(0, chest.getBlockInventory().getSize());

            if (randomSlot >= 0 && randomSlot < chest.getBlockInventory().getSize())
                chest.getBlockInventory().setItem(randomSlot, itemStack);

        });


        chest.open();

        if (hologram != null) {
            hologram.remove();
        }

        hologram = new Hologram(
                sxAirDrops.getInstance().getConfig().getString("event.holograms.opened")
                        .replace("{time}", String.valueOf(chestTimeToOpen)),
                block.getLocation().add(0.5, 0.5, 0.5)
        );
        hologram.showAll();

        if (bossBarChest != null)
            Bukkit.getOnlinePlayers().forEach(player -> bossBarChest.showToPlayer(player));
    }

}
