package net.mcdrop.bukkit;

import net.mcdrop.bukkit.mythical.QuestChest;
import net.mcdrop.common.base.chest.ChestListFiller;
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
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Tasks {


    private final int timeToStart;
    private final int timeToDestroy;
    private final int timeToOpen;
    private final String timeOne;
    private final String timeTwo;
    private final Random random;

    public Tasks() {
        int preTimeMessage = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.pre_time_message");
        this.timeToStart = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_start");
        this.timeToDestroy = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_destroy");
        this.timeToOpen = sxAirDrops.getInstance().getConfig().getInt("event.task.settings.time_to_open");
        String timeMode = sxAirDrops.getInstance().getConfig().getString("event.task.time_mode");
        this.timeOne = sxAirDrops.getInstance().getConfig().getString("event.task.moscowTime.time_one");
        this.timeTwo = sxAirDrops.getInstance().getConfig().getString("event.task.moscowTime.time_two");
        this.random = new Random();


        if ("schedule".equalsIgnoreCase(timeMode)) {
            startTime1Mode(preTimeMessage);
        } else if ("moscow".equalsIgnoreCase(timeMode)) {
            startTime2Mode();
        } else if ("mythical".equalsIgnoreCase(timeMode)) {
            startMythMode();
        }
    }

    private void startMythMode() {
        Bukkit.getScheduler().runTask(sxAirDrops.getInstance(), ChestListFiller::load);
    }

    public void startTime2Mode() {
        new BukkitRunnable() {
            @Override
            public void run() {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                TimeZone moscowTimeZone = TimeZone.getTimeZone("Europe/Moscow");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                dateFormat.setTimeZone(moscowTimeZone);

                String nowString = dateFormat.format(new Date());
                LocalTime now = LocalTime.parse(nowString, timeFormatter);

                LocalTime timeOneM = LocalTime.parse(timeOne, timeFormatter);
                LocalTime timeTwoM = LocalTime.parse(timeTwo, timeFormatter);

                Optional<DropItemRarity> optionalRarity = DropItemsFiller.getAllRarities().values().stream()
                        .skip(random.nextInt(DropItemsFiller.getAllRarities().size()))
                        .findFirst();

                if (!optionalRarity.isPresent()) {
                    Bukkit.getLogger().warning("Не удалось выбрать редкость дропа.");
                    return;
                }

                DropItemRarity rarity = optionalRarity.get();
                List<ItemStack> itemStackList = rarity.getItems().stream()
                        .filter(item -> random.nextDouble() * 100 < rarity.getChance())
                        .map(ChestItem::getItemStack)
                        .filter(stack -> stack != null
                                && stack.getType() != Material.AIR)
                        .collect(Collectors.toList());

                if (itemStackList.isEmpty()) {
                    Bukkit.getLogger().warning("Список предметов пуст! Дроп не будет создан.");
                    return;
                }

                if ((now.equals(timeOneM)
                        || now.equals(timeTwoM))) {
                    int minX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-min");
                    int maxX = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.x-direct-max");
                    int minZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-min");
                    int maxZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.serializate.z-direct-max");

                    File schematicFile = new File(sxAirDrops.getInstance().getSchematicsDirectory(),
                            sxAirDrops.getInstance().getConfig().getString("event.tools.schematic.file"));

                    Dimensions dimensions = new DimensionUtility().getSchematicDimensions(schematicFile);
                    World world = Bukkit.getWorld(sxAirDrops.getInstance().getConfig().getString("event.tools.location.generate.world"));

                    if (world == null) {
                        Bukkit.getLogger().warning("Мир не найден! Проверьте конфигурацию.");
                        return;
                    }


                    LocationStrategy locationStrategy = new ChestLocationStrategy();
                    locationStrategy.findLocationAsync(world, minX, maxX, minZ, maxZ,
                            sxAirDrops.getInstance().getConfig().getInt("event.tools.attempts"),
                            dimensions.getWidth(), dimensions.getLength(),
                            new LocationCallBack() {
                                @Override
                                public void onLocationFound(Location location) {
                                    if (location != null) {
                                        Bukkit.getScheduler().cancelTasks(sxAirDrops.getInstance());
                                        new Creating(itemStackList, "mcpAirDrop", Material.CHEST, location, timeToOpen, timeToDestroy, rarity.getDisplay());
                                        Bukkit.getLogger().info("Дроп успешно создан на координатах: " + location);
                                    }
                                }

                                @Override
                                public void onFailure(String error) {
                                    Bukkit.getLogger().warning("Ошибка при поиске локации: " + error);
                                }
                            });
                }
            }
        }.runTaskTimer(sxAirDrops.getInstance(), 0L, 20L);
    }


    private void startTime1Mode(int preStartBroadCoast) {
        Bukkit.getScheduler().runTaskTimer(
                sxAirDrops.getInstance(),
                () -> {
                    int time = getTimeToStart();
                    Optional<DropItemRarity> optionalRarity = DropItemsFiller.getAllRarities().values().stream()
                            .skip(random.nextInt(DropItemsFiller.getAllRarities().size()))
                            .findFirst();

                    if (!optionalRarity.isPresent()) {
                        Bukkit.getLogger().warning("Не удалось выбрать редкость дропа.");
                        return;
                    }

                    DropItemRarity rarity = optionalRarity.get();
                    List<ItemStack> itemStackList = rarity.getItems().stream()
                            .filter(item -> random.nextDouble() * 100 < rarity.getChance())
                            .map(ChestItem::getItemStack)
                            .filter(stack -> stack != null
                                    && stack.getType() != Material.AIR)
                            .collect(Collectors.toList());

                    if (itemStackList.isEmpty()) {
                        Bukkit.getLogger().warning("Список предметов пуст! Дроп не будет создан.");
                        return;
                    }

                    time--;
                    if (time == preStartBroadCoast) {
                        int finalTime = time;
                        sxAirDrops.getInstance().getConfig()
                                .getStringList("messages.pre_drop_spawned")
                                .forEach(message -> Bukkit.getOnlinePlayers()
                                        .forEach(player -> player.sendMessage(
                                                Plugin.getWithColor().hexToMinecraftColor(
                                                        message.replace("%time%", String.valueOf(finalTime))
                                                )
                                        )));
                    }


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
                }, 20L, 20L);
    }


    public int getTimeToStart() {
        return timeToStart;
    }
}


