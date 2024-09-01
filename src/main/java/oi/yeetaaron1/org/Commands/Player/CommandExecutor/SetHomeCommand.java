package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetHomeCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final int maxHomes;
    private final MessageSystem messageSystem;

    public SetHomeCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.maxHomes = plugin.getConfigUtil().getMaxHomes(); // Retrieve max homes from config
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            messageSystem.sendLocalizedMessage(commandSender, "command.common.player-only");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("safehaven.sethome")) {
            messageSystem.sendLocalizedMessage(player, "command.common.no-permission");
            return true;
        }
        String homeName;
        if (args.length == 0) {
            homeName = "default";
        } else if (args.length == 1) {
            homeName = args[0];
        } else {
            messageSystem.sendLocalizedMessage(player, "command.sethome.usage");
            return true;
        }
        int homeCount = homeSystem.getHomeCount(player);
        if (homeCount >= maxHomes) {
            messageSystem.sendLocalizedMessage(player, "command.sethome.max-homes", maxHomes);
            return true;
        }
        Location location = player.getLocation();
        homeSystem.saveHome(player, homeName, location);
        messageSystem.sendLocalizedMessage(player, "command.sethome.success", homeName);
        return true;
    }
}