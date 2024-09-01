package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;
    private final MessageSystem messageSystem;

    public HomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            messageSystem.sendLocalizedMessage(commandSender, "command.common.player-only");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.home")) {
            messageSystem.sendLocalizedMessage(player, "command.common.no-permission");
            return true;
        }
        if (args.length > 1) {
            messageSystem.sendLocalizedMessage(player, "command.home.usage");
            return true;
        }
        String homeName = args.length == 1 ? args[0] : null;
        if (homeName == null) {
            List<String> homes = homeSystem.getHomes(player);
            if (homes.isEmpty()) {
                messageSystem.sendLocalizedMessage(player, "command.home.no-homes");
                return true;
            }
            homeName = homes.get(0);
        }
        Location location = homeSystem.getHome(player, homeName);
        if (location == null) {
            messageSystem.sendLocalizedMessage(player, "command.home.not-found", homeName);
            return true;
        }
        teleportSystem.teleportPlayer(player, homeName);
        messageSystem.sendLocalizedMessage(player, "command.home.success", homeName);
        return true;
    }
}