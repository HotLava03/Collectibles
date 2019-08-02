package io.github.hotlava03.collectibles.commands;

import io.github.hotlava03.collectibles.util.Colors;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddFlagCmd implements CommandExecutor {

    @SuppressWarnings("Duplicates")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("[Collectibles] Only in-game players can run this.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("collectibles.admin")) {
            player.sendMessage(Colors.fixCodes("&cError &8&l\u00BB &7You do not have permission to do this."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Colors.fixCodes("&9Usage &8&l\u00BB &7/addflag <id|-list>"));
            return true;
        }

        if (args[0].startsWith("-"))
            if (args[0].equals("-list")) {
                player.sendMessage(Colors.fixCodes("&8----------=[ &9Item Flag ID List &8]=----------\n" +
                        "&8- &7HIDE_ENCHANTS\n" +
                        "&8- &7HIDE_ATTRIBUTES\n" +
                        "&8- &7HIDE_UNBREAKABLE\n" +
                        "&8- &7HIDE_DESTROYS\n" +
                        "&8- &7HIDE_PLACED_ON\n" +
                        "&8- &7HIDE_POTION_EFFECTS\n" +
                        "&8-----------------------------------------"
                ));
                return true;
            }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.equals(new ItemStack(Material.AIR)) || item.getItemMeta() == null) {
            player.sendMessage(Colors.fixCodes("&cFailure &8&l\u00BB &7You must have an item in your main hand."));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        try {
            meta.addItemFlags(ItemFlag.valueOf(args[0].toUpperCase()));
        } catch (IllegalArgumentException e) {
            player.sendMessage(Colors.fixCodes("&cError &8&l\u00BB &7Invalid flag. Use &f/addflag -list &7for a list."));
            return true;
        }

        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        player.sendMessage(Colors.fixCodes("&aSuccess &8&l\u00BB &7Added flag &f" + args[0].toUpperCase() + " &7to the item in your hand."));
        return true;
    }
}
