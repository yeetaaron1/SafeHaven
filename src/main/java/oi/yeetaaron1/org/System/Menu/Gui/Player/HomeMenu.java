package oi.yeetaaron1.org.System.Menu.Gui.Player;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HomeMenu implements GuiMenu {

    private final MessageSystem messageSystem;

    public HomeMenu(SafeHaven plugin){
        this.messageSystem = new MessageSystem(plugin);
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27,  messageSystem.sendLocalizedTitle("gui.home.menu"));

        // Add border of enchanted glass panels
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 18) {
                inventory.setItem(i, createMenuItem(Material.BLACK_STAINED_GLASS_PANE, ""));
            }
        }

        // Add menu items
        inventory.setItem(11, createMenuItem(Material.DIAMOND_SWORD, messageSystem.sendLocalizedTitle("gui.sethome.menu")));
        inventory.setItem(13, createMenuItem(Material.BOOK, messageSystem.sendLocalizedTitle("gui.listofhomes.menu")));
        inventory.setItem(15, createMenuItem(Material.BARRIER, messageSystem.sendLocalizedTitle("gui.deletehome.menu")));

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