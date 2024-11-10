package oi.yeetaaron1.org.System.Menu.Gui.Admin;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListMenu implements GuiMenu {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;
    private final MessageSystem messageSystem;

    public PlayerListMenu(SafeHaven plugin, HomeSystem homeSystem){
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
        this.messageSystem = new MessageSystem(plugin);
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Admin GUI");

        // Set glass panel border
        ItemStack glassPanel = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPanel.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(" ");
            glassPanel.setItemMeta(glassMeta);
        }

        for (int i = 0; i < 54; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 45) {
                inventory.setItem(i, glassPanel);
            }
        }

        // Add player heads in the center
        for (int i = 10; i < 19; i++) {
            for (int j = 0; j < 9; j++) {
                if (j >= 1 && j <= 7) {
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                    ItemMeta headMeta = playerHead.getItemMeta();
                    if (headMeta != null) {
                        headMeta.setDisplayName(ChatColor.YELLOW + "Player Head");
                        playerHead.setItemMeta(headMeta);
                    }
                    inventory.setItem(i * 9 + j, playerHead);
                }
            }
        }

        // Open the inventory for the player
        player.openInventory(inventory);
    }
}
