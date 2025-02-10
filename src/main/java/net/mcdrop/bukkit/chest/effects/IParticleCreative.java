package net.mcdrop.bukkit.chest.effects;

import net.mcdrop.sxAirDrops;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class IParticleCreative implements ITypeParticle{

    @Override
    public void playParticleEffect(Block block) {
        if (block.getType() != Material.CHEST) {
            throw new IllegalArgumentException(sxAirDrops.getInstance().getConfig().getString("messages.must_be_chest"));
        }

        World world = block.getWorld();
        Location blockLocation = block.getLocation().add(0.5, 0.5, 0.5);

        new BukkitRunnable() {
            double angle = 0;
            double radius = 1.5;
            double heightShift = 0;
            double scaleFactor = 0.1;

            @Override
            public void run() {
                if (block.getType() != Material.CHEST &&
                        !block.hasMetadata("mcpAirDrop")) {
                    cancel();
                    return;
                }

                for (int i = 0; i < 360; i += 20) {
                    double radians = Math.toRadians(i + angle);
                    double x = radius * Math.cos(radians);
                    double z = radius * Math.sin(radians);


                    double y = Math.sin(heightShift + Math.toRadians(i)) * 0.3;


                    world.spawnParticle(Particle.END_ROD, blockLocation.clone().add(x, y + 0.3, z), 0, 0, 0, 0, 0);
                }


                radius += scaleFactor;
                if (radius >= 2) scaleFactor = -0.05;
                if (radius <= 1.5) scaleFactor = 0.05;

                angle += 2;
                if (angle >= 360) angle = 0;


                heightShift += 0.05;
                if (heightShift >= Math.PI * 2) heightShift = 0;
            }
        }.runTaskTimer(sxAirDrops.getInstance(), 0L, 5L);
    }
}
