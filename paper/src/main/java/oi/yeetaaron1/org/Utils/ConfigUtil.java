package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for managing configuration file operations.
 * <p>
 * This class handles loading, saving, and reloading the plugin's configuration file.
 * It also provides methods for retrieving specific configuration values and
 * ensuring necessary directories are created.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class ConfigUtil {

    private final SafeHaven plugin;
    private FileConfiguration config;
    private final File configFile;

    /**
     * Constructs a new {@code ConfigUtil} instance and initializes the configuration file.
     *
     * @param plugin the {@link SafeHaven} plugin instance associated with this utility
     */
    public ConfigUtil(SafeHaven plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        File playerDataFolder = new File(plugin.getDataFolder(), "player");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        saveDefaultConfig();
        reloadConfig();
    }

    /**
     * Returns the current configuration.
     *
     * @return the {@link FileConfiguration} instance representing the plugin's configuration
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Reloads the configuration file from disk.
     */
    public void reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reload config file: " + e.getMessage());
        }
    }

    /**
     * Saves the current configuration to disk.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file to " + configFile.getPath() + ": " + e.getMessage());
        }
    }

    /**
     * Saves the default configuration file from the JAR if it does not already exist.
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    /**
     * Retrieves a configuration value from the file. If the value is not present,
     * the provided default value is returned.
     *
     * @param path the path to the configuration value
     * @param defaultValue the default value to return if the path is not found
     * @param <T> the type of the configuration value
     * @return the configuration value or the default value if the path is not found
     */
    public <T> T getConfigValue(String path, T defaultValue) {
        return config.contains(path) ? (T) config.get(path) : defaultValue;
    }

    /**
     * Retrieves the plugin prefix from the configuration.
     *
     * @return the prefix to use in messages, or an empty string if not enabled
     */
    public String getPluginPrefix() {
        boolean prefixEnabled = getConfigValue("messages.prefix.value", true);
        return prefixEnabled ? getConfigValue("messages.prefix.name", "<gradient:#00FF00:#00CC00>[SafeHaven]</gradient>") : "";
    }

    /**
     * Retrieves the maximum number of homes allowed from the configuration.
     *
     * @return the maximum number of homes
     */
    public int getMaxHomes() {
        return getConfigValue("home-system.max-homes", 10);
    }

    /**
     * Retrieves the storage method from the configuration.
     *
     * @return the storage method (e.g., "YAML", "MySQL", "SQLite", etc.)
     */
    public String getStorageMethod() {
        return getConfigValue("home-system.storage-method", "YAML");
    }

    /**
     * Retrieves the teleport delay in seconds from the configuration.
     *
     * @return the teleport delay in seconds
     */
    public int getTeleportDelaySeconds() {
        return getConfigValue("home-system.teleport-delay-seconds", 5);
    }

    /**
     * Retrieves the stand-still time in seconds from the configuration.
     *
     * @return the stand-still time in seconds
     */
    public int getStandStillTimeSeconds() {
        return getConfigValue("home-system.stand-still-time-seconds", 3);
    }

    /**
     * Retrieves the MySQL database URL from the configuration.
     *
     * @return the MySQL URL
     */
    public String getMySQLUrl() {
        return getConfigValue("storage.database.mysql.url", "jdbc:mysql://localhost:3306/your_database");
    }

    /**
     * Retrieves the MySQL username from the configuration.
     *
     * @return the MySQL username
     */
    public String getMySQLUser() {
        return getConfigValue("storage.database.mysql.user", "your_username");
    }

    /**
     * Retrieves the MySQL password from the configuration.
     *
     * @return the MySQL password
     */
    public String getMySQLPassword() {
        return getConfigValue("storage.database.mysql.password", "your_password");
    }

    /**
     * Retrieves the SQLite database file name from the configuration.
     *
     * @return the SQLite file name
     */
    public String getSQLiteFile() {
        return getConfigValue("storage.database.sqlite.file", "database.db");
    }

    /**
     * Retrieves the MongoDB URI from the configuration.
     *
     * @return the MongoDB URI
     */
    public String getMongoDBUri() {
        return getConfigValue("storage.database.mongodb.url", "mongodb://localhost:27017");
    }

    /**
     * Retrieves the MongoDB database name from the configuration.
     *
     * @return the MongoDB database name
     */
    public String getMongoDBName() {
        return getConfigValue("storage.database.mongodb.name", "your_database");
    }

    /**
     * Checks if Bedrock player support is enabled in the configuration.
     *
     * @return {@code true} if Bedrock player support is enabled, {@code false} otherwise
     */
    public boolean areBedrockPlayersEnabled() {
        return getConfigValue("bedrock-players.enabled", true);
    }

    /**
     * Checks if the GUI system for Bedrock players is enabled in the configuration.
     *
     * @return {@code true} if the GUI system is enabled, {@code false} otherwise
     */
    public boolean isGuiSystemEnabled() {
        return getConfigValue("bedrock-players.floodgate.guisystem", true);
    }

    /**
     * Checks if the menu system for Bedrock players is enabled in the configuration.
     *
     * @return {@code true} if the menu system is enabled, {@code false} otherwise
     */
    public boolean isMenuSystemEnabled() {
        return getConfigValue("bedrock-players.floodgate.menusystem", false);
    }

    /**
     * Returns the data folder for the plugin.
     *
     * @return the data folder {@link File}
     */
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Returns the path to the configuration file.
     *
     * @return the configuration file path
     */
    public String getConfigFilePath() {
        return configFile.getPath();
    }
}
