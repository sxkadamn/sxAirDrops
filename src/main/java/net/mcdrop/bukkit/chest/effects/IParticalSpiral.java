package net.mcdrop.bukkit.chest.effects;

import net.mcdrop.sxAirDrops;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class IParticalSpiral implements ITypeParticle{

    @Override
    public void playParticleEffect(Block block) {
        if (block.getType() != Material.CHEST) {
            throw new IllegalArgumentException(sxAirDrops.getInstance().getConfig().getString("messages.must_be_chest"));
        }

        World world = block.getWorld();
        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

        new BukkitRunnable() {
            double angle = 0;
            double innerRadius = 1;
            double outerRadius = 2;
            double heightShift = 0;
            Random random = new Random();

            @Override
            public void run() {
                if (block.getType() != Material.CHEST &&
                        !block.hasMetadata("mcpAirDrop")) {
                    cancel();
                    return;
                }

                for (int i = 0; i < 360; i += 20) {
                    double radians = Math.toRadians(i + angle);
                    double x = innerRadius * Math.cos(radians);
                    double z = innerRadius * Math.sin(radians);
                    double y = Math.sin(heightShift + Math.toRadians(i)) * 0.2;

                    world.spawnParticle(Particle.END_ROD, blockLocation.clone().add(x, y + 0.5, z), 0, 0, 0, 0, 0.05);
                }


                for (int i = 0; i < 360; i += 15) {
                    double radians = Math.toRadians(i - angle);
                    double x = outerRadius * Math.cos(radians);
                    double z = outerRadius * Math.sin(radians);
                    double y = Math.cos(heightShift + Math.toRadians(i)) * 0.3;

                    Particle.DustOptions dustOptions = new Particle.DustOptions(
                            Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 1);
                    world.spawnParticle(Particle.REDSTONE, blockLocation.clone().add(x, y + 0.3, z), 1, dustOptions);
                }

                if (random.nextInt(10) < 2) {
                    double x = random.nextDouble() * 2 - 1;
                    double y = random.nextDouble() * 2;
                    double z = random.nextDouble() * 2 - 1;
                    world.spawnParticle(Particle.FIREWORKS_SPARK, blockLocation.clone().add(x, y, z), 5, 0.1, 0.1, 0.1, 0.05);
                }


                for (double h = 0; h <= 2; h += 0.5) {
                    double radians = Math.toRadians(angle + h * 180);
                    double x = 0.5 * Math.cos(radians);
                    double z = 0.5 * Math.sin(radians);
                    world.spawnParticle(Particle.SPELL_MOB, blockLocation.clone().add(x, h, z), 0, 0, 0, 0, 1);
                }

                if (random.nextInt(5) == 0) { // 20% шанс
                    double x = random.nextDouble() * 2 - 1;
                    double y = random.nextDouble();
                    double z = random.nextDouble() * 2 - 1;
                    world.spawnParticle(Particle.FLAME, blockLocation.clone().add(x, y + 0.3, z), 0, 0, 0, 0, 0.01);
                }

                angle += 5;
                if (angle >= 360) angle = 0;

                heightShift += 0.1;
                if (heightShift >= Math.PI * 2) heightShift = 0;
            }
        }.runTaskTimer(sxAirDrops.getInstance(), 0L, 5L);
    }
}
