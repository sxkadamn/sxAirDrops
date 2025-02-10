package net.mcdrop.bukkit;

import net.mcdrop.sxAirDrops;
import net.mcdrop.bukkit.chest.Creating;
import net.mcdrop.common.base.drop.DropItemsFiller;
import net.mcdrop.common.base.drop.rarity.DropItemRarity;
import net.mcdrop.common.base.impl.ChestItem;
import net.mcdrop.common.location.LocationStrategy;
import net.mcdrop.common.location.impl.ChestLocationStrategy;
import net.mcdrop.common.location.impl.LocationCallBack;
import net.mcdrop.common.schematic.dimension.Dimensions;
import net.mcdrop.common.schematic.dimension.utility.DimensionUtility;
import net.lielibrary.bukkit.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Tasks {


    private final int timeToStart;
    private final int timeToDestroy;
    private final int timeToOpen;
    private final String timeOne;
    private final String timeTwo;

    public Tasks() {
        int preTimeMessage = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.pre_time_message");
        this.timeToStart = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_start");
        this.timeToDestroy = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_destroy");
        this.timeToOpen = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_open");
        String timeMode = sxAirDrops.getInstance().getConfig().getString("event.task.time_mode");
        this.timeOne = sxAirDrops.getInstance().getConfig().getString("event.task.moscowTime.time_one");
        this.timeTwo = sxAirDrops.getInstance().getConfig().getString("event.task.moscowTime.time_two");


        if ("schedule".equalsIgnoreCase(timeMode)) {
            startTime1Mode(preTimeMessage);
        } else if ("moscow".equalsIgnoreCase(timeMode)) {
            startTime2Mode();
        } else if ("mythical".equalsIgnoreCase(timeMode)) {
            startMythMode();
        }
    }

    private void startMythMode() {
        Bukkit.getScheduler().runTask(sxAirDrops.getInstance(), () -> {
            sxAirDrops.getInstance().getChestManager().loadChests(sxAirDrops.getInstance().getConfig());
        });
    }

    private void startTime2Mode() {
        Bukkit.getScheduler().runTaskTimer(
                sxAirDrops.getInstance(),
                () -> {

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    TimeZone moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    dateFormat.setTimeZone(moscowTimeZone);
                    String nowString = dateFormat.format(new Date());
                    LocalTime now = LocalTime.parse(nowString, timeFormatter);

                    Optional<DropItemRarity> optionalRarity = DropItemsFiller.getAllRarities().values().stream().findFirst();

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
                        if (now.equals(timeOne) || now.equals(timeTwo)) {
                            int minX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-min");
                            int maxX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-max");
                            int minZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-min");
                            int maxZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-max");

                            Dimensions dimensions = new DimensionUtility().getSchematicDimensions(
                                    new File(
                                            sxAirDrops.getSchematicsDirectory(),
                                            sxAirDrops.getInstance().getConfig().getString("event.tools.schematic.file")));

// И
                            LocationStrategy locationStrategy = new ChestLocationStrategy();

                            locationStrategy.findLocationAsync(
                                    Bukkit.getWorld(sxAirDrops.getInstance().getConfig().getString("event.tools.location.generate.world")),
                                    minX, maxX, minZ, maxZ,
                                    sxAirDrops.getInstance().getConfig().getInt("event.tools.attempts"),
                                    dimensions.getWidth(),
                                    dimensions.getLength(),
                                    new LocationCallBack() {
                                        @Override
                                        public void onLocationFound(Location location) {
                                            if (location != null) {
                                                Bukkit.getScheduler().cancelTasks(sxAirDrops.getInstance());

                                                new Creating(itemStackList, "mcpAirDrop", Material.CHEST, location, timeToOpen, timeToDestroy, rarity.getDisplay());
                                            }
                                        }

                                        @Override
                                        public void onFailure(String error) {
                                            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig().getString("messages.location_failure")
                                                    .replace("{error}", String.valueOf(error)));
                                        }
                                    }
                            );
                        }
                    }
                },
                20L, 20L
        );
    }

    private void startTime1Mode(int preStartBroadCoast) {
        Bukkit.getScheduler().runTaskTimer(
                sxAirDrops.getInstance(),
                new Runnable() {
                    private int time = getTimeToStart();

                    @Override
                    public void run() {
                        time--;
                        if (time == preStartBroadCoast) {
                            sxAirDrops.getInstance().getConfig()
                                    .getStringList("messages.pre_drop_spawned")
                                    .forEach(message -> Bukkit.getOnlinePlayers()
                                            .forEach(player -> player.sendMessage(
                                                    Plugin.getWithColor().hexToMinecraftColor(
                                                            message.replace("%time%", String.valueOf(time))
                                                    )
                                            )));
                        }

                        Optional<DropItemRarity> optionalRarity = DropItemsFiller.getAllRarities().values().stream()
                                .skip((int) (Math.random() * DropItemsFiller.getAllRarities().size())) // Случайный выбор редкости
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

                            if (time == 0) {
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

                                                    new Creating(itemStackList, "airDrop", Material.CHEST, location, timeToOpen, timeToDestroy, rarity.getDisplay());
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
                    }
                }, 0L, 20L);
    }


    public int getTimeToStart() {
        return timeToStart;
    }
}
