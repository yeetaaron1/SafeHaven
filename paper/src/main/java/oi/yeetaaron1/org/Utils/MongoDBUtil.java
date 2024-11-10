package oi.yeetaaron1.org.Utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import oi.yeetaaron1.org.SafeHaven;

/**
 * Utility class for managing MongoDB connections.
 * <p>
 * This class handles the connection to a MongoDB database using configuration
 * settings provided by the {@link ConfigUtil} class. It provides methods for
 * initializing the connection, retrieving the database, checking the connection
 * status, and closing the connection.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class MongoDBUtil {

    private final ConfigUtil configUtil;
    private final LoggerUtil loggerUtil;

    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Constructs a new {@code MongoDBUtil} instance.
     *
     * @param plugin the {@link SafeHaven} plugin instance
     * @param configUtil the {@link ConfigUtil} instance for retrieving MongoDB configuration
     */
    public MongoDBUtil(SafeHaven plugin, ConfigUtil configUtil) {
        this.configUtil = configUtil;
        this.loggerUtil = plugin.getLoggerUtil();
    }

    /**
     * Initializes the connection to MongoDB using the URI and database name
     * from the configuration.
     * <p>
     * This method creates a connection to MongoDB and logs the connection status.
     * If the connection fails, an error is logged.
     * </p>
     */
    public void initializeMongoDB() {
        String uri = configUtil.getMongoDBUri();
        String dbName = configUtil.getMongoDBName();

        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            this.mongoClient = MongoClients.create(settings);
            this.database = mongoClient.getDatabase(dbName);
            loggerUtil.logInfo("Connected to MongoDB database: " + dbName);
        } catch (Exception e) {
            loggerUtil.logError("Failed to connect to MongoDB: " + e.getMessage());
        }
    }

    /**
     * Returns the MongoDB database instance.
     *
     * @return the {@link MongoDatabase} instance if connected, or {@code null} if not connected
     */
    public MongoDatabase getDatabase() {
        if (isConnected()) {
            return database;
        } else {
            loggerUtil.logWarning("MongoDB connection is not established.");
            return null;
        }
    }

    /**
     * Checks if the connection to MongoDB is established.
     *
     * @return {@code true} if connected, {@code false} otherwise
     */
    public boolean isConnected() {
        return mongoClient != null && database != null;
    }

    /**
     * Closes the connection to MongoDB.
     * <p>
     * This method closes the MongoDB client and logs the connection closure.
     * </p>
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            loggerUtil.logInfo("MongoDB connection closed.");
        }
    }
}
