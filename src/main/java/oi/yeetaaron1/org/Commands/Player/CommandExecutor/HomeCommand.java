package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final LoggerUtil loggerUtil;

    public HomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.loggerUtil = new LoggerUtil(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.home")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (strings.length > 1) {
            player.sendMessage("Usage: /home [home_name]");
            return true;
        }
        String homeName = strings.length == 1 ? strings[0] : null;
        Location location;
        if (homeName == null) {
            List<String> homes = homeSystem.getHomes(player);
            if (homes.isEmpty()) {
                player.sendMessage("You have no homes set.");
                return true;
            }
            homeName = homes.get(0); // Get the first home
        }
        location = homeSystem.getHome(player, homeName);
        if (location == null) {
            player.sendMessage("Home '" + homeName + "' not found.");
            return true;
        }
        player.teleport(location);
        player.sendMessage("Teleported to home '" + homeName + "'!");
        loggerUtil.logInfo("Player '" + player.getName() + "' teleported to home '" + homeName + "'.");
        return true;
    }
}