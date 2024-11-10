package oi.yeetaaron1.org.Listener.Server;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class DeathEvent implements Listener {

    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;

    public DeathEvent(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.messageSystem = plugin.getMessageSystem();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String homeName = "default";
        Location homeLocation = homeSystem.getHome(player, homeName);

        if (homeLocation == null) {
            List<String> homes = homeSystem.getHomes(player);
            if (!homes.isEmpty()) {
                homeName = homes.get(0);
                homeLocation = homeSystem.getHome(player, homeName);
            }
        }

        if (homeLocation != null) {
            event.setRespawnLocation(homeLocation);
            messageSystem.sendLocalizedMessage(player, "event.death.teleport-success", homeName);
        } else {
            messageSystem.sendLocalizedMessage(player, "event.death.no-home-found");
        }
    }
}