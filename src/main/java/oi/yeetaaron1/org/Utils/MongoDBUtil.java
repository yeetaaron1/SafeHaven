package oi.yeetaaron1.org.Utils;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import oi.yeetaaron1.org.SafeHaven;

public class MongoDBUtil {

    private final ConfigUtil configUtil;
    private final LoggerUtil loggerUtil;

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBUtil(SafeHaven plugin, ConfigUtil configUtil) {
        this.configUtil = configUtil;
        this.loggerUtil = new LoggerUtil(plugin);
        initializeMongoDB();
    }

    private void initializeMongoDB() {
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

    public MongoDatabase getDatabase() {
        if (isConnected()) {
            return database;
        } else {
            loggerUtil.logWarning("MongoDB connection is not established.");
            return null;
        }
    }

    public boolean isConnected() {
        return mongoClient != null && database != null;
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            loggerUtil.logInfo("MongoDB connection closed.");
        }
    }
}