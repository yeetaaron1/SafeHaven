package oi.yeetaaron1.org;

import oi.yeetaaron1.org.Managers.CommandManager;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeHaven extends JavaPlugin {

    private LoggerUtil loggerUtil;
    private ConfigUtil configUtil;
    private DatabaseUtil databaseUtil;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        initializeComponents();
        setupDatabase();
        logPluginInfo();
    }

    @Override
    public void onDisable() {
        shutDownPlugin();
    }

    private void initializeComponents() {
        loggerUtil = new LoggerUtil(this);
        configUtil = new ConfigUtil(this);
        commandManager = new CommandManager(this);
    }

    private void setupDatabase() {
        String storageMethod = configUtil.getStorageMethod();
        loggerUtil.logInfo("Storage method: " + storageMethod);

        if (storageMethod.equalsIgnoreCase("MySQL") || storageMethod.equalsIgnoreCase("SQLite")) {
            databaseUtil = new DatabaseUtil(this, storageMethod, configUtil);
        } else {
            loggerUtil.logInfo("Using YAML storage as the configured method is not supported.");
        }
    }

    private void logPluginInfo() {
        loggerUtil.logInfo("Starting SafeHaven %s by %s...".formatted(
                this.getPluginMeta().getVersion(),
                this.getPluginMeta().getAuthors()
        ));
        loggerUtil.logInfo("Max homes allowed: " + configUtil.getMaxHomes());

        // Example of using the database connection
        if (databaseUtil != null) {
            // Perform some database operations if necessary
            // databaseUtil.executeUpdate("CREATE TABLE IF NOT EXISTS homes (...)");
        }

        loggerUtil.logInfo("SafeHaven Plugin has been enabled successfully!");
    }

    private void shutDownPlugin() {
        loggerUtil.logInfo("SafeHaven Plugin is shutting down...");
        if (databaseUtil != null) {
            databaseUtil.close();
        }
        loggerUtil.logInfo("SafeHaven Plugin has been disabled successfully!");
    }
}