package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private final SafeHaven plugin;
    private final LoggerUtil loggerUtil;
    private Connection connection;
    private final String dbType;
    private final ConfigUtil configUtil;

    public DatabaseUtil(SafeHaven plugin, String dbType, ConfigUtil configUtil) {
        this.plugin = plugin;
        this.loggerUtil = new LoggerUtil(plugin);
        this.dbType = dbType;
        this.configUtil = configUtil;
        connect();
    }

    private void connect() {
        try {
            if (dbType.equalsIgnoreCase("MySQL")) {
                String url = configUtil.getMySQLUrl();
                String user = configUtil.getMySQLUser();
                String password = configUtil.getMySQLPassword();
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                loggerUtil.logInfo("Connected to MySQL database.");
            } else if (dbType.equalsIgnoreCase("SQLite")) {
                String url = "jdbc:sqlite:" + plugin.getDataFolder() + "/" + configUtil.getSQLiteFile();
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
                loggerUtil.logInfo("Connected to SQLite database.");
            } else {
                loggerUtil.logError("Unsupported database type: " + dbType);
            }
        } catch (ClassNotFoundException | SQLException e) {
            loggerUtil.logError("Database connection error: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeUpdate(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            loggerUtil.logError("SQL Error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                loggerUtil.logInfo("Database connection closed.");
            }
        } catch (SQLException e) {
            loggerUtil.logError("Error closing database connection: " + e.getMessage());
        }
    }
}