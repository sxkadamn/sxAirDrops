package net.mcdrop.bukkit.chest.bossbar;

import net.mcdrop.sxAirDrops;
import net.mcdrop.bukkit.chest.bossbar.state.BossBarState;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarChest {
    private final BarColor color;

    private final BarStyle style;

    private String name;


    private static BossBar bossBar;

    public BossBarChest(BossBarState state) {
        this.color = BarColor.valueOf(sxAirDrops.getInstance().getConfig().getString("event.bossbar." + state.getName() + ".color"));
        this.style = BarStyle.valueOf(sxAirDrops.getInstance().getConfig().getString("event.bossbar." + state.getName() + ".style"));
        this.name = sxAirDrops.getInstance().getConfig().getString("event.bossbar." + state.getName() + ".name");
    }

    public void stop() {
        bossBar.setVisible(false);
    }

    public BarColor getColor() {
        return this.color;
    }

    public BarStyle getStyle() {
        return this.style;
    }

    public String getName() {
        return this.name;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void showToPlayer(Player player) {
        if (!getBossBar().getPlayers().contains(player)) {
            getBossBar().addPlayer(player);
        }
    }

    protected void setBossBar(BossBar bossBar) {
        BossBarChest.bossBar = bossBar;
        bossBar.setVisible(true);
        bossBar.setProgress(1.0D);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
    }

    public void setName(String name) {
        this.name = name;
    }
}
