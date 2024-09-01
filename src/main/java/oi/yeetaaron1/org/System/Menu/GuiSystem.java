package oi.yeetaaron1.org.System.Menu;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.Player.*;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Handles interactions with GUI menus for player home management.
 * <p>
 * This class listens for inventory click events and processes interactions with GUI elements related to player homes.
 * It supports opening, setting, listing, and deleting homes through various menus.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class GuiSystem implements Listener {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;
    private final MessageSystem messageSystem;

    /**
     * Constructs a new {@code GuiSystem} instance.
     * <p>
     * Initializes the system with the provided plugin and {@link HomeSystem}. Also sets up the {@link MessageSystem}
     * and {@link TeleportSystem}.
     * </p>
     *
     * @param plugin the {@link SafeHaven} plugin instance used to retrieve configuration and utility instances
     * @param homeSystem the {@link HomeSystem} instance used for home management
     */
    public GuiSystem(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.messageSystem = new MessageSystem(plugin);
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
    }

    /**
     * Handles inventory click events.
     * <p>
     * Processes clicks in GUI inventories and triggers corresponding actions based on the menu and clicked item.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        // Check if the inventory title matches any of the GUI titles
        if (title.equals(messageSystem.sendLocalizedTitle("gui.home.menu")) ||
                title.equals(messageSystem.sendLocalizedTitle("gui.sethome.menu")) ||
                title.equals(messageSystem.sendLocalizedTitle("gui.listofhomes.menu")) ||
                title.equals(messageSystem.sendLocalizedTitle("gui.deletehome.menu")) ||
                title.equals(messageSystem.sendLocalizedTitle("gui.confirmdeletehome.menu"))) {

            event.setCancelled(true); // Cancel the event to handle custom logic

            if (title.equals(messageSystem.sendLocalizedTitle("gui.home.menu"))) {
                handleHomeMenu(event, player);
            } else if (title.equals(messageSystem.sendLocalizedTitle("gui.sethome.menu"))) {
                handleSetHomeMenu(event, player);
            } else if (title.equals(messageSystem.sendLocalizedTitle("gui.listofhomes.menu"))) {
                handleListOfHomesMenu(event, player);
            } else if (title.equals(messageSystem.sendLocalizedTitle("gui.deletehome.menu"))) {
                handleDeleteHomeMenu(event, player);
            } else if (title.equals(messageSystem.sendLocalizedTitle("gui.confirmdeletehome.menu"))) {
                handleConfirmDeleteHomeMenu(event, player);
            }
        }
    }

    /**
     * Handles interactions in the home menu.
     * <p>
     * Opens the appropriate sub-menu based on the item clicked in the home menu.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     * @param player the {@link Player} interacting with the menu
     */
    private void handleHomeMenu(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        switch (event.getCurrentItem().getType()) {
            case DIAMOND_SWORD:
                new SetHomeMenu(plugin).open(player);
                break;
            case BOOK:
                new ListHomeMenu(plugin, homeSystem).open(player);
                break;
            case BARRIER:
                new DelHomeMenu(plugin, homeSystem).open(player);
                break;
        }
    }

    /**
     * Handles interactions in the set home menu.
     * <p>
     * Prompts the user for a home name or returns to the home menu based on the item clicked.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     * @param player the {@link Player} interacting with the menu
     */
    private void handleSetHomeMenu(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BOOK) {
            messageSystem.sendLocalizedMessage(player, "gui.guisystem.sethome.nameprompt");
        } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
            new HomeMenu(plugin).open(player);
        }
    }

    /**
     * Handles interactions in the list of homes menu.
     * <p>
     * Teleports the player to the selected home or returns to the home menu based on the item clicked.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     * @param player the {@link Player} interacting with the menu
     */
    private void handleListOfHomesMenu(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GREEN_BED) {
            String homeName = event.getCurrentItem().getItemMeta().getDisplayName();
            Location homeLocation = homeSystem.getHome(player, homeName);
            if (homeLocation != null) {
                teleportSystem.teleportPlayer(player, homeName);
                player.closeInventory();
            }
        } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
            new HomeMenu(plugin).open(player);
        }
    }

    /**
     * Handles interactions in the delete home menu.
     * <p>
     * Opens a confirmation menu for deleting the selected home or returns to the home menu based on the item clicked.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     * @param player the {@link Player} interacting with the menu
     */
    private void handleDeleteHomeMenu(InventoryClickEvent event, Player player) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.RED_BED) {
            String homeName = event.getCurrentItem().getItemMeta().getDisplayName();
            new ConfirmDeleteHomeMenu(plugin, homeName).open(player);
        } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
            new HomeMenu(plugin).open(player);
        }
    }

    /**
     * Handles interactions in the confirm delete home menu.
     * <p>
     * Confirms the deletion of the home or returns to the home menu based on the item clicked.
     * </p>
     *
     * @param event the {@link InventoryClickEvent} to handle
     * @param player the {@link Player} interacting with the menu
     */
    private void handleConfirmDeleteHomeMenu(InventoryClickEvent event, Player player) {
        String homeName = event.getInventory().getHolder().toString();
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GREEN_WOOL) {
            homeSystem.deleteHome(player, homeName);
            messageSystem.sendLocalizedMessage(player, "gui.guisystem.home.deleted", homeName);
            new HomeMenu(plugin).open(player);
        } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.RED_WOOL) {
            new HomeMenu(plugin).open(player);
        }
    }
}
