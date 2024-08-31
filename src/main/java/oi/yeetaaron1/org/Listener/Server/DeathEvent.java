package oi.yeetaaron1.org.Listener.Server;

import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class DeathEvent implements Listener {

    private final HomeSystem homeSystem;

    public DeathEvent(HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String homeName = "default";

        // Try to get the home named "default"
        Location homeLocation = homeSystem.getHome(player, homeName);

        // If the "default" home doesn't exist, try to get the first home
        if (homeLocation == null) {
            // Get the list of homes
            List<String> homes = homeSystem.getHomes(player);
            if (!homes.isEmpty()) {
                homeName = homes.get(0); // Get the first home
                homeLocation = homeSystem.getHome(player, homeName);
            }
        }

        // Set the respawn location
        if (homeLocation != null) {
            event.setRespawnLocation(homeLocation);
            player.sendMessage("You have been teleported to your home: " + homeName);
        } else {
            player.sendMessage("No home found to teleport to after death.");
        }
    }
}