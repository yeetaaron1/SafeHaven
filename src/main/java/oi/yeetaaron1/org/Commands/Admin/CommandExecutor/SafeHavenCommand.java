package oi.yeetaaron1.org.Commands.Admin.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SafeHavenCommand implements CommandExecutor {

    private final HomeSystem homeSystem;
    private final MessageSystem messageSystem;

    public SafeHavenCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("safehaven.admin")) {
            messageSystem.sendLocalizedMessage(sender, "command.common.no-permission");
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "admingui":
                if(args.length <2){
                    messageSystem.sendLocalizedMessage(sender, "command.admin.admingui.usage");
                    handleAdminGuiCommand(sender);
                }
                break;
            case "sethome":
                if (args.length < 2) {
                    messageSystem.sendLocalizedMessage(sender, "command.admin.sethome.usage");
                    return true;
                }
                handleSetHomeCommand(sender, args);
                break;
            case "home":
                if (args.length < 2) {
                    messageSystem.sendLocalizedMessage(sender, "command.admin.home.usage");
                    return true;
                }
                handleHomeCommand(sender, args);
                break;
            case "delhome":
                if (args.length < 2) {
                    messageSystem.sendLocalizedMessage(sender, "command.admin.delhome.usage");
                    return true;
                }
                handleDeleteHomeCommand(sender, args);
                break;
            case "list":
                if (args.length < 2) {
                    messageSystem.sendLocalizedMessage(sender, "command.admin.list.usage");
                    return true;
                }
                handleListHomesCommand(sender, args);
                break;
            default:
                messageSystem.sendLocalizedMessage(sender, "command.admin.common.unknown-command");
                break;
        }

        return true;
    }

    private void handleAdminGuiCommand(CommandSender sender) {
        messageSystem.sendLocalizedMessage(sender, "command.admin.admingui.success");
        // Implement GUI logic here
    }

    private void handleSetHomeCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String homeName = args.length > 2 ? args[2] : "default";
            homeSystem.saveHome(player, homeName, player.getLocation());
            messageSystem.sendLocalizedMessage(sender, "command.admin.sethome.success", homeName, player.getName());
        } else {
            messageSystem.sendLocalizedMessage(sender, "command.common.player-only");
        }
    }

    private void handleHomeCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            messageSystem.sendLocalizedMessage(sender, "command.admin.home.usage");
            return;
        }
        messageSystem.sendLocalizedMessage(sender, "command.admin.home.success", args[1], args.length > 2 ? args[2] : "default");
    }

    private void handleDeleteHomeCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            messageSystem.sendLocalizedMessage(sender, "command.admin.delhome.usage");
            return;
        }
        messageSystem.sendLocalizedMessage(sender, "command.admin.delhome.success", args[1], args.length > 2 ? args[2] : "default");
    }

    private void handleListHomesCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            messageSystem.sendLocalizedMessage(sender, "command.admin.list.usage");
            return;
        }
        messageSystem.sendLocalizedMessage(sender, "command.admin.list.success", args[1]);
    }
}