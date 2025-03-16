package net.mcdrop.bukkit.mythical.listener;

import net.lielibrary.bukkit.requirements.RequirementAPI;
import net.mcdrop.common.base.chest.fake.ChestFake;
import net.mcdrop.common.base.key.KeyChestFiller;
import net.mcdrop.sxAirDrops;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class KeyDrop implements Listener {
    private final Random RANDOM = new Random();
    private static final Set<String> droppedKeyIds = new HashSet<>();

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Zombie zombie)) return;
        Player killer = zombie.getKiller();
        if (killer == null) return;

        KeyChestFiller.getAllKeys().values().stream()
                .filter(key -> !droppedKeyIds.contains(key.getId()))
                .findAny()
                .ifPresent(key -> {
                    if (RANDOM.nextDouble() < key.getDropChance()) {
                        killer.getInventory().addItem(new ItemStack(key.getItem()));
                        RequirementAPI.executeFromConfig(killer, "event.rewards.kill_boss");
                        Bukkit.broadcastMessage("§a" + killer.getName() + " выбил ключ: §e" + key.getName() + "!");

                        droppedKeyIds.add(key.getId());
                    }
                });
    }

    public static Set<String> getDroppedKeys() {
        return droppedKeyIds;
    }
}
