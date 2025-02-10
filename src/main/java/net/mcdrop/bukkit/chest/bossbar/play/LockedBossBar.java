package net.mcdrop.bukkit.chest.bossbar.play;

import net.mcdrop.bukkit.chest.bossbar.BossBarChest;
import net.mcdrop.bukkit.chest.bossbar.state.BossBarState;
import net.lielibrary.bukkit.Plugin;
import org.bukkit.Bukkit;


public class LockedBossBar extends BossBarChest {

    public LockedBossBar() {
        super(BossBarState.CHEST_LOCKED);
        setBossBar(Bukkit.createBossBar(Plugin.getWithColor().hexToMinecraftColor(getName()), getColor(), getStyle()));
    }

}
