package net.mcdrop.common.base.chest.fake;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;

import java.util.concurrent.atomic.AtomicBoolean;

public interface ChestFake {
    Location getLocation();
    String getRequiredKey();
    String getWorld();
    Hologram getHologram();
    int getX();
    int getY();
    int getZ();
    void check();
    AtomicBoolean isActivated();
}
