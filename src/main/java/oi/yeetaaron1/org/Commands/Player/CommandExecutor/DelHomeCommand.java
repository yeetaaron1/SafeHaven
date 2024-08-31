package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final LoggerUtil loggerUtil;

    public DelHomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
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
        if (!player.hasPermission("safehaven.delhome")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (strings.length != 1) {
            player.sendMessage("Usage: /delhome <home_name>");
            return true;
        }
        String homeName = strings[0];
        if (homeSystem.deleteHome(player, homeName)) {
            player.sendMessage("Home '" + homeName + "' has been deleted!");
            loggerUtil.logInfo("Home '" + homeName + "' deleted for player '" + player.getName() + "'.");
        } else {
            player.sendMessage("Home '" + homeName + "' not found.");
        }
        return true;
    }
}