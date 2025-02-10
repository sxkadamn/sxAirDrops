package net.mcdrop.bukkit.chest.bossbar.manager;

import net.mcdrop.bukkit.chest.bossbar.BossBarChest;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {
    private static final Map<UUID, BossBarChest> activeBars = new HashMap<>();

    public void addBossBar(UUID worldId, BossBarChest bossBar) {
        activeBars.put(worldId, bossBar);
    }

    public void removeBossBar(UUID worldId) {
        activeBars.remove(worldId);
    }

    public void showBossBarToAllPlayers() {
        activeBars.values().forEach(bossBar -> {
            Bukkit.getOnlinePlayers().forEach(bossBar::showToPlayer);
        });
    }

    public static BossBarChest getBossBar(UUID worldId) {
        return activeBars.get(worldId);
    }
}
