package oi.yeetaaron1.org.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utility class for logging messages to the server console with various severity levels.
 * <p>
 * This class provides methods for logging informational, warning, and error messages,
 * as well as support for localized messages using a {@link LanguageUtil} instance.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class LoggerUtil {

    private final JavaPlugin plugin;
    private final LanguageUtil languageUtil;

    /**
     * Constructs a new {@code LoggerUtil} instance.
     *
     * @param plugin the {@link JavaPlugin} instance associated with this logger
     * @param languageUtil the {@link LanguageUtil} instance used for localized messages
     */
    public LoggerUtil(JavaPlugin plugin, LanguageUtil languageUtil) {
        this.plugin = plugin;
        this.languageUtil = languageUtil;
    }

    /**
     * Logs an informational message to the server console.
     *
     * @param message the message to log
     */
    public void logInfo(String message) {
        logMessage(ChatColor.GREEN, "info", message);
    }

    /**
     * Logs a warning message to the server console.
     *
     * @param message the message to log
     */
    public void logWarning(String message) {
        logMessage(ChatColor.YELLOW, "warning", message);
    }

    /**
     * Logs an error message to the server console.
     *
     * @param message the message to log
     */
    public void logError(String message) {
        logMessage(ChatColor.RED, "error", message);
    }

    /**
     * Logs a localized informational message to the server console.
     *
     * @param messageKey the key for the localized message
     */
    public void logLocalizedInfo(String messageKey) {
        logLocalizedMessage(ChatColor.GREEN, "info", messageKey);
    }

    /**
     * Logs a localized warning message to the server console.
     *
     * @param messageKey the key for the localized message
     */
    public void logLocalizedWarning(String messageKey) {
        logLocalizedMessage(ChatColor.YELLOW, "warning", messageKey);
    }

    /**
     * Logs a localized error message to the server console.
     *
     * @param messageKey the key for the localized message
     */
    public void logLocalizedError(String messageKey) {
        logLocalizedMessage(ChatColor.RED, "error", messageKey);
    }

    /**
     * Sends a formatted message to the server console with a specific color and type.
     *
     * @param color the color of the message
     * @param type the type of message (info, warning, error)
     * @param message the message to log
     */
    private void logMessage(ChatColor color, String type, String message) {
        Bukkit.getConsoleSender().sendMessage(color + "[" + plugin.getName() + "] " + ChatColor.RESET + message);
    }

    /**
     * Retrieves a localized message from the {@link LanguageUtil} and logs it to the server console.
     *
     * @param color the color of the message
     * @param type the type of message (info, warning, error)
     * @param messageKey the key for the localized message
     */
    private void logLocalizedMessage(ChatColor color, String type, String messageKey) {
        String localizedMessage = languageUtil.getMessage(type + "." + messageKey);
        logMessage(color, type, localizedMessage);
    }
}
