package oi.yeetaaron1.org.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


public class LoggerUtil {

    private final JavaPlugin plugin;

    public LoggerUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[SafeHaven] " + ChatColor.RESET + message);
    }

    public void logWarning(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "[SafeHaven] " + ChatColor.RESET + message);
    }

    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[SafeHaven] " + ChatColor.RESET + message);
    }
}