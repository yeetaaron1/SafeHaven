package oi.yeetaaron1.org.System.Menu.Gui.Player;

import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DelHomeMenu implements GuiMenu {

    private final HomeSystem homeSystem;

    public DelHomeMenu(HomeSystem homeSystem){
        this.homeSystem = homeSystem;
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Delete Home Menu");

        // Add border of glass panels
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 45) {
                inventory.setItem(i, createMenuItem(Material.BLACK_STAINED_GLASS_PANE, ""));
            }
        }

        // Add list of homes for deletion
        List<String> homes = homeSystem.getHomes(player);
        for (String homeName : homes) {
            inventory.addItem(createMenuItem(Material.RED_BED, homeName));
        }

        inventory.setItem(53, createMenuItem(Material.BARRIER, "Go Back"));

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