package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.Managers.CommandManager;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SetHomeCommand implements CommandExecutor {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final LoggerUtil loggerUtil;

    public SetHomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.loggerUtil = new LoggerUtil(plugin);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if(!player.hasPermission("safehaven.sethome")){
            player.hasPermission("You do not have permission to use this command.");
            return true;
        }
        if(strings.length != 1){
            player.sendMessage("Usage: /sethome <home_name>");
            return true;
        }
        String homeName = strings[0];
        Location location = player.getLocation();
        //homeSystem.getHomeStorage().saveHome(player.getUniqueId(), homeName, location);
        player.sendMessage("Home '" + homeName + "' has been set!");

        loggerUtil.logInfo("Home '" + homeName + "' set for player '" + player.getName() + "'.");
        return true;
    }
}
