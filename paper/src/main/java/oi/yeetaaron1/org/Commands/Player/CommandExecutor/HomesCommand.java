package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomesCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;

    public HomesCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            messageSystem.sendLocalizedMessage(commandSender, "command.common.player-only");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.homes")) {
            messageSystem.sendLocalizedMessage(player, "command.common.no-permission");
            return true;
        }
        List<String> homes = homeSystem.getHomes(player);
        if (homes.isEmpty()) {
            messageSystem.sendLocalizedMessage(player, "command.homes.no-homes");
        } else {
            messageSystem.sendLocalizedMessage(player, "command.homes.list-header");
            for (String home : homes) {
                messageSystem.sendLocalizedMessage(player, "command.homes.list-item", home);
            }
        }
        return true;
    }
}