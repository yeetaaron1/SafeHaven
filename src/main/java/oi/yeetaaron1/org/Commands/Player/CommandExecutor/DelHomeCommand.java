package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DelHomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;

    public DelHomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            messageSystem.sendLocalizedMessage(commandSender, "command.common.player-only");
            return true;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("safehaven.delhome")) {
            messageSystem.sendLocalizedMessage(player, "command.common.no-permission");
            return true;
        }

        if (strings.length != 1) {
            messageSystem.sendLocalizedMessage(player, "command.common.player-only");
            return true;
        }

        String homeName = strings[0];
        if (homeSystem.deleteHome(player, homeName)) {
            messageSystem.sendLocalizedMessage(player, "command.delhome.success", homeName);
        } else {
            messageSystem.sendLocalizedMessage(player, "command.delhome.not-found", homeName);
        }
        return true;
    }
}