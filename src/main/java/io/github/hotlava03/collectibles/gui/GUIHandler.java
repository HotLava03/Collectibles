package io.github.hotlava03.collectibles.gui;

import io.github.hotlava03.collectibles.Collectibles;
import io.github.hotlava03.collectibles.util.Colors;
import io.github.hotlava03.collectibles.util.UserInputUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GUIHandler implements Listener {

    private Player player;
    private Plugin plugin;
    private FileConfiguration config = Collectibles.config;

    public GUIHandler(Plugin plugin, Player player) {
        this.player = player;
        this.plugin = plugin;
    }

    private Inventory menu = Bukkit.createInventory(
            null,
            54,
            config.getString("server") + "'s collectibles"
    );

    public Inventory open() {

        Set<String> collectibles = getCollectibles();

        if (isNull(collectibles)) {
            player.sendMessage(Colors.fixCodes("&cError&8 &l\u00BB &7The server has no collectibles."));
            return null;
        }


        List<ItemStack> collectibleList = new ArrayList<>();
        boolean returned = false;

        for (String key : collectibles) {

            // Executes at the beginning of each collectible found
            HashMap<String, String> meta = new HashMap<>();
            HashMap<String, List<String>> metaLists = new HashMap<>();

            meta.put("item", getString(key, "item"));

            // If item is null in the collectible, consider the collectible doesn't exist
            if (isNull(meta.get("item")) || isNull(Material.getMaterial(meta.get("item")))) {
                returned = true;
                continue;
            }

            meta.put("displayName", getString(key, "displayName"));
            if (isNull(meta.get("displayName")))
                meta.put(
                        "displayName",
                        ChatColor.WHITE + Material.getMaterial(meta.get("item")).name()
                );

            List<Integer> levels = getIntList(key, "levels");

            // Not verifying if lists are null yet
            metaLists.put("lore", getStringList(key, "lore"));
            int i = 0;
            if (!metaLists.get("lore").isEmpty())
                for (String line : metaLists.get("lore"))
                    metaLists.get("lore").set(i++, line.replace("&", "\u00a7"));


            metaLists.put("enchants", getStringList(key, "enchants"));
            metaLists.put("flags", getStringList(key, "flags"));

            meta.put("unbreakable", getString(key, "unbreakable"));
            if (isNull(meta.get("unbreakable")))
                meta.put("unbreakable", "false");

            ItemStack itemStack = new ItemStack(Material.getMaterial(meta.get("item")));
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(meta.get("displayName"));
            itemMeta.setUnbreakable(Boolean.parseBoolean(meta.get("unbreakable")));
            if (!isNull(metaLists.get("lore")))
                itemMeta.setLore(metaLists.get("lore"));

            for (String flag : metaLists.get("flags")) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            i = 0;
            for (String flag : metaLists.get("enchants")) {
                try {
                    itemMeta.addEnchant(Enchantment.getByName(flag), levels.get(i++), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            itemStack.setItemMeta(itemMeta);

            collectibleList.add(itemStack);
            returned = false;
        }

        if (returned) {
            player.sendMessage(Colors.fixCodes("&cError&8 &l\u00BB &7All collectibles are invalid. They may exist, but the item ID is wrong."));
            plugin.getLogger().warning("There are invalid collectible item IDs in the collectibles file. Please fix this.");
            return null;
        }

        player.sendMessage(Colors.fixCodes("&9Collectibles&8 &l\u00BB &7Opening the collectibles menu."));

        for (ItemStack itemStack : collectibleList)
            menu.addItem(itemStack);

        return menu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equals(menu.getName()) || event.getInventory().getItem(event.getSlot()) == null)
            return;
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        if (!player.hasPermission("collectibles.admin")) return;
        UserInputUtil util = new UserInputUtil(plugin);
        util.openSignGui(player, event.getInventory().getItem(event.getSlot()));
    }

    private Set<String> getCollectibles() {
        final FileConfiguration coll = Collectibles.collectibles;
        return coll.getConfigurationSection("collectibles").getKeys(false);
    }

    private String getString(String key, String path) {
        try {
            return Collectibles.collectibles.getString("collectibles." + key + "." + path).replace("&", "\u00a7");
        } catch (NullPointerException e) {
            return null;
        }
    }

    private List<String> getStringList(String key, String path) {
        try {
            return Collectibles.collectibles.getStringList("collectibles." + key + "." + path);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private List<Integer> getIntList(String key, String path) {
        try {
            return Collectibles.collectibles.getIntegerList("collectibles." + key + "." + path);
        } catch (NullPointerException e) {
            return null;
        }
    }

    private boolean isNull(Object str) {
        return str == null;
    }
}
