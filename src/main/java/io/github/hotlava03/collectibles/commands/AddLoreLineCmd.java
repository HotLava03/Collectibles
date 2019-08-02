package io.github.hotlava03.collectibles.commands;

import io.github.hotlava03.collectibles.util.Colors;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AddLoreLineCmd implements CommandExecutor {

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
            player.sendMessage(Colors.fixCodes("&9Usage &8&l\u00BB &7/addloreline <text...|-empty|-help>"));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.equals(new ItemStack(Material.AIR)) || item.getItemMeta() == null) {
            player.sendMessage(Colors.fixCodes("&cFailure &8&l\u00BB &7You must have an item in your main hand."));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(" ");

        if (args[0].startsWith("-"))
            if (args[0].equals("-e") || args[0].equals("-empty"))
                sb = new StringBuilder().append(" ");
            else if (args[0].equals("-help")) {
                player.sendMessage(Colors.fixCodes(
                        "&8------------=[ &9Options &8]=------------\n" +
                        "&8&l\u00BB &7-help &8- &7Sends options help.\n" +
                        "&8&l\u00BB &7-empty/-e &8- &7Adds an empty lore line.\n" +
                        "&8&l\u00BB &7-- &8- &7Escapes the \"-\" character.\n" +
                        "&8-----------------------------------"
                ));
                return true;
            } else if (args[0].startsWith("--"))
                sb = new StringBuilder().append(sb.toString().replaceFirst("-", ""));
            else {
                player.sendMessage(Colors.fixCodes("&cError &8&l\u00BB &7Invalid option. For all options, use /addloreline &f-help&7."));
                return true;
            }

        List<String> lore = new ArrayList<>();
        if (!(item.getItemMeta().getLore() == null))
            lore = item.getItemMeta().getLore();

        lore.add(Colors.fixCodes(sb.toString()));

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);

        item.setItemMeta(meta);

        player.getInventory().setItemInMainHand(item);

        player.sendMessage(Colors.fixCodes("&aSuccess &8&l\u00BB &7Added a lore line to the item's lore in your main hand."));

        return true;
    }
}
