package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for managing the configuration file in the SafeHaven plugin.
 */
public class ConfigUtil {

    private final SafeHaven plugin;
    private FileConfiguration config;
    private final File configFile;

    /**
     * Constructs a ConfigUtil instance.
     *
     * @param plugin The SafeHaven plugin instance.
     */
    public ConfigUtil(SafeHaven plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        File playerDataFolder = new File(plugin.getDataFolder(), "player");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
        saveDefaultConfig();
        reloadConfig(); // Load the configuration
    }

    /**
     * Gets the current configuration.
     *
     * @return The FileConfiguration object.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Reloads the configuration from the file.
     */
    public void reloadConfig() {
        try {
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to reload config file: " + e.getMessage());
        }
    }

    /**
     * Saves the current configuration to the file.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file to " + configFile.getPath() + ": " + e.getMessage());
        }
    }

    /**
     * Saves the default configuration if it does not already exist.
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    /**
     * Gets a configuration value with a default fallback.
     *
     * @param path The path to the configuration value.
     * @param defaultValue The default value to return if the path does not exist.
     * @param <T> The type of the configuration value.
     * @return The configuration value, or the default value if the path does not exist.
     */
    public <T> T getConfigValue(String path, T defaultValue) {
        return config.contains(path) ? (T) config.get(path) : defaultValue;
    }

    public String getPluginPrefix() {
        boolean prefixEnabled = getConfigValue("messages.prefix.value", true);
        return prefixEnabled ? getConfigValue("messages.prefix.name", "<gradient:#00FF00:#00CC00>[SafeHaven]</gradient>") : "";
    }

    public String getSuccessMessage() {
        return getConfigValue("messages.success.format", "<green>Operation completed successfully!</green>");
    }

    public String getMissingPermissionMessage() {
        return getConfigValue("messages.missing-permission.format", "<orange>You do not have permission to execute this command.</orange>");
    }

    public String getInfoCommandMessage() {
        return getConfigValue("messages.info-command.format", "<blue>Use /homeinfo for more details.</blue>");
    }

    public String getErrorCommandMessage() {
        return getConfigValue("messages.error-command.format", "<red>An error occurred while processing your request.</red>");
    }

    public int getMaxHomes() {
        return getConfigValue("home-system.max-homes", 10);
    }

    public String getStorageMethod() {
        return getConfigValue("home-system.storage-method", "YAML");
    }

    public int getTeleportDelaySeconds() {
        return getConfigValue("home-system.teleport-delay-seconds", 5);
    }

    public int getStandStillTimeSeconds() {
        return getConfigValue("home-system.stand-still-time-seconds", 3);
    }

    public String getMySQLUrl() {
        return getConfigValue("storage.database.mysql.url", "jdbc:mysql://localhost:3306/your_database");
    }

    public String getMySQLUser() {
        return getConfigValue("storage.database.mysql.user", "your_username");
    }

    public String getMySQLPassword() {
        return getConfigValue("storage.database.mysql.password", "your_password");
    }

    public String getSQLiteFile() {
        return getConfigValue("storage.database.sqlite.file", "database.db");
    }

    public String getMongoDBUri() {
        return getConfigValue("storage.database.mongodb.url", "mongodb://localhost:27017");
    }

    public String getMongoDBName() {
        return getConfigValue("storage.database.mongodb.name", "your_database");
    }

    public boolean areBedrockPlayersEnabled() {
        return getConfigValue("bedrock-players.enabled", true);
    }

    public String getFloodgatePath() {
        return getConfigValue("bedrock-players.floodgate.path", "plugins/Floodgate.jar");
    }

    public boolean isGuiSystemEnabled() {
        return getConfigValue("bedrock-players.floodgate.guisystem", true);
    }

    public boolean isMenuSystemEnabled() {
        return getConfigValue("bedrock-players.floodgate.menusystem", false);
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    public String getConfigFilePath() {
        return configFile.getPath();
    }
}
