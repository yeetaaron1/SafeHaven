package oi.yeetaaron1.org.System.Server.Storage;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bson.Document;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MongoDBHomeStorage implements HomeStorage {

    private final SafeHaven plugin;
    private final MongoDatabase database;
    private final MongoCollection<Document> homesCollection;
    private final LoggerUtil loggerUtil;

    public MongoDBHomeStorage(SafeHaven plugin) {
        this.plugin = plugin;
        this.database = plugin.getMongoDBUtil().getDatabase();
        this.homesCollection = database.getCollection("homes");
        this.loggerUtil = plugin.getLoggerUtil();
        homesCollection.createIndex(new Document("uuid", 1).append("homeName", 1));
    }

    @Override
    public void saveHome(UUID uuid, String homeName, Location location) {
        Document homeDoc = new Document("uuid", uuid.toString())
                .append("homeName", homeName)
                .append("world", location.getWorld().getName())
                .append("x", location.getX())
                .append("y", location.getY())
                .append("z", location.getZ())
                .append("yaw", location.getYaw())
                .append("pitch", location.getPitch());

        homesCollection.replaceOne(
                new Document("uuid", uuid.toString()).append("homeName", homeName),
                homeDoc,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public Location getHome(UUID uuid, String homeName) {
        Document homeDoc = homesCollection.find(
                new Document("uuid", uuid.toString()).append("homeName", homeName)
        ).first();

        if (homeDoc != null) {
            String worldName = homeDoc.getString("world");
            double x = homeDoc.getDouble("x");
            double y = homeDoc.getDouble("y");
            double z = homeDoc.getDouble("z");
            float yaw = homeDoc.getDouble("yaw").floatValue();
            float pitch = homeDoc.getDouble("pitch").floatValue();

            return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        Document result = homesCollection.findOneAndDelete(
                new Document("uuid", uuid.toString()).append("homeName", homeName)
        );
        if (result != null) {
            return true;
        } else {
            loggerUtil.logError("Failed to delete home '%s' for player '%s' in MongoDB.".formatted(homeName, uuid));
            return false;
        }
    }

    @Override
    public List<String> getHomes(UUID uuid) {
        List<String> homes = new ArrayList<>();
        for (Document doc : homesCollection.find(new Document("uuid", uuid.toString()))) {
            homes.add(doc.getString("homeName"));
        }
        return homes;
    }

    @Override
    public int getHomeCount(UUID uuid) {
        try {
            long count = homesCollection.countDocuments(new Document("uuid", uuid.toString()));
            return (int) count;
        } catch (Exception e) {
            loggerUtil.logError("Failed to retrieve home count from MongoDB for player '%s': %s".formatted(uuid, e.getMessage()));
        }
        return 0;
    }
}