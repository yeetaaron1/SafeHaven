package oi.yeetaaron1.org.System.Server.Storage;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;

import java.io.File;
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

    public MySQLHomeStorage(SafeHaven plugin, DatabaseUtil databaseUtil){
        this.plugin = plugin;
        this.databaseUtil = databaseUtil;
        this.loggerUtil = new LoggerUtil(plugin);

        setupMySQLTable();
    }

    private void setupMySQLTable(){
        String createTableSQL = "CREATE TABLE IF NOT EXISTS player_homes (" +
                "uuid VARCHAR(36) NOT NULL," +
                "home_name VARCHAR(50) NOT NULL," +
                "world VARCHAR(50) NOT NULL," +
                "x DOUBLE NOT NULL," +
                "y DOUBLE NOT NULL," +
                "z DOUBLE NOT NULL," +
                "yaw FLOAT NOT NULL," +
                "pitch FLOAT NOT NULL," +
                "PRIMARY KEY (uuid, home_name)," +
                ");";
        databaseUtil.executeUpdate(createTableSQL);
        loggerUtil.logInfo("MySQL table for player homes has been set up.");
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
}
