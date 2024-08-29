package oi.yeetaaron1.org.Commands.Admin.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SafeHavenCommand implements CommandExecutor {

    private final SafeHaven plugin;
    private final ConfigUtil configUtil;
    private final LoggerUtil loggerUtil;

    public SafeHavenCommand(SafeHaven plugin) {
        this.plugin = plugin;
        this.configUtil = new ConfigUtil(plugin);
        this.loggerUtil = new LoggerUtil(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has the required permission
        if (!sender.hasPermission("safehaven.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        // Check if the sender is a player or console
        if (!(sender instanceof Player) && !sender.isOp()) {
            sender.sendMessage("This command can only be executed by a player or the console.");
            return true;
        }

        // Reload the configuration
        try {
            configUtil.reloadConfig();
            String storageMethod = configUtil.getStorageMethod();
            plugin.getLogger().info("Configuration reloaded successfully.");
            sender.sendMessage("Configuration reloaded successfully.");
            sender.sendMessage("Current storage method: " + storageMethod); // Inform user of storage method
            loggerUtil.logInfo("Configuration reloaded by " + (sender instanceof Player ? (sender).getName() : "console") + ". Storage method: " + storageMethod);
        } catch (Exception e) {
            sender.sendMessage("An error occurred while reloading the configuration.");
            loggerUtil.logError("Error reloading configuration: " + e.getMessage());
        }
        return true;
    }
}