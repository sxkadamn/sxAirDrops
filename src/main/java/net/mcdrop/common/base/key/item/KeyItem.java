package net.mcdrop.common.base.key.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface KeyItem {
    String getId();
    String getName();
    Material getMaterial();
    ItemStack getItem();

    double getDropChance();
}
