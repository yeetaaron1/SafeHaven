package oi.yeetaaron1.org.System.Menu;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.Admin.PlayerListHomeMenu;
import oi.yeetaaron1.org.System.Menu.Gui.Admin.PlayerListMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiAdminSystem implements Listener {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;
    private final MessageSystem messageSystem;

    public GuiAdminSystem(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.messageSystem = new MessageSystem(plugin);
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

    }

    private void handlePlayerListMenu(InventoryClickEvent event, Player player) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;


    }

    private void handlePlayerListHomeMenu(InventoryClickEvent event, Player player) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() != Material.RED_BED) return;

    }
}
