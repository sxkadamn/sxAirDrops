package net.mcdrop.bukkit.mythical;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.lielibrary.bukkit.Plugin;
import net.mcdrop.bukkit.chest.Creating;
import net.mcdrop.common.base.chest.ChestListFiller;
import net.mcdrop.common.base.chest.fake.ChestFake;
import net.mcdrop.common.base.drop.DropItemsFiller;
import net.mcdrop.common.base.drop.rarity.DropItemRarity;
import net.mcdrop.common.base.impl.ChestItem;
import net.mcdrop.common.base.key.KeyChestFiller;
import net.mcdrop.common.base.key.item.KeyItem;
import net.mcdrop.common.location.LocationStrategy;
import net.mcdrop.common.location.impl.ChestLocationStrategy;
import net.mcdrop.common.location.impl.LocationCallBack;
import net.mcdrop.common.schematic.dimension.Dimensions;
import net.mcdrop.common.schematic.dimension.utility.DimensionUtility;
import net.mcdrop.sxAirDrops;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class QuestChest implements ChestFake {

    private final String id;
    private final Location location;
    private final String requiredKey;
    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private Hologram hologram;

    public QuestChest(String id, Location location, String requiredKey) {
        this.id = id;
        this.location = location;
        this.requiredKey = requiredKey;

        location.getBlock().setType(Material.CHEST);

        KeyItem key = KeyChestFiller.getKeyById(requiredKey);

        List<String> listText = new ArrayList<>();
        sxAirDrops.getInstance().getConfig().getStringList("event.myth_not_activated").forEach(line ->
                listText.add(Plugin.getWithColor().hexToMinecraftColor(line).replace("%key%", key.getName()))
        );

        hologram = DHAPI.createHologram(id,
                location.clone().add(0.5, 2.7, 0.5),
                listText);
        hologram.showAll();
    }

    public void check() {
        if (!(location.getBlock().getState() instanceof Chest chest)) return;

        ItemStack item = chest.getBlockInventory().getItem(13);

        if (item != null && item.hasItemMeta()) {
            String itemName = item.getItemMeta().getDisplayName();
            KeyItem key = KeyChestFiller.getKeyById(requiredKey);

            if (key != null && itemName.equalsIgnoreCase(Plugin.getWithColor().hexToMinecraftColor(key.getName()))) {
                isActive.set(true);
                spawnParticles();

                if (hologram != null) {
                    hologram.delete();

                    List<String> listText = new ArrayList<>();
                    sxAirDrops.getInstance().getConfig().getStringList("event.myth_activated").forEach(line ->
                            listText.add(Plugin.getWithColor().hexToMinecraftColor(line))
                    );

                    hologram = DHAPI.createHologram(id,
                            location.clone().add(0.5, 2.7, 0.5),
                            listText);
                    hologram.showAll();
                }

                if (areAllChestsActivated()) {
                    spawnDrop();
                    resetChests();

                    sxAirDrops.getInstance().getConfig().getStringList("messages.quest_complete").forEach(s ->
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Plugin.getWithColor().hexToMinecraftColor(s))));
                }
            }
        }
    }

    public AtomicBoolean isActivated() {
        return isActive;
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

    private void spawnDrop() {
        AtomicInteger time = new AtomicInteger(5);

        Bukkit.getScheduler().runTaskTimer(sxAirDrops.getInstance(),() -> {
            time.getAndDecrement();
                Optional<DropItemRarity> optionalRarity = DropItemsFiller.getAllRarities().values().stream()
                        .skip((int) (Math.random() * DropItemsFiller.getAllRarities().size()))
                        .findFirst();

                if (optionalRarity.isPresent()) {
                    DropItemRarity rarity = optionalRarity.get();

                    List<ItemStack> itemStackList = rarity.getItems().stream()
                            .filter(item -> Math.random() * 100 < rarity.getChance())
                            .map(ChestItem::getItemStack)
                            .filter(stack -> {
                                if (stack == null || stack.getType() == Material.AIR) {
                                    Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.incorrect_stack")
                                            .replace("{stack}", String.valueOf(stack)));
                                    return false;
                                }
                                return true;
                            })
                            .collect(Collectors.toList());

                    if (time.get() == 0) {
                        int minX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-min");
                        int maxX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-max");
                        int minZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-min");
                        int maxZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-max");

                        Dimensions dimensions = new DimensionUtility().getSchematicDimensions(
                                new File(sxAirDrops.getSchematicsDirectory(),
                                        sxAirDrops.getInstance().getConfig().getString("event.tools.schematic.file")));

                        LocationStrategy locationStrategy = new ChestLocationStrategy();

                        locationStrategy.findLocationAsync(Bukkit.getWorld("world"), minX, maxX, minZ, maxZ,
                                sxAirDrops.getInstance().getConfig().getInt("event.tools.attempts"),
                                dimensions.getWidth(), dimensions.getLength(), new LocationCallBack() {

                                    @Override
                                    public void onLocationFound(Location location) {
                                        if (location != null) {
                                            Bukkit.getScheduler().cancelTasks(sxAirDrops.getInstance());

                                            new Creating(itemStackList, "airDrop", Material.CHEST, location,
                                                    sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_open"),
                                                    sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_destroy"), rarity.getDisplay());
                                        }
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.location_failure")
                                                .replace("{error}", String.valueOf(error)));
                                    }
                                });
                    }
                }
        },20L,20L);
    }

    private void resetChests() {
        ChestListFiller.getAllChests().values().forEach(chestFake -> {
            Location chestLocation = chestFake.getLocation();

            if (chestLocation.getBlock().getState() instanceof Chest chest) {
                chest.getBlockInventory().clear();
                chestLocation.getBlock().setType(Material.BARRIER);
            }

            if (chestFake.getHologram() != null) {
                chestFake.getHologram().delete();
            }

            chestFake.isActivated().set(false);
        });
    }

    public boolean areAllChestsActivated() {
        return ChestListFiller.getAllChests().values().stream().allMatch(chestFake -> chestFake.isActivated().get());
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

    @Override
    public String getWorld() {
        return String.valueOf(location.getWorld());
    }

    @Override
    public int getX() {
        return location.getBlockX();
    }

    @Override
    public int getY() {
        return location.getBlockY();
    }

    @Override
    public int getZ() {
        return location.getBlockZ();
    }

    public Hologram getHologram() {
        return hologram;
    }

    public AtomicBoolean getIsActive() {
        return isActive;
    }
}
