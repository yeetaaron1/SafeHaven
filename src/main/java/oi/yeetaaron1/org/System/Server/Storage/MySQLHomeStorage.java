package oi.yeetaaron1.org.System.Server.Storage;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MySQLHomeStorage implements HomeStorage {

    private final SafeHaven plugin;
    private final DatabaseUtil databaseUtil;
    private final LoggerUtil loggerUtil;

    public MySQLHomeStorage(SafeHaven plugin){
        this.plugin = plugin;
        this.databaseUtil = SafeHaven.getDatabaseUtil();
        this.loggerUtil = SafeHaven.getLoggerUtil();

        if (this.databaseUtil == null) {
            throw new IllegalStateException("DatabaseUtil is not initialized.");
        }
        if (this.loggerUtil == null) {
            throw new IllegalStateException("LoggerUtil is not initialized.");
        }
    }

    @Override
    public void saveHome(UUID uuid, String homeName, Location location) {
        String sql = "INSERT INTO player_homes (uuid, home_name, world, x, y, z, yaw, pitch) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE world=VALUES(world), x=VALUES(x), y=VALUES(y), z=VALUES(z), yaw=VALUES(yaw), pitch=VALUES(pitch);";
        try (PreparedStatement statement = databaseUtil.getConnection().prepareStatement(sql)){
            statement.setString(1, uuid.toString());
            statement.setString(2, homeName);
            statement.setString(3, location.getWorld().getName());
            statement.setDouble(4, location.getX());
            statement.setDouble(5, location.getY());
            statement.setDouble(6, location.getZ());
            statement.setFloat(7, location.getYaw());
            statement.setFloat(8, location.getPitch());
            statement.executeUpdate();
            loggerUtil.logInfo("Home '%s' saved for player '%s' in MySQL".formatted(homeName, uuid));
        } catch (SQLException e){
            loggerUtil.logError("Failed to save home to MySQL for player '%s' : %s".formatted(uuid, e.getMessage()));
        }
    }

    @Override
    public Location getHome(UUID uuid, String homeName) {
        String sql = "SELECT world, x, y, z, yaw, pitch FROM player_homes WHERE uuid=? AND home_name=?";
        try(PreparedStatement statement = databaseUtil.getConnection().prepareStatement(sql)){
            statement.setString(1, uuid.toString());
            statement.setString(2, homeName);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String worldName = resultSet.getString("world");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble( "y");
                double z = resultSet.getDouble("z");
                float yaw = resultSet.getFloat("yaw");
                float pitch = resultSet.getFloat("pitch");
                return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
            }
        } catch (SQLException e){
            loggerUtil.logError("Failed to retrieve home from MySQL for player '%s': %s".formatted(uuid, e.getMessage()));
        }
        return null;
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        String sql = "DELETE FROM player_homes WHERE uuid=? and home_name=?";
        try(PreparedStatement statement = databaseUtil.getConnection().prepareStatement(sql)){
            statement.setString(1, uuid.toString());
            statement.setString(2, homeName);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                loggerUtil.logInfo("Home '%s' deleted for player '%s' in YAML.".formatted(homeName, uuid));
                return true;
            }
        } catch (SQLException e){
            loggerUtil.logError("Failed to delete home from YAML for player '%s' : %s".formatted(uuid, e.getMessage()));
        }
        return false;
    }


    @Override
    public List<String> getHomes(UUID uuid) {
        List<String> homeNames = new ArrayList<>();
        String sql = "SELECT home_name FROM player_homes WHERE uuid=?";
        try(PreparedStatement statement = databaseUtil.getConnection().prepareStatement(sql)){
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                homeNames.add(resultSet.getString("home_name"));
            }
        } catch (SQLException e){
            loggerUtil.logError("Failed to retrieve homes from MySQL for player '%s': %s".formatted(uuid, e.getMessage()));
        }
        return homeNames;
    }

    @Override
    public int getHomeCount(UUID uuid) {
        String sql = "SELECT COUNT(*) FROM player_homes WHERE uuid=?";
        try (PreparedStatement statement = databaseUtil.getConnection().prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            loggerUtil.logError("Failed to retrieve home count from MySQL for player '%s': %s".formatted(uuid, e.getMessage()));
        }
        return 0;
    }
}
