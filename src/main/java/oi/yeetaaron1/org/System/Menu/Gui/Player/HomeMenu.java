package oi.yeetaaron1.org.System.Menu.Gui.Player;

import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HomeMenu implements GuiMenu {

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Home Menu");

        // Add border of enchanted glass panels
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 18) {
                inventory.setItem(i, createMenuItem(Material.BLACK_STAINED_GLASS_PANE, ""));
            }
        }

        // Add menu items
        inventory.setItem(11, createMenuItem(Material.DIAMOND_SWORD, "Set Home"));
        inventory.setItem(13, createMenuItem(Material.BOOK, "View Homes"));
        inventory.setItem(15, createMenuItem(Material.BARRIER, "Delete Home"));

        player.openInventory(inventory);
    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}