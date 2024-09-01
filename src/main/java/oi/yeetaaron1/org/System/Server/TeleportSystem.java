package oi.yeetaaron1.org.System.Server;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.ConfigUtil;
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

/**
 * Handles the teleportation system for players, including countdowns, visual and sound effects, and player movement checks.
 * <p>
 * This class manages the teleportation process, including visual and auditory effects, countdowns, and checks for player movement
 * during the teleportation countdown. It also interacts with the HomeSystem for retrieving home locations and the MessageSystem
 * for sending messages to players.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class TeleportSystem implements Listener {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;
    private final ConfigUtil configUtil;

    private final Set<Player> teleportQueue = new HashSet<>();

    /**
     * Constructs a new {@code TeleportSystem} instance.
     *
     * @param plugin the {@link SafeHaven} plugin instance associated with this system
     * @param homeSystem the {@link HomeSystem} instance used for retrieving home locations
     */
    public TeleportSystem(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.configUtil = plugin.getConfigUtil();
        this.messageSystem = new MessageSystem(plugin);
    }

    /**
     * Teleports a player to their specified home location after a countdown.
     * <p>
     * Adds the player to a teleport queue and starts a countdown with visual and auditory effects. If the player moves during
     * the countdown, the teleportation is canceled.
     * </p>
     *
     * @param player the {@link Player} to be teleported
     * @param homeName the name of the home location to teleport to
     */
    public void teleportPlayer(Player player, String homeName) {
        Location homeLocation = homeSystem.getHome(player, homeName);
        if (homeLocation == null) {
            messageSystem.sendLocalizedMessage(player, "teleport.doesntexist");
            return;
        }
        teleportQueue.add(player);
        new BukkitRunnable() {
            int countdown = configUtil.getTeleportDelaySeconds();
            @Override
            public void run() {
                if (!teleportQueue.contains(player)) {
                    cancel();
                    return;
                }
                if (countdown > 0) {
                    // Send the countdown as a subtitle to the teleporting player only
                    player.sendTitle(player.getName(), messageSystem.sendLocalizedTitle("teleport.countdown", countdown), 10, 70, 20);
                    player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 30, 0.5, 1, 0.5, 0.1);
                    countdown--;
                } else {
                    player.teleport(homeLocation);
                    messageSystem.sendLocalizedMessage(player, "teleport.foundhome", homeName);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation(), 50, 0.5, 1, 0.5, 0.1);
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    teleportQueue.remove(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!teleportQueue.contains(player)) {
                    cancel();
                    return;
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, configUtil.getStandStillTimeSeconds() * 20, 0, true, false, true));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (teleportQueue.contains(player)) {
                            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
                            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                        }
                    }
                }.runTaskTimer(plugin, 0, 20);
            }
        }.runTaskLater(plugin, configUtil.getStandStillTimeSeconds() * 20L);
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerMove(PlayerMoveEvent moveEvent) {
                if (teleportQueue.contains(moveEvent.getPlayer())) {
                    if (!moveEvent.getFrom().getBlock().equals(moveEvent.getTo().getBlock())) {
                        teleportQueue.remove(moveEvent.getPlayer());
                        moveEvent.getPlayer().sendMessage(messageSystem.sendLocalizedTitle("teleport.canceled"));
                    }
                }
            }
        }, plugin);
    }
}
