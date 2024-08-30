package oi.yeetaaron1.org.System.Menu.GuiSets;

import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class PlayerGuiMenu implements GuiMenu {

    private final HomeSystem homeSystem;

    public PlayerGuiMenu(HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Home Menu");
        ItemStack setHomeItem = createGuiItem(Material.BEACON, "Set Home", "Click to set a home.");
        ItemStack viewHomesItem = createGuiItem(Material.BOOK, "View Home List", "Click to view your homes.");
        ItemStack delHomeItem = createGuiItem(Material.RED_BED, "Delete Home", "Click to delete a home.");

        inventory.setItem(2, setHomeItem);
        inventory.setItem(4, viewHomesItem);
        inventory.setItem(6, delHomeItem);

        player.openInventory(inventory);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleInventoryClick(Player player, ItemStack clickedItem) {
        if (clickedItem.getType() == Material.BEACON) {
            openSetHomeGui(player);
        } else if (clickedItem.getType() == Material.BOOK) {
            openViewHomesGui(player);
        } else if (clickedItem.getType() == Material.RED_BED) {
            openDeleteHomeGui(player);
        }
    }

    private void openSetHomeGui(Player player) {
        Inventory setHomeGui = Bukkit.createInventory(null, 9, "Set Home");
        // Add items to set home GUI and open it
        player.openInventory(setHomeGui);
    }

    private void openViewHomesGui(Player player) {
        Inventory viewHomesGui = Bukkit.createInventory(null, 18, "Home List");
        List<String> homes = homeSystem.getHomes(player);
        for (int i = 0; i < homes.size(); i++) {
            ItemStack homeItem = createGuiItem(Material.PAPER, homes.get(i), "Click to teleport");
            viewHomesGui.setItem(i, homeItem);
        }
        player.openInventory(viewHomesGui);
    }

    private void openDeleteHomeGui(Player player) {
        Inventory deleteHomeGui = Bukkit.createInventory(null, 18, "Delete Home");
        List<String> homes = homeSystem.getHomes(player);
        for (int i = 0; i < homes.size(); i++) {
            ItemStack deleteItem = createGuiItem(Material.RED_BED, homes.get(i), "Click to delete");
            deleteHomeGui.setItem(i, deleteItem);
        }
        player.openInventory(deleteHomeGui);
    }
}