package oi.yeetaaron1.org.System.Menu;

import oi.yeetaaron1.org.System.Menu.GuiSets.PlayerGuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class GuiSystem implements Listener {

    private final PlayerGuiMenu playerGuiMenu;
    private final HomeSystem homeSystem;
    private final Map<Player, String> deletionConfirmationMap = new HashMap<>();

    public GuiSystem(HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.playerGuiMenu = new PlayerGuiMenu(homeSystem);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.hasItemMeta()) {
            String inventoryTitle = event.getView().getTitle();

            if (inventoryTitle.equals("Home Menu")) {
                playerGuiMenu.handleInventoryClick(player, clickedItem);
            } else if (inventoryTitle.startsWith("Delete Home")) {
                handleDeleteHomeClick(player, clickedItem);
            } else if (inventoryTitle.equals("Confirm Delete Home")) {
                handleDeleteConfirmationClick(player, clickedItem);
            }

            event.setCancelled(true);
        }
    }

    private void handleDeleteHomeClick(Player player, ItemStack clickedItem) {
        if (clickedItem.getType() == Material.RED_BED && clickedItem.hasItemMeta()) {
            String homeName = clickedItem.getItemMeta().getDisplayName();
            deletionConfirmationMap.put(player, homeName);
            openDeleteConfirmationGui(player);
        }
    }

    private void handleDeleteConfirmationClick(Player player, ItemStack clickedItem) {
        String homeName = deletionConfirmationMap.get(player);
        if (homeName == null) return;

        if (clickedItem.getType() == Material.LIME_WOOL) {
            // Confirm deletion
            if (homeSystem.getHome(player, homeName) != null) {
                homeSystem.deleteHome(player, homeName);
                player.sendMessage("Home '" + homeName + "' has been deleted.");
            } else {
                player.sendMessage("Home '" + homeName + "' does not exist.");
            }
        } else if (clickedItem.getType() == Material.RED_WOOL) {
            // Cancel deletion
            player.sendMessage("Home deletion cancelled.");
        }
        deletionConfirmationMap.remove(player);
        player.closeInventory();
    }

    private void openDeleteConfirmationGui(Player player) {
        Inventory confirmationGui = Bukkit.createInventory(null, 9, "Confirm Delete Home");
        ItemStack confirmItem = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = confirmItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Confirm Delete");
            confirmItem.setItemMeta(meta);
        }

        ItemStack cancelItem = new ItemStack(Material.RED_WOOL);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName("Cancel");
            cancelItem.setItemMeta(cancelMeta);
        }

        confirmationGui.setItem(3, confirmItem);
        confirmationGui.setItem(5, cancelItem);

        player.openInventory(confirmationGui);
    }
}