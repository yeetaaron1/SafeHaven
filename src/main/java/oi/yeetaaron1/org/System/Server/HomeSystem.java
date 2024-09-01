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

public class HomeSystem {

    private final HomeStorage homeStorage;
    private final LoggerUtil loggerUtil;

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

    public void saveHome(Player player, String homeName, Location location) {
        try {
            homeStorage.saveHome(player.getUniqueId(), homeName, location);
        } catch (Exception e) {
            loggerUtil.logError("Failed to save home for player '%s': %s".formatted(player.getName(), e.getMessage()));
        }
    }

    public Location getHome(Player player, String homeName) {
        try {
            return homeStorage.getHome(player.getUniqueId(), homeName);
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return null;
        }
    }

    public boolean deleteHome(Player player, String homeName) {
        try {
            return homeStorage.deleteHome(player.getUniqueId(), homeName);
        } catch (Exception e) {
            loggerUtil.logError("Failed to delete home for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return false;
        }
    }

    public List<String> getHomes(Player player) {
        try {
            return homeStorage.getHomes(player.getUniqueId());
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve homes for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return new ArrayList<>();
        }
    }

    public int getHomeCount(Player player) {
        try {
            return homeStorage.getHomeCount(player.getUniqueId());
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home count for player '%s': %s".formatted(player.getName(), e.getMessage()));
            return 0;
        }
    }
}