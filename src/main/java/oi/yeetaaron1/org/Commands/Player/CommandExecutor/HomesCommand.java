package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomesCommand implements CommandExecutor {


    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final LoggerUtil loggerUtil;

    public HomesCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.loggerUtil = new LoggerUtil(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) commandSender;

        // Check permission
        if (!player.hasPermission("safehaven.homes")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }

        List<String> homes = /*homeSystem.getHomeStorage().getHomes(player.getUniqueId());*/

        if (homes.isEmpty()) {
            player.sendMessage("You have no homes set.");
        } else {
            player.sendMessage("Your homes:");
            for (String home : homes) {
                player.sendMessage("- " + home);
            }
        }

        loggerUtil.logInfo("Player '" + player.getName() + "' listed their homes.");
        return true;
    }
}
