package oi.yeetaaron1.org.System.Server;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;


public class TeleportSystem implements Listener {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;

    private final int teleportDelaySeconds = 3; // Teleport delay in seconds
    private final int standStillTimeSeconds = 5; // Time the player must stand still

    // Store the players who are in the teleport queue
    private final Set<Player> teleportQueue = new HashSet<>();

    public TeleportSystem(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.messageSystem = new MessageSystem(plugin);
    }

    public void teleportPlayer(Player player, String homeName) {
        Location homeLocation = homeSystem.getHome(player, homeName);
        if (homeLocation == null) {
            player.sendMessage("The home \"" + homeName + "\" does not exist.");
            messageSystem.sendInfoCommandMessage(player);
            return;
        }

        teleportQueue.add(player);

        new BukkitRunnable() {
            int countdown = teleportDelaySeconds;

            @Override
            public void run() {
                if (!teleportQueue.contains(player)) {
                    cancel(); // Player moved, cancel the task
                    return;
                }

                if (countdown > 0) {
                    // Countdown message and effects
                    Bukkit.getOnlinePlayers().forEach(p ->
                            p.sendTitle(player.getName(), "Teleporting in " + countdown + "...", 10, 70, 20));
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 30, 0.5, 1, 0.5, 0.1);

                    countdown--;
                } else {
                    // Teleport the player and apply god mode
                    player.teleport(homeLocation);
                    player.sendMessage("You have been teleported to your home: " + homeName);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);

                    // Remove blindness and end the task
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    teleportQueue.remove(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Run task every 20 ticks (1 second)

        // Start the stand-still check
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!teleportQueue.contains(player)) {
                    cancel(); // Player moved, cancel the task
                    return;
                }

                // Apply blindness while waiting
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, standStillTimeSeconds * 20, 0, true, false, true));

                // Particle effect and sound
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (teleportQueue.contains(player)) {
                            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                        }
                    }
                }.runTaskTimer(plugin, 0, 20); // Run every second

            }
        }.runTaskLater(plugin, standStillTimeSeconds * 20L); // Start after the stand-still time

        // Register move event listener to cancel teleportation if the player moves
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent moveEvent) {
                if (teleportQueue.contains(moveEvent.getPlayer())) {
                    if (!moveEvent.getFrom().getBlock().equals(moveEvent.getTo().getBlock())) {
                        teleportQueue.remove(moveEvent.getPlayer());
                        moveEvent.getPlayer().sendMessage("Teleportation canceled due to movement.");
                    }
                }
            }
        }, plugin);
    }
}