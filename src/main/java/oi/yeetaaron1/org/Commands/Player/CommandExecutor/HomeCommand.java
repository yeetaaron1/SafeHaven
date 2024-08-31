package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;

    public HomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.home")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (args.length > 1) {
            player.sendMessage("Usage: /home [home_name]");
            return true;
        }
        String homeName = args.length == 1 ? args[0] : null;
        if (homeName == null) {
            List<String> homes = homeSystem.getHomes(player);
            if (homes.isEmpty()) {
                player.sendMessage("You have no homes set.");
                return true;
            }
            homeName = homes.get(0); // Get the first home
        }
        Location location = homeSystem.getHome(player, homeName);
        if (location == null) {
            player.sendMessage("Home '" + homeName + "' not found.");
            return true;
        }
        teleportSystem.teleportPlayer(player, homeName);
        return true;
    }
}