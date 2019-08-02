package io.github.hotlava03.collectibles.commands;

import io.github.hotlava03.collectibles.util.Colors;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class AddLoreCmd implements CommandExecutor {

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
            player.sendMessage(Colors.fixCodes("&9Usage &8&l\u00BB &7/addlore <line1> | <line2> | <lineX>"));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(" ");


        List<String> lore = Arrays.asList(Colors.fixCodes(sb.toString()).split("\\|"));

        // Removes white spaces when a user adds a space near |
        int i = 0;
        for (String line : lore) {
            if (i != 0)
                lore.set(i, line.replaceFirst(" ", ""));
            i++;
        }


        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.equals(new ItemStack(Material.AIR)) || item.getItemMeta() == null) {
            player.sendMessage(Colors.fixCodes("&cFailure &8&l\u00BB &7You must have an item in your main hand."));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);

        player.sendMessage(Colors.fixCodes("&aSuccess &8&l\u00BB &7Added the lore to the item in your main hand."));
        return true;
    }
}
