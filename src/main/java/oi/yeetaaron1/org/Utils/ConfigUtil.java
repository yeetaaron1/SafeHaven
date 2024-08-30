package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class ConfigUtil {

    private final SafeHaven plugin;
    private FileConfiguration config;
    private final File configFile;

    public ConfigUtil(SafeHaven plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(configFile);
        saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config file to " + configFile.getPath());
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    // Getter methods for home system settings
    public int getMaxHomes() {
        return config.getInt("home-system.max-homes", 10); // Default to 10 if not set
    }

    public String getStorageMethod() {
        return config.getString("home-system.storage-method", "YAML"); // Default to YAML if not set
    }

    // Getter methods for MySQL settings
    public String getMySQLUrl() {
        return config.getString("database.mysql.url", "jdbc:mysql://localhost:3306/your_database");
    }

    public String getMySQLUser() {
        return config.getString("database.mysql.user", "your_username");
    }

    public String getMySQLPassword() {
        return config.getString("database.mysql.password", "your_password");
    }

    // Getter method for SQLite settings
    public String getSQLiteFile() {
        return config.getString("database.sqlite.file", "database.db");
    }

    // Getter methods for MongoDB settings
    public String getMongoDBUri() {
        return config.getString("database.mongodb.uri", "mongodb://localhost:27017");
    }

    public String getMongoDBName() {
        return config.getString("database.mongodb.name", "your_database");
    }

    // Method to get the plugin's data folder
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    // Method to get the config file's path as a string
    public String getConfigFilePath() {
        return configFile.getPath();
    }
}