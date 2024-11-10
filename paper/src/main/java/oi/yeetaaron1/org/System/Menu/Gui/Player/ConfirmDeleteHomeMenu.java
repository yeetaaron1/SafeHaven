package oi.yeetaaron1.org.System.Menu.Gui.Player;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.GuiMenu;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfirmDeleteHomeMenu implements GuiMenu {

    private final String homeName;
    private final MessageSystem messageSystem;

    public ConfirmDeleteHomeMenu(SafeHaven plugin, String homeName) {
        this.homeName = homeName;
        this.messageSystem = new MessageSystem(plugin);
    }

    @Override
    public void open(Player player) {
        InventoryHolder holder = new ConfirmDeleteInventoryHolder(homeName);

        Inventory inventory = Bukkit.createInventory(holder, 27, messageSystem.sendLocalizedTitle("gui.confirmdeletehome.menu"));
        for (int i = 0; i < 27; i++) {
            if (i < 9 || i % 9 == 0 || i % 9 == 8 || i >= 18) {
                inventory.setItem(i, createMenuItem(Material.BLACK_STAINED_GLASS_PANE, ""));
            }
        }

        inventory.setItem(12, createMenuItem(Material.GREEN_WOOL, messageSystem.sendLocalizedTitle("gui.confirmdeletehome.confirm")));
        inventory.setItem(14, createMenuItem(Material.RED_WOOL, messageSystem.sendLocalizedTitle("gui.confirmdeletehome.cancel")));

        player.openInventory(inventory);
    }

    private ItemStack createMenuItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private static class ConfirmDeleteInventoryHolder implements InventoryHolder {
        private final String homeName;

        public ConfirmDeleteInventoryHolder(String homeName) {
            this.homeName = homeName;
        }

        @Override
        public Inventory getInventory() {
            return null; // Not used for this holder
        }

        @Override
        public String toString() {
            return homeName;
        }
    }
}