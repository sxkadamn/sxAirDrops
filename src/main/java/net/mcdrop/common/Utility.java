package net.mcdrop.common;

import net.mcdrop.sxAirDrops;
import net.mcdrop.bukkit.chest.effects.ITypeParticle;
import net.mcdrop.bukkit.chest.effects.factory.IParticeFactory;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class Utility {

    public static void playParticleEffectBasedOnConfig(Block block, String stage) {
        FileConfiguration config = sxAirDrops.getInstance().getConfig();

        String animationType = config.getString("event.animations." + stage + ".anima");

        if (animationType == null) return;

        ITypeParticle animation = IParticeFactory.getParticleEffect(animationType);

        if (animation != null) {
            animation.playParticleEffect(block);
        } else {
            Bukkit.getLogger().warning(sxAirDrops.getInstance().getConfig()
                    .getString("messages.animation_not_found")
                    .replace("{animation}", animationType));
        }
    }

    public static void playConfiguredSounds(Player player, String configPath) {
        sxAirDrops.getInstance().getConfig().getStringList(configPath).forEach(soundData -> {
            String[] parts = soundData.split(":");
            if (parts.length == 3) {
                try {
                    Sound sound = Sound.valueOf(parts[0]);
                    float volume = Float.parseFloat(parts[1]);
                    float pitch = Float.parseFloat(parts[2]);

                    player.playSound(player.getLocation(), sound, volume, pitch);
                } catch (IllegalArgumentException e) {
                    sxAirDrops.getInstance().getLogger().warning(sxAirDrops.getInstance().getConfig()
                            .getString("messages.invalid_sound_configuration")
                            .replace("{soundData}", soundData));
                }
            } else {
                sxAirDrops.getInstance().getLogger().warning(sxAirDrops.getInstance().getConfig()
                        .getString("messages.invalid_sound_format")
                        .replace("{soundData}", soundData));
            }
        });
    }

    public static void checkPlugin(String pluginName) {
        if (!getServer().getPluginManager().isPluginEnabled(pluginName)) {
            getLogger().severe(sxAirDrops.getInstance().getConfig()
                    .getString("messages.plugin_not_enabled")
                    .replace("{pluginName}", pluginName));
            getServer().getPluginManager().disablePlugin(sxAirDrops.getInstance());
        }
    }

    public static Float getVersion() {
        try {
            String version = Bukkit.getVersion();
            Pattern pattern = Pattern.compile("\\(MC: ([0-9]+\\.[0-9]+)");
            Matcher matcher = pattern.matcher(version);
            if (matcher.find())
                return Float.parseFloat(matcher.group(1));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return 0f;
    }

//    public static Location findLocation(World world, int minX, int maxX, int minZ, int maxZ) {
//        int x = getRandom(minX, maxX);
//        int z = getRandom(minZ, maxZ);
//        Location location = new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z);
//
//        if (isOceanOrRiver(location)) {
//            return location;
//        }
//        for (int attempts = 0; attempts < 1000; attempts++) {
//            x = getRandom(minX, maxX);
//            z = getRandom(minZ, maxZ);
//            location = new Location(world, x, world.getHighestBlockYAt(x, z) + 1, z);
//            if (isOceanOrRiver(location)) {
//                return location;
//            }
//        }
//
//        throw new RuntimeException("Unable to find a suitable location within the specified range.");
//    }
//
//    private static boolean isOceanOrRiver(Location location) {
//        Biome biome = location.getBlock().getBiome();
//        return (biome != Biome.OCEAN && biome != Biome.RIVER);
//    }
//
//    public static int getRandom(int min, int max) {
//        return ThreadLocalRandom.current().nextInt(min, max + 1);
//    }

//    public static Location generateLocation(int radius) {
//        final int BASE_HEIGHT = 200;
//        final int MAX_HEIGHT = 600;
//
//        Random random = new Random();
//        int x = 0, z = 0, y = BASE_HEIGHT;
//        boolean validLocation = false;
//        Location location = null;
//
//        while (!validLocation) {
//            if (y >= MAX_HEIGHT) {
//                x = random.nextInt(radius * 2) - radius;
//                z = random.nextInt(radius * 2) - radius;
//                y = BASE_HEIGHT;
//            }
//
//            Location currentLocation = new Location(Bukkit.getWorld("world"), x, y, z);
//            Location above = currentLocation.clone().add(0, 1, 0);
//            Location below = currentLocation.clone().add(0, -1, 0);
//
//            if (currentLocation.getBlock().getType().equals(Material.AIR) &&
//                    above.getBlock().getType().equals(Material.AIR) &&
//                    !below.getBlock().getType().equals(Material.AIR) &&
//                    !below.getBlock().getType().equals(Material.LAVA) &&
//                    !below.getBlock().getType().equals(Material.WATER)) {
//
//                location = currentLocation.clone().add(0.5, 0, 0.5);
//                location.getChunk().load();
//
//                if (!location.isChunkLoaded()) {
//                    getLogger().severe("Location chunk not loaded");
//                    return null;
//                }
//                validLocation = true;
//            } else {
//                y++;
//            }
//        }
//        return location;
//    }

}
