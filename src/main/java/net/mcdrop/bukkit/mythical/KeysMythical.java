package net.mcdrop.bukkit.mythical;

import net.lielibrary.bukkit.Plugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KeysMythical {

    private String id;

    private String name;

    private Material material;


    public KeysMythical(String id, String name, Material material) {
        this.id = id;
        this.name = name;
        this.material = material;
    }

    public ItemStack getItem() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Plugin.getWithColor().hexToMinecraftColor(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
