package oi.yeetaaron1.org.System.Server;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.Storage.*;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeSystem {

    private final HomeStorage homeStorage;
    private final SafeHaven plugin;
    private final LoggerUtil loggerUtil;

    public HomeSystem(SafeHaven plugin) {
        this.plugin = plugin;
        this.loggerUtil = SafeHaven.getLoggerUtil();
        ConfigUtil configUtil = plugin.getConfigUtil();
        String storageMethod = configUtil.getStorageMethod();

        switch (storageMethod.toUpperCase()) {
            case "YAML":
                this.homeStorage = new YAMLHomeStorage(plugin, configUtil.getDataFolder());
                break;
            case "MYSQL":
                this.homeStorage = new MySQLHomeStorage(plugin);
                break;
            case "SQLITE":
                this.homeStorage = new SqliteHomeStorage(plugin);
                break;
            case "MONGODB":
                this.homeStorage = new MongoDBHomeStorage(plugin);
                break;
            default:
                throw new IllegalArgumentException("Unsupported storage method: " + storageMethod);
        }
    }

    public void saveHome(Player player, String homeName, Location location) {
        try {
            homeStorage.saveHome(player.getUniqueId(), homeName, location);
            loggerUtil.logInfo("Home '%s' saved for player '%s'.".formatted(homeName, player.getName()));
        } catch (Exception e) {
            loggerUtil.logError("Failed to save home for player '%s': %s".formatted(player.getName(), e.getMessage()));
        }
    }

    public Location getHome(Player player, String homeName) {
        try {
            Location location = homeStorage.getHome(player.getUniqueId(), homeName);
            if (location != null) {
                loggerUtil.logInfo("Retrieved home '%s' for player '%s'.".formatted(homeName, player.getName()));
            } else {
                loggerUtil.logWarning("Home '%s' not found for player '%s'.".formatted(homeName, player.getName()));
            }
            return location;
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return null;
        }
    }

    public boolean deleteHome(Player player, String homeName) {
        try {
            boolean result = homeStorage.deleteHome(player.getUniqueId(), homeName);
            if (result) {
                loggerUtil.logInfo("Home '%s' deleted for player '%s'.".formatted(homeName, player.getName()));
            } else {
                loggerUtil.logWarning("Home '%s' not found for player '%s'.".formatted(homeName, player.getName()));
            }
            return result;
        } catch (Exception e) {
            loggerUtil.logError("Failed to delete home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return false;
        }
    }

    public List<String> getHomes(Player player) {
        try {
            List<String> homes = homeStorage.getHomes(player.getUniqueId());
            loggerUtil.logInfo("Retrieved %d homes for player '%s'.".formatted(homes.size(), player.getName()));
            return homes;
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve homes for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return new ArrayList<>();
        }
    }

    public int getHomeCount(Player player) {
        try {
            int count = homeStorage.getHomeCount(player.getUniqueId());
            loggerUtil.logInfo("Player '%s' has %d homes.".formatted(player.getName(), count));
            return count;
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home count for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return 0;
        }
    }
}