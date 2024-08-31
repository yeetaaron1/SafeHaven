package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomesCommand implements CommandExecutor {


    private final HomeSystem homeSystem;

    public HomesCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.homes")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        List<String> homes = homeSystem.getHomes(player);
        if (homes.isEmpty()) {
            player.sendMessage("You have no homes set.");
        } else {
            player.sendMessage("Your homes:");
            for (String home : homes) {
                player.sendMessage("- " + home);
            }
        }
        return true;
    }
}
