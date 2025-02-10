package net.mcdrop.bukkit.chest.bossbar.play;

import net.mcdrop.bukkit.chest.bossbar.BossBarChest;
import net.mcdrop.bukkit.chest.bossbar.state.BossBarState;
import net.lielibrary.bukkit.Plugin;
import org.bukkit.Bukkit;


public class OpenedBossBar extends BossBarChest{

    public OpenedBossBar() {
        super(BossBarState.CHEST_OPENED);
        setBossBar(Bukkit.createBossBar(Plugin.getWithColor().hexToMinecraftColor(getName()), getColor(), getStyle()));
    }
}
