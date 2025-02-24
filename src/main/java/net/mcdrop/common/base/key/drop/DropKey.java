package net.mcdrop.common.base.key.drop;

import net.lielibrary.bukkit.Plugin;
import net.mcdrop.common.base.key.item.KeyItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DropKey implements KeyItem {
    private final String id;
    private final String name;
    private final Material material;
    private final double dropChance;

    public DropKey(String id, String name, Material material, double dropChance) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.dropChance = dropChance;
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Plugin.getWithColor().hexToMinecraftColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    public double getDropChance() {
        return dropChance;
    }
}