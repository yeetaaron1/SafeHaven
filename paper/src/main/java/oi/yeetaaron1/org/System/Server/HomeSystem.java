package oi.yeetaaron1.org.System.Server;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.Storage.*;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages player homes using various storage methods.
 * <p>
 * This class is responsible for saving, retrieving, deleting, and listing player homes. The storage method used is determined
 * by the configuration settings and can be one of YAML, MySQL, SQLite, or MongoDB.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class HomeSystem {

    private final HomeStorage homeStorage;
    private final LoggerUtil loggerUtil;

    /**
     * Constructs a new {@code HomeSystem} instance.
     * <p>
     * Initializes the appropriate {@link HomeStorage} implementation based on the configured storage method.
     * </p>
     *
     * @param plugin the {@link SafeHaven} plugin instance used to retrieve configuration and utility instances
     */
    public HomeSystem(SafeHaven plugin) {
        this.loggerUtil = plugin.getLoggerUtil();
        ConfigUtil configUtil = plugin.getConfigUtil();
        String storageMethod = configUtil.getStorageMethod();
        DatabaseUtil databaseUtil = plugin.getDatabaseUtil();

        switch (storageMethod.toLowerCase()) {
            case "yaml":
                this.homeStorage = new YAMLHomeStorage(plugin, configUtil.getDataFolder());
                break;
            case "mysql":
                this.homeStorage = new MySQLHomeStorage(plugin, databaseUtil);
                break;
            case "sqlite":
                this.homeStorage = new SqliteHomeStorage(plugin);
                break;
            case "mongodb":
                this.homeStorage = new MongoDBHomeStorage(plugin);
                break;
            default:
                throw new IllegalArgumentException("Unsupported storage method: " + storageMethod);
        }
    }

    /**
     * Saves a home location for a player.
     * <p>
     * Attempts to save the home location for the specified player. Logs an error if the operation fails.
     * </p>
     *
     * @param player the {@link Player} whose home is being saved
     * @param homeName the name of the home to be saved
     * @param location the {@link Location} of the home to be saved
     */
    public void saveHome(Player player, String homeName, Location location) {
        try {
            homeStorage.saveHome(player.getUniqueId(), homeName, location);
        } catch (Exception e) {
            loggerUtil.logError("Failed to save home for player '%s': %s".formatted(player.getName(), e.getMessage()));
        }
    }

    /**
     * Retrieves the home location for a player.
     * <p>
     * Attempts to retrieve the home location for the specified player and home name. Logs an error if the operation fails.
     * </p>
     *
     * @param player the {@link Player} whose home is being retrieved
     * @param homeName the name of the home to retrieve
     * @return the {@link Location} of the home, or {@code null} if not found
     */
    public Location getHome(Player player, String homeName) {
        try {
            return homeStorage.getHome(player.getUniqueId(), homeName);
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return null;
        }
    }

    /**
     * Deletes a home for a player.
     * <p>
     * Attempts to delete the home for the specified player and home name. Logs an error if the operation fails.
     * </p>
     *
     * @param player the {@link Player} whose home is being deleted
     * @param homeName the name of the home to be deleted
     * @return {@code true} if the home was successfully deleted, {@code false} otherwise
     */
    public boolean deleteHome(Player player, String homeName) {
        try {
            return homeStorage.deleteHome(player.getUniqueId(), homeName);
        } catch (Exception e) {
            loggerUtil.logError("Failed to delete home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return false;
        }
    }

    /**
     * Retrieves a list of all homes for a player.
     * <p>
     * Attempts to retrieve the list of home names for the specified player. Logs an error if the operation fails.
     * </p>
     *
     * @param player the {@link Player} whose homes are being retrieved
     * @return a {@link List} of home names for the player, or an empty list if no homes are found
     */
    public List<String> getHomes(Player player) {
        try {
            return homeStorage.getHomes(player.getUniqueId());
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve homes for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves the count of homes for a player.
     * <p>
     * Attempts to retrieve the number of homes for the specified player. Logs an error if the operation fails.
     * </p>
     *
     * @param player the {@link Player} whose home count is being retrieved
     * @return the number of homes for the player
     */
    public int getHomeCount(Player player) {
        try {
            return homeStorage.getHomeCount(player.getUniqueId());
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home count for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return 0;
        }
    }
}
