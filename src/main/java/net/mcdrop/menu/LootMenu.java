package net.mcdrop.menu;

import net.lielibrary.AnimatedMenu;
import net.mcdrop.sxAirDrops;
import net.mcdrop.common.base.drop.DropItemsFiller;
import net.mcdrop.common.base.drop.rarity.DropItemRarity;
import net.mcdrop.common.base.impl.ChestItem;
import net.lielibrary.bukkit.Plugin;

import net.lielibrary.gui.buttons.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class LootMenu {

    public void lootCreateMenu(Player player) {
        AnimatedMenu animatedMenu =
                Plugin.getMenuManager().createMenuFromConfig(
                        Plugin.getWithColor().hexToMinecraftColor(
                                sxAirDrops.getInstance().getConfig().getString("menus.loot")),
                        3, player);

        Map<String, DropItemRarity> rarities = DropItemsFiller.getAllRarities();
        int slot = 0;
        for (String rarityKey : rarities.keySet()) {
            Button button = new Button(new ItemStack(Material.PAPER));
            button.setDisplay(Plugin.getWithColor().hexToMinecraftColor("&7" + rarityKey));
            button.withListener(event -> openEditRarityMenus(player, rarityKey));

            animatedMenu.setSlot(slot, button);
            slot++;
        }

        animatedMenu.open(player);
    }

    public void openEditRarityMenus(Player player, String rarityKey) {
        DropItemRarity rarity = DropItemsFiller.getAllRarities().get(rarityKey);

        if (rarity == null) {
            player.sendMessage(sxAirDrops.getInstance().getConfig().getString("messages.rarity_not_found"));
            return;
        }

        AnimatedMenu animatedMenu =
                Plugin.getMenuManager().createMenuFromConfig(
                        Plugin.getWithColor().hexToMinecraftColor(
                                sxAirDrops.getInstance().getConfig().getString("menus.loot_edit")
                                .replace("{rarity}", rarity.getName())),
                        3, player);

        for (ChestItem chestItem : rarity.getItems()) {
            Button button = new Button(chestItem.getItemStack());

            if (animatedMenu.getInventory().firstEmpty() != -1)
                animatedMenu.setSlot(animatedMenu.getInventory().firstEmpty(), button);
        }

        animatedMenu.setCloseListener(player1 -> {

            List<ChestItem> updatedChestItems = new ArrayList<>();

            ItemStack[] itemStacks = animatedMenu.getInventory().getStorageContents();
            for (ItemStack itemStack : itemStacks) {
                if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                    ChestItem updatedItem = new ChestItem(itemStack, itemStack.getAmount());
                    updatedChestItems.add(updatedItem);
                }
            }

            rarity.getItems().clear();
            rarity.getItems().addAll(updatedChestItems);

            DropItemsFiller.save();
        });

        animatedMenu.open(player);
    }

