package io.github.hotlava03.collectibles.commands;

import io.github.hotlava03.collectibles.util.Colors;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AddEnchCmd implements CommandExecutor {
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

        if (args.length < 1) {
            player.sendMessage(Colors.fixCodes("&9Usage &8&l\u00BB &7/addenchant <id|-list> [level]"));
            return true;
        }
        
        if (args[0].startsWith("-"))
            if (args[0].equals("-list")) {
                player.sendMessage(Colors.fixCodes("&8----------=[ &9Enchantment ID List &8]=----------\n" +
                        "&8- &7PROTECTION_ENVIRONMENTAL\n" +
                        "&8- &7PROTECTION_FIRE\n" +
                        "&8- &7PROTECTION_FALL\n" +
                        "&8- &7PROTECTION_EXPLOSIONS\n" +
                        "&8- &7PROTECTION_PROJECTILE\n" +
                        "&8- &7OXYGEN\n" +
                        "&8- &7WATER_WORKER\n" +
                        "&8- &7THORNS\n" +
                        "&8- &7DEPTH_STRIDER\n" +
                        "&8- &7FROST_WALKER\n" +
                        "&8- &7BINDING_CURSE\n" +
                        "&8- &7DAMAGE_ALL\n" +
                        "&8- &7DAMAGE_UNDEAD\n" +
                        "&8- &7DAMAGE_ARTHROPODS\n" +
                        "&8- &7KNOCKBACK\n" +
                        "&8- &7FIRE_ASPECT\n" +
                        "&8- &7LOOT_BONUS_MOBS\n" +
                        "&8- &7SWEEPING_EDGE\n" +
                        "&8- &7DIG_SPEED\n" +
                        "&8- &7SILK_TOUCH\n" +
                        "&8- &7DURABILITY\n" +
                        "&8- &7LOOT_BONUS_BLOCKS\n" +
                        "&8- &7ARROW_DAMAGE\n" +
                        "&8- &7ARROW_KNOCKBACK\n" +
                        "&8- &7ARROW_FIRE\n" +
                        "&8- &7ARROW_INFINITE\n" +
                        "&8- &7LUCK\n" +
                        "&8- &7LURE\n" +
                        "&8- &7MENDING\n" +
                        "&8- &7VANISHING_CURSE\n" +
                        "&8-----------------------------------------"
                ));
                return true;
            }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.equals(new ItemStack(Material.AIR)) || item.getItemMeta() == null) {
            player.sendMessage(Colors.fixCodes("&cFailure &8&l\u00BB &7You must have an item in your main hand."));
            return true;
        }

        String id = args[0];
        int lvl;
        if (args.length > 1)
            try {
                lvl = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(Colors.fixCodes("&cError &8&l\u00BB &7Invalid number."));
                return true;
            }
        else
            lvl = 1;
        
        if (Enchantment.getByName(id.toUpperCase()) == null) {
            player.sendMessage(Colors.fixCodes("&cError &8&l\u00BB &7Invalid enchantment. Use &f/addenchant -list &7for a list."));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.getByName(id.toUpperCase()), lvl, true);
        item.setItemMeta(meta);
        player.getInventory().setItemInMainHand(item);
        player.sendMessage(Colors.fixCodes("&aSuccess &8&l\u00BB &7Added enchantment &f" + args[0].toUpperCase() + " &7to the item in your hand."));
        return true;
    }
}
