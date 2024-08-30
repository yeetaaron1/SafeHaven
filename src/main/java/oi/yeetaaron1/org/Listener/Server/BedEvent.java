package oi.yeetaaron1.org.Listener.Server;

import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BedEvent implements Listener {

    private final HomeSystem homeSystem;

    public BedEvent(HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
    }

    @EventHandler
    public void onBedPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().endsWith("BED")) {
            Player player = event.getPlayer();
            String defaultHomeName = "default";
            Location location = block.getLocation();

            // Check if the player has a default home
            if (homeSystem.getHome(player, defaultHomeName) == null) {
                homeSystem.saveHome(player, defaultHomeName, location);
                player.sendMessage("Default home has been set!");
                Bukkit.getLogger().info("Default home set for player '" + player.getName() + "' at bed placement.");
            }
        }
    }

    @EventHandler
    public void onBedBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType().toString().endsWith("BED")) {
            Player player = event.getPlayer();
            String defaultHomeName = "default";

            // Check if the player has a default home
            if (homeSystem.getHome(player, defaultHomeName) != null) {
                // Check if the default home is at the broken bed's location
                if (block.getLocation().equals(homeSystem.getHome(player, defaultHomeName))) {
                    homeSystem.deleteHome(player, defaultHomeName);
                    player.sendMessage("Default home has been removed!");
                    Bukkit.getLogger().info("Default home removed for player '" + player.getName() + "' at bed break.");
                }
            }
        }
    }
}
