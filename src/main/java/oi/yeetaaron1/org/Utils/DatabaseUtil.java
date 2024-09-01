package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for managing database connections and operations for the plugin.
 * <p>
 * This class supports both MySQL and SQLite databases. It handles establishing connections,
 * setting up tables, and executing SQL statements. The class also provides methods for
 * closing the database connection and logging relevant information and errors.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class DatabaseUtil {

    private final SafeHaven plugin;
    private final LoggerUtil loggerUtil;
    private Connection connection;
    private final String dbType;
    private final ConfigUtil configUtil;

    /**
     * Constructs a new {@code DatabaseUtil} instance and establishes a connection
     * based on the specified database type.
     *
     * @param plugin the {@link SafeHaven} plugin instance associated with this utility
     * @param dbType the type of the database to connect to ("mysql" or "sqlite")
     * @param configUtil the {@link ConfigUtil} instance for retrieving database configuration
     */
    public DatabaseUtil(SafeHaven plugin, String dbType, ConfigUtil configUtil) {
        this.plugin = plugin;
        this.loggerUtil = plugin.getLoggerUtil();
        this.dbType = dbType;
        this.configUtil = configUtil;
        connect();
    }

    /**
     * Establishes a connection to the database based on the specified database type.
     */
    private void connect() {
        try {
            if (dbType.equalsIgnoreCase("mysql")) {
                String url = configUtil.getMySQLUrl();
                String user = configUtil.getMySQLUser();
                String password = configUtil.getMySQLPassword();
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                loggerUtil.logInfo("Connected to MySQL database.");
            } else if (dbType.equalsIgnoreCase("sqlite")) {
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

    /**
     * Returns the current database connection.
     *
     * @return the {@link Connection} instance to the database
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets up the MySQL table for storing player home data. This method creates the table
     * if it does not already exist.
     */
    public void setupMySQLTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_homes (" +
                "uuid VARCHAR(36) NOT NULL," +
                "home_name VARCHAR(50) NOT NULL," +
                "world VARCHAR(50) NOT NULL," +
                "x DOUBLE NOT NULL," +
                "y DOUBLE NOT NULL," +
                "z DOUBLE NOT NULL," +
                "yaw FLOAT NOT NULL," +
                "pitch FLOAT NOT NULL," +
                "PRIMARY KEY (uuid, home_name)" +
                ");";
        executeUpdate(createTableSQL);
        loggerUtil.logInfo("MySQL table for player homes has been set up.");
    }

    /**
     * Sets up the SQLite table for storing player home data. This method creates the table
     * if it does not already exist.
     */
    public void setupSQLiteTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_homes (" +
                "uuid TEXT NOT NULL," +
                "home_name TEXT NOT NULL," +
                "world TEXT NOT NULL," +
                "x REAL NOT NULL," +
                "y REAL NOT NULL," +
                "z REAL NOT NULL," +
                "yaw REAL NOT NULL," +
                "pitch REAL NOT NULL," +
                "PRIMARY KEY (uuid, home_name)" +
                ");";
        executeUpdate(createTableSQL);
        loggerUtil.logInfo("SQLite table for player homes has been set up.");
    }

    /**
     * Executes an SQL update statement.
     *
     * @param sql the SQL statement to execute
     */
    public void executeUpdate(String sql) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            loggerUtil.logError("SQL Error: " + e.getMessage());
        }
    }

    /**
     * Closes the database connection if it is open.
     */
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
