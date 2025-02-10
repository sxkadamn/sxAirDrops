package net.mcdrop.bukkit.chest.bossbar.state;

public enum BossBarState {
    CHEST_LOCKED("locked"),
    CHEST_OPENED("opened");

    private final String name;

    BossBarState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;

    }
}
