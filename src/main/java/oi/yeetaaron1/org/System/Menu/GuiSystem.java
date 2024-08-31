package oi.yeetaaron1.org.System.Menu;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.Player.*;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiSystem implements Listener {

    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;

    public GuiSystem(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        event.setCancelled(true);

        if (title.equals("Home Menu")) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

            switch (event.getCurrentItem().getType()) {
                case DIAMOND_SWORD:
                    new SetHomeMenu().open(player);
                    break;
                case BOOK:
                    new ListHomeMenu(homeSystem).open(player);
                    break;
                case BARRIER:
                    new DelHomeMenu(homeSystem).open(player);
                    break;
            }
        } else if (title.equals("Set Home Menu")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BOOK) {
                player.sendMessage("Name your home now!");
            } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
                new HomeMenu().open(player);
            }
        } else if (title.equals("List of Homes")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GREEN_BED) {
                String homeName = event.getCurrentItem().getItemMeta().getDisplayName();
                Location homeLocation = homeSystem.getHome(player, homeName);
                if (homeLocation != null) {
                    teleportSystem.teleportPlayer(player, homeName);
                    player.closeInventory();
                }
            } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
                new HomeMenu().open(player);
            }
        }  else if (title.equals("Delete Home Menu")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.RED_BED) {
                String homeName = event.getCurrentItem().getItemMeta().getDisplayName();
                new ConfirmDeleteHomeMenu(homeSystem, homeName).open(player);
            } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BARRIER) {
                new HomeMenu().open(player);
            }
        } else if (title.equals("Confirm Delete Home")) {
            String homeName = event.getInventory().getHolder().toString();
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GREEN_WOOL) {
                homeSystem.deleteHome(player, homeName);
                new HomeMenu().open(player);
            } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.RED_WOOL) {
                new HomeMenu().open(player);
            }
        }
    }
}