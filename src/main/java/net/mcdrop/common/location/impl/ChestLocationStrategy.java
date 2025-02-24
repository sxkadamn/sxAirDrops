package net.mcdrop.common.location.impl;

import net.mcdrop.sxAirDrops;
import net.mcdrop.common.location.LocationStrategy;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChestLocationStrategy implements LocationStrategy {

    private final List<String> UNSUITABLE_BLOCKS =
            sxAirDrops.getInstance().getConfig().getStringList("event.tools.location.generate.black_List_Blocks");
    private final List<String> UNSUITABLE_BIOMES =
            sxAirDrops.getInstance().getConfig().getStringList("event.tools.location.generate.black_List_Biomes");


    public void findLocationAsync(World world, int minX, int maxX, int minZ, int maxZ,
                                  int maxAttempts, int schematicWidth, int schematicLength,
                                  LocationCallBack callback) {
        Bukkit.getScheduler().runTaskAsynchronously(sxAirDrops.getInstance(), () -> {
            try {
                Location location = null;
                for (int attempts = 0; attempts < maxAttempts; attempts++) {
                    int x = getRandom(minX, maxX);
                    int z = getRandom(minZ, maxZ);
                    int y = world.getHighestBlockYAt(x, z);

                    if (y == -1) continue;

                    Location baseLocation = new Location(world, x, y, z);

//                    if (!checkForEvenness(baseLocation)) {
//                        sxAirDrops.getInstance().getLogger().info("Локация не прошла проверку на ровность: X=" + x + ", Y=" + y + ", Z=" + z);
//                        continue;
//                    }

                    if (isSuitableLocation(baseLocation, schematicWidth,schematicLength)) {
                        location = baseLocation.add(0, 1, 0);
                        break;
                    }
                }

                final Location finalLocation = location;
                Bukkit.getScheduler().runTask(sxAirDrops.getInstance(), () -> {
                    if (finalLocation != null) {
                        callback.onLocationFound(finalLocation);
                    } else {
                        callback.onFailure(sxAirDrops.getInstance().getConfig().getString("messages.location_not_found")
                                .replace("{attempts}", String.valueOf(maxAttempts)));
                    }
                });

            } catch (Exception e) {
                Bukkit.getScheduler().runTask(sxAirDrops.getInstance(), () -> {
                    callback.onFailure(sxAirDrops.getInstance().getConfig().getString("messages.location_search_error")
                            .replace("{error}", e.getMessage()));
                });
            }
        });
    }

//    private int getHighestBlock(Chunk chunk, int worldX, int worldZ, int maxY) {
//        int chunkX = worldX & 15;
//        int chunkZ = worldZ & 15;
//
//        for (int y = maxY; y > 30; y--) {
//            Block block = chunk.getBlock(chunkX, y, chunkZ);
//
//
//            if (!block.getType().isAir() && !UNSUITABLE_BLOCKS.contains(block.getType().name())) {
//                return y;
//            }
//        }
//        return -1;
//    }

    private boolean isSuitableLocation(Location baseLocation, int schematicWidth, int schematicLength) {
        World world = baseLocation.getWorld();
        if (world == null) return false;

        Biome biome = baseLocation.getBlock().getBiome();
        if (UNSUITABLE_BIOMES.contains(biome.name())) {
            sxAirDrops.getInstance().getLogger().info(sxAirDrops.getInstance().getConfig()
                    .getString("messages.unsuitable_biome")
                    .replace("{biome}", biome.name()));
            return false;
        }

        for (int xOffset = 0; xOffset < schematicWidth; xOffset++) {
            for (int zOffset = 0; zOffset < schematicLength; zOffset++) {
                Location checkLocation = baseLocation.clone().add(xOffset, -1, zOffset);
                Block block = checkLocation.getBlock();

                if (UNSUITABLE_BLOCKS.contains(block.getType().name())) {
                    sxAirDrops.getInstance().getLogger().info(sxAirDrops.getInstance().getConfig()
                            .getString("messages.unsuitable_block")
                            .replace("{block}", block.getType().name()));
                    return false;
                }
            }
        }

        return true;
    }

//    public boolean checkForEvenness(@NotNull Location location) {
//        int startX = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.start-x");
//        int endX = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.end-x");
//        int startY = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.start-y");
//        int endY = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.end-y");
//        int startZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.start-z");
//        int endZ = sxAirDrops.getInstance().getConfig().getInt("event.tools.location.generate.evenness.end-z");
//
//        List<String> ignoredBlocks = sxAirDrops.getInstance().getConfig().getStringList("event.tools.location.generate.evenness.ignored-blocks");
//
//        int baseY = location.getBlockY();
//
//        for (int x = startX; x <= endX; x++) {
//            for (int z = startZ; z <= endZ; z++) {
//                for (int y = startY; y <= endY; y++) {
//                    Location checkLocation = location.clone().add(x, y, z);
//                    Block block = checkLocation.getBlock();
//
//                    if (y == 0) {
//
//                        if (block.getY() != baseY || ignoredBlocks.contains(block.getType().name())) {
//                            return false;
//                        }
//                    } else {
//
//                        if (!block.getType().isAir() && !ignoredBlocks.contains(block.getType().name())) {
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }

    private int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}