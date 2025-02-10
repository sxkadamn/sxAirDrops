package net.mcdrop.bukkit.commands;

import net.mcdrop.bukkit.mythical.KeysMythical;
import net.mcdrop.sxAirDrops;
import net.mcdrop.menu.LootMenu;
import net.lielibrary.bukkit.Plugin;
import net.lielibrary.bukkit.command.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands extends BaseCommand {
    public AdminCommands(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {

            sender.sendMessage(sxAirDrops.getInstance().getConfig().getString("messages.commands.command_error"));
            return;
        }

        if (args.length == 0) {

            sxAirDrops.getInstance().getConfig().getStringList("messages.commands.user-help")
                    .forEach(s -> player.sendMessage(Plugin.getWithColor().hexToMinecraftColor(s)));
            return;
        }

        if (args[0].equalsIgnoreCase("loot")) {
            new LootMenu().lootCreateMenu(player);

            player.sendMessage(Plugin.getWithColor().hexToMinecraftColor(
                    sxAirDrops.getInstance().getConfig().getString("messages.commands.loot_menu_opened")
            ));
        } else if (args[0].equalsIgnoreCase("admin")) {
            for(KeysMythical keysMythical : sxAirDrops.getInstance().getChestManager().getMythicals())
                player.getInventory().addItem(keysMythical.getItem());
        }
        else {
            sxAirDrops.getInstance().getConfig().getStringList("messages.commands.user-help")
                    .forEach(s -> player.sendMessage(Plugin.getWithColor().hexToMinecraftColor(s)));
        }
    }

}
