/**
 * SafeHaven is a Minecraft plugin designed to provide a comprehensive home system
 * with support for various storage methods, including YAML, MySQL, SQLite, and MongoDB.
 * It offers server administrators an array of features to manage player homes, including
 * both command-based and GUI menu systems. The plugin supports both Java and Bedrock
 * players. Bedrock players can utilize GUI menus or forms, with compatibility for Geyser
 * and Floodgate to bridge cross-platform play.
 *
 * <p>Features:</p>
 * <ul>
 *     <li>Home system management with options to set, list, teleport to, and delete homes.</li>
 *     <li>Support for multiple storage methods: YAML for file-based storage, MySQL, SQLite, and MongoDB for database storage.</li>
 *     <li>Command-based and GUI menu systems for ease of use by Java players.</li>
 *     <li>Cross-platform compatibility allowing Bedrock players to interact using either GUI menus or forms.</li>
 *     <li>Integration with Geyser and Floodgate for seamless Bedrock and Java player interaction.</li>
 * </ul>
 *
 * <p>Usage Restrictions:</p>
 * <ul>
 *     <li>Users are allowed to utilize this code for their own purposes and make modifications as needed.</li>
 *     <li>Selling this code as your own or distributing it for profit is strictly prohibited.</li>
 * </ul>
 *
 * <p>Author: yeetaaron1</p>
 * <p>Version: 0.07-ALPHA</p>
 * <p>Date: 2024-09-01</p>
 *
 * @version 0.07-ALPHA
 * @link https://github.com/yeetaaron1/SafeHaven.git
 * Copyright (c) 2024 yeetaaron1
 */
package oi.yeetaaron1.org;

import oi.yeetaaron1.org.Managers.CommandManager;
import oi.yeetaaron1.org.Managers.EventManager;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.Utils.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SafeHaven extends JavaPlugin {

    private LoggerUtil loggerUtil;
    private ConfigUtil configUtil;
    private DatabaseUtil databaseUtil;
    private MongoDBUtil mongoDBUtil;
    private LanguageUtil languageUtil;
    private MessageSystem messageSystem;

    @Override
    public void onEnable() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        initializeComponents();
        logPluginInfo();
    }

    @Override
    public void onDisable() {
        shutDownPlugin();
    }

    private void initializeComponents() {
        configUtil = new ConfigUtil(this);
        languageUtil = new LanguageUtil(this);
        loggerUtil = new LoggerUtil(this, languageUtil);
        messageSystem = new MessageSystem(this);
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
                loggerUtil.logInfo("Initialized Monogodb DatabaseUtil.");
                mongoDBUtil.initializeMongoDB();
            } else {
                loggerUtil.logInfo("Using YAML storage");
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

    // Getters
    public ConfigUtil getConfigUtil() {
        return configUtil;
    }

    public LoggerUtil getLoggerUtil() {
        return loggerUtil;
    }

    public LanguageUtil getLanguageUtil() {
        return languageUtil;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    public DatabaseUtil getDatabaseUtil() {
        return databaseUtil;
    }

    public MongoDBUtil getMongoDBUtil() {
        return mongoDBUtil;
    }

    // Setters
    public void setConfigUtil(ConfigUtil configUtil) {
        this.configUtil = configUtil;
    }

    public void setLoggerUtil(LoggerUtil loggerUtil) {
        this.loggerUtil = loggerUtil;
    }

    public void setLanguageUtil(LanguageUtil languageUtil) {
        this.languageUtil = languageUtil;
    }

    public void setMessageSystem(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    public void setDatabaseUtil(DatabaseUtil databaseUtil) {
        this.databaseUtil = databaseUtil;
    }

    public void setMongoDBUtil(MongoDBUtil mongoDBUtil) {
        this.mongoDBUtil = mongoDBUtil;
    }
}