package net.mcdrop.common.location.impl;

import org.bukkit.Location;

public interface LocationCallBack {

    void onLocationFound(Location location);
    void onFailure(String error);
}