//    public void openRarityEditMenu(Player player, String rarityKey) {
//        DropItemRarity rarity = DropItemsFiller.getAllRarities().get(rarityKey);
//        List<ChestItem> items = rarity.getItems();
//        Menu rarityMenu = Plugin.getMenuManager().createGUI(
//                "loot_edit_" + rarityKey,
//                player.getName(),
//                Plugin.getWithColor().hexToMinecraftColor("&6Editing " + rarityKey + " Items"),
//                3,
//                false
//        );
//
//        int slot = 0;
//        for (ChestItem entry : items) {
//            ItemStack itemStack = entry.getItemStack();
//
//            Button button = new Button(itemStack);
//            button.setAmount(entry.getAmount());
//            button.withListener(event -> openItemEditMenu(player, rarityKey, entry, entry));
//
//            rarityMenu.setSlot(slot, button);
//            slot++;
//        }
//
//        player.openInventory(rarityMenu.getInventory());
//    }
//
//    public void openItemEditMenu(Player player, String rarityKey, String itemKey, ChestItem chestItem) {
//        Menu itemMenu = Plugin.getMenuManager().createGUI(
//                "item_edit_" + itemKey,
//                player.getName(),
//                Plugin.getWithColor().hexToMinecraftColor("&6Editing Item: " + chestItem.getItemStack().getType()),
//                1,
//                false
//        );
//
//        ItemStack itemStack = chestItem.getItemStack();
//        Button amountButton = new Button(itemStack);
//        amountButton.setAmount(chestItem.getAmount());
//        amountButton.withListener(event -> {
//            changeAmountMenu(player, rarityKey, itemKey, chestItem);
//        });
//
//        itemMenu.setSlot(0, amountButton);
//        player.openInventory(itemMenu.getInventory());
//    }
//
//    public void changeAmountMenu(Player player, String rarityKey, String itemKey, ChestItem chestItem) {
//        Menu amountMenu = Plugin.getMenuManager().createGUI(
//                "amount_edit_" + itemKey,
//                player.getName(),
//                Plugin.getWithColor().hexToMinecraftColor("&6Change Amount of " + chestItem.getItemStack().getType()),
//                1,
//                false
//        );
//
//        Button increaseButton = new Button(new ItemStack(Material.GREEN_DYE));
//        increaseButton.setDisplay(Plugin.getWithColor().hexToMinecraftColor("&aIncrease Amount"));
//        increaseButton.withListener(event -> {
//            chestItem.setAmount(chestItem.getAmount() + 1);
//            saveItemChange(rarityKey, itemKey, chestItem);
//        });
//
//        Button decreaseButton = new Button(new ItemStack(Material.RED_DYE));
//        decreaseButton.setDisplay(Plugin.getWithColor().hexToMinecraftColor("&cDecrease Amount"));
//            decreaseButton.withListener(event -> {
//            if (chestItem.getAmount() > 1) {
//                chestItem.setAmount(chestItem.getAmount() - 1);
//                saveItemChange(rarityKey, itemKey, chestItem);
//            }
//        });
//
//
//        amountMenu.setSlot(0, increaseButton);
//        amountMenu.setSlot(1, decreaseButton);
//        player.openInventory(amountMenu.getInventory());
//    }
//    public void saveItemChange(String rarityKey, String itemKey, ChestItem chestItem) {
//        DropItemRarity rarity = DropItemsFiller.getAllRarities().get(rarityKey);
//        Map<String, ChestItem> items = rarity.getItems();
//        items.put(itemKey, chestItem);
//
//        DropItemsFiller.updateAll(DropItemsFiller.getAllChestItems());
//        DropItemsFiller.save();
//    }


//    public void lootCreateMenu(Player player) {
//        Menu menu = Plugin.getMenuManager().createGUI(
//                "loot",
//                player.getName(),
//                Plugin.getWithColor().hexToMinecraftColor("&6LootEditor"),
//                3,
//                false
//        );
//
//        Map<String, ChestItem> chestItems = DropItemsFiller.getAllChestItems();
//
//        chestItems.forEach((key, chestItem) -> {
//            ItemStack itemStack = chestItem.getItemStack();
//            int slot = chestItem.getSlot();
//
//            if (slot < menu.getInventory().getSize()) {
//                Button button = new Button(itemStack);
//                button.setAmount(chestItem.getAmount());
//                button.setDisplay(itemStack.getItemMeta().getDisplayName());
//                menu.setSlot(slot, button);
//            }
//        });
//
//        Map<String, ChestItem> updatedChestItems = new HashMap<>();
//
//        menu.setCloseListener(player1 -> {
//            updatedChestItems.clear();
//
//            int element = 0;
//            for (int slot = 0; slot < menu.getInventory().getSize(); slot++) {
//                ItemStack stack = menu.getInventory().getItem(slot);
//                if (stack != null && stack.getType() != Material.AIR) {
//                    updatedChestItems.put("item" + element, new ChestItem(stack, slot, stack.getAmount()));
//                    element++;
//                }
//            }
//
//            DropItemsFiller.updateAll(updatedChestItems);
//            DropItemsFiller.save();
//        });
//
//        player.openInventory(menu.getInventory());
//    }

}
