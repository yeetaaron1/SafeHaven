package oi.yeetaaron1.org.Commands.Admin.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import oi.yeetaaron1.org.Utils.MongoDBUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SafeHavenCommand implements CommandExecutor {

    private final SafeHaven plugin;
    private final ConfigUtil configUtil;
    private final LoggerUtil loggerUtil;
    private HomeSystem homeSystem;

    public SafeHavenCommand(SafeHaven plugin, HomeSystem homeSystem) {
        this.plugin = plugin;
        this.configUtil = plugin.getConfigUtil();
        this.loggerUtil = plugin.getLoggerUtil();
        this.homeSystem = homeSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("safehaven.admin")) {
            sender.sendMessage(configUtil.getMissingPermissionMessage());
            return true;
        }

        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReloadCommand(sender);
                break;
            case "admingui":
                handleAdminGuiCommand(sender);
                break;
            case "sethome":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /safehaven sethome <player> [name]");
                    return true;
                }
                handleSetHomeCommand(sender, args);
                break;
            case "home":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /safehaven home <player> [name]");
                    return true;
                }
                handleHomeCommand(sender, args);
                break;
            case "delhome":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /safehaven delhome <player> [name]");
                    return true;
                }
                handleDeleteHomeCommand(sender, args);
                break;
            case "list":
                if (args.length < 2) {
                    sender.sendMessage("Usage: /safehaven list <player>");
                    return true;
                }
                handleListHomesCommand(sender, args);
                break;
            default:
                sender.sendMessage("Unknown command. Use /safehaven help for a list of commands.");
                break;
        }

        return true;
    }

    private void handleReloadCommand(CommandSender sender) {
        try {
            configUtil.reloadConfig();
            String storageMethod = configUtil.getStorageMethod();
            updateHomeSystem(storageMethod);
            sender.sendMessage("Configuration reloaded successfully.");
            sender.sendMessage("Current storage method: " + storageMethod);
            loggerUtil.logInfo("Configuration reloaded by " + (sender instanceof Player ? ((Player) sender).getName() : "console") + ". Storage method: " + storageMethod);
        } catch (Exception e) {
            sender.sendMessage("An error occurred while reloading the configuration.");
            loggerUtil.logError("Error reloading configuration: " + e.getMessage());
        }
    }

    private void handleAdminGuiCommand(CommandSender sender) {
        sender.sendMessage("Admin GUI command executed.");
        // Implement GUI logic here
    }

    private void handleSetHomeCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String homeName = args.length > 2 ? args[2] : "default";
            homeSystem.saveHome(player, homeName, player.getLocation());
            sender.sendMessage("Home '" + homeName + "' set for player '" + player.getName() + "'.");
        } else {
            sender.sendMessage("This command can only be executed by a player.");
        }
    }

    private void handleHomeCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /safehaven home <player> [name]");
            return;
        }
        // For now, just send a message
        sender.sendMessage("Home command executed for player: " + args[1] + " with name: " + (args.length > 2 ? args[2] : "default"));
    }

    private void handleDeleteHomeCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /safehaven delhome <player> [name]");
            return;
        }
        // For now, just send a message
        sender.sendMessage("Delete home command executed for player: " + args[1] + " with name: " + (args.length > 2 ? args[2] : "default"));
    }

    private void handleListHomesCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /safehaven list <player>");
            return;
        }
        // For now, just send a message
        sender.sendMessage("List homes command executed for player: " + args[1]);
    }

    private void updateHomeSystem(String storageMethod) {
        // Handle YAML storage
        if ("YAML".equalsIgnoreCase(storageMethod)) {
            homeSystem = new HomeSystem(plugin); // Assuming HomeSystem takes a type parameter
            loggerUtil.logInfo("HomeSystem updated to use YAML storage.");
        }
        // Handle MySQL storage
        else if ("MYSQL".equalsIgnoreCase(storageMethod)) {
            // Initialize MySQL connection
            homeSystem = new HomeSystem(plugin); // You may need additional parameters like connection details
            loggerUtil.logInfo("HomeSystem updated to use MySQL storage.");
        }
        // Handle SQLite storage
        else if ("SQLITE".equalsIgnoreCase(storageMethod)) {
            // Initialize SQLite connection
            homeSystem = new HomeSystem(plugin); // You may need additional parameters like connection details
            loggerUtil.logInfo("HomeSystem updated to use SQLite storage.");
        }
        // Handle MongoDB storage
        else if ("MONGODB".equalsIgnoreCase(storageMethod)) {
            homeSystem = new HomeSystem(plugin); // Assuming HomeSystem can work with MongoDBUtil
            loggerUtil.logInfo("HomeSystem updated to use MongoDB storage.");
        }
        // Handle unknown storage methods
        else {
            loggerUtil.logWarning("Unknown storage method: " + storageMethod);
        }
    }
}