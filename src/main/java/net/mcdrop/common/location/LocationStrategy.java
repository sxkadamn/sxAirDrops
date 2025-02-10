package net.mcdrop.common.location;

import net.mcdrop.common.location.impl.LocationCallBack;
import org.bukkit.World;

public interface LocationStrategy {

    void findLocationAsync(World world, int minX, int maxX, int minZ, int maxZ, int maxAttempts, int schematicWidth,  int schematicLength, LocationCallBack callBack);
}