package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final int maxHomes;

    public SetHomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.maxHomes = plugin.getConfigUtil().getMaxHomes(); // Retrieve max homes from config
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.sethome")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        String homeName;
        if (strings.length == 0) {
            homeName = "default";
        } else if (strings.length == 1) {
            homeName = strings[0];
        } else {
            player.sendMessage("Usage: /sethome [home_name]");
            return true;
        }
        int homeCount = homeSystem.getHomeCount(player);
        if (homeCount >= maxHomes) {
            player.sendMessage("You have reached the maximum number of homes (" + maxHomes + ").");
            return true;
        }
        Location location = player.getLocation();
        homeSystem.saveHome(player, homeName, location);
        player.sendMessage("Home '" + homeName + "' has been set!");
        return true;
    }
}