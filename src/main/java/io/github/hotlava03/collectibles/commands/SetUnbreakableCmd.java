package io.github.hotlava03.collectibles.commands;

import io.github.hotlava03.collectibles.util.Colors;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SetUnbreakableCmd implements CommandExecutor {

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

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.equals(new ItemStack(Material.AIR)) || item.getItemMeta() == null) {
            player.sendMessage(Colors.fixCodes("&cFailure &8&l\u00BB &7You must have an item in your main hand."));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);

        item.setItemMeta(meta);

        player.getInventory().setItemInMainHand(item);
        player.sendMessage(Colors.fixCodes("&aSuccess &8&l\u00BB &7Item set to &funbreakable&7."));
        return true;
    }
}
