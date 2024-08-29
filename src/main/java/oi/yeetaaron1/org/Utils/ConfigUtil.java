package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class ConfigUtil {

    private final SafeHaven plugin;
    private FileConfiguration config;
    private File configFile;

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

    public int getMaxHomes() {
        return config.getInt("home-system.max-homes", 10); // Default to 10 if not set
    }

    public String getStorageMethod() {
        return config.getString("home-system.storage-method", "YAML"); // Default to YAML if not set
    }

    public String getMySQLUrl() {
        return config.getString("database.mysql.url", "jdbc:mysql://localhost:3306/your_database");
    }

    public String getMySQLUser() {
        return config.getString("database.mysql.user", "your_username");
    }

    public String getMySQLPassword() {
        return config.getString("database.mysql.password", "your_password");
    }

    public String getSQLiteFile() {
        return config.getString("database.sqlite.file", "database.db");
    }

    public File getDataFolder() {
        return plugin.getDataFolder();
    }
}