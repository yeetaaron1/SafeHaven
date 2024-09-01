package oi.yeetaaron1.org;

import oi.yeetaaron1.org.Managers.CommandManager;
import oi.yeetaaron1.org.Managers.EventManager;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import oi.yeetaaron1.org.Utils.MongoDBUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeHaven extends JavaPlugin {

    private static LoggerUtil loggerUtil;
    private static ConfigUtil configUtil;
    private static DatabaseUtil databaseUtil;
    private static MongoDBUtil mongoDBUtil;

    @Override
    public void onEnable() {
        initializeComponents();
        logPluginInfo();
    }

    @Override
    public void onDisable() {
        shutDownPlugin();
    }

    private void initializeComponents() {
        loggerUtil = new LoggerUtil(this);
        configUtil = new ConfigUtil(this);
        setupDatabase();
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands();
        EventManager eventManager = new EventManager(this);
        eventManager.registerEvents();
    }

    private void setupDatabase() {
        loggerUtil.logInfo("Setting up database...");
        String storageMethod = configUtil.getStorageMethod();
        loggerUtil.logInfo("Storage method: " + storageMethod);

        try {
            if (storageMethod.equalsIgnoreCase("mysql")) {
                databaseUtil = new DatabaseUtil(this, storageMethod, configUtil);
                loggerUtil.logInfo("Initialized MySQL DatabaseUtil.");
                databaseUtil.setupMySQLTable();
            } else if (storageMethod.equalsIgnoreCase("sqlite")) {
                databaseUtil = new DatabaseUtil(this, storageMethod, configUtil);
                loggerUtil.logInfo("Initialized SQLite DatabaseUtil.");
                databaseUtil.setupSQLiteTable();
            } else if (storageMethod.equalsIgnoreCase("mongodb")) {
                mongoDBUtil = new MongoDBUtil(this, configUtil);
            } else {
                loggerUtil.logInfo("Using YAML storage as the configured method is not supported.");
            }
        } catch (Exception e) {
            loggerUtil.logError("Database setup error: " + e.getMessage());
        }
    }

    private void logPluginInfo() {
        loggerUtil.logInfo("Starting SafeHaven %s by %s...".formatted(
                this.getPluginMeta().getVersion(),
                this.getPluginMeta().getAuthors()
        ));
        loggerUtil.logInfo("Max homes allowed: " + configUtil.getMaxHomes());
        loggerUtil.logInfo("SafeHaven Plugin has been enabled successfully!");
    }

    private void shutDownPlugin() {
        loggerUtil.logInfo("SafeHaven Plugin is shutting down...");
        if (databaseUtil != null) {
            databaseUtil.close();
        } else if (mongoDBUtil != null) {
            mongoDBUtil.close();
        }
        loggerUtil.logInfo("SafeHaven Plugin has been disabled successfully!");
    }

    public LoggerUtil getLoggerUtil() {
        return loggerUtil;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }

    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    public MongoDBUtil getMongoDBUtil() {
        return mongoDBUtil;
    }
}