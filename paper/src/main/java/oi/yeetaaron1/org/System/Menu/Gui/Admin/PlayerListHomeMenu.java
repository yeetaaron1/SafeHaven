package oi.yeetaaron1.org.System.Menu.Gui.Admin;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerListHomeMenu implements GuiMenu {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final Player targetPlayer;

    public PlayerListHomeMenu(SafeHaven plugin, HomeSystem homeSystem, Player targetPlayer) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.targetPlayer = targetPlayer;
    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + targetPlayer.getName() + "'s Homes");

        // Set border
        ItemStack glassPanel = createMenuItem(Material.LIGHT_BLUE_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 45) {
                inventory.setItem(i, glassPanel);
            }
        }

    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
