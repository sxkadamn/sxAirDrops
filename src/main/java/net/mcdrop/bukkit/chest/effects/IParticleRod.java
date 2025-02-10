package net.mcdrop.bukkit.chest.effects;

import net.mcdrop.sxAirDrops;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;


public class IParticleRod implements ITypeParticle {

    @Override
    public void playParticleEffect(Block block) {
        if (block.getType() != Material.CHEST) {
            throw new IllegalArgumentException(sxAirDrops.getInstance().getConfig().getString("messages.must_be_chest"));
        }

        World world = block.getWorld();
        Location chestLocation = block.getLocation().add(0.5, 0.5, 0.5);

        new BukkitRunnable() {
            double angle = 0;
            double heightShift = 0;

            @Override
            public void run() {
                if (block.getType() != Material.CHEST &&
                        !block.hasMetadata("mcpAirDrop")) {
                    cancel();
                    return;
                }

                for (int i = 0; i < 360; i += 20) {
                    double radians = Math.toRadians(i + angle);
                    double x = Math.cos(radians) * 1.5;
                    double z = Math.sin(radians) * 1.5;

                    double y = Math.sin(heightShift) * 0.5;

                    world.spawnParticle(Particle.END_ROD, chestLocation.clone().add(x, y, z), 0, 0, 0, 0, 0);

                    if (i % 90 == 0) {
                        world.spawnParticle(Particle.FLAME, chestLocation.clone().add(x, y, z), 0, 0.1, 0.1, 0.1, 0.01);
                    }
                }

                angle += 10;
                if (angle >= 360) angle = 0;

                heightShift += 0.1;
                if (heightShift >= Math.PI * 2) heightShift = 0;
            }
        }.runTaskTimer(sxAirDrops.getInstance(), 0L, 5L);
    }

}
