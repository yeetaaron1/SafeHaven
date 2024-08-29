package oi.yeetaaron1.org.System.Server;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.Storage.HomeStorage;
import oi.yeetaaron1.org.System.Server.Storage.MySQLHomeStorage;
import oi.yeetaaron1.org.System.Server.Storage.SqliteHomeStorage;
import oi.yeetaaron1.org.System.Server.Storage.YAMLHomeStorage;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.DatabaseUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeSystem {

    private final HomeStorage homeStorage;
    private final SafeHaven plugin;
    private final ConfigUtil configUtil;
    private final DatabaseUtil databaseUtil;

    public HomeSystem(SafeHaven plugin, ConfigUtil configUtil, DatabaseUtil databaseUtil){
        this.plugin = plugin;
        this.configUtil = configUtil;
        this.databaseUtil = databaseUtil;

        if(configUtil.getStorageMethod().equalsIgnoreCase("YAML")){
            this.homeStorage = new YAMLHomeStorage(plugin, configUtil.getDataFolder());
        } else if(configUtil.getStorageMethod().equalsIgnoreCase("MySQL")){
            this.homeStorage = new MySQLHomeStorage(plugin, databaseUtil);
        } else if(configUtil.getStorageMethod().equalsIgnoreCase("SQLite")){
            this.homeStorage = new SqliteHomeStorage(plugin, databaseUtil);
        } else {
            throw new IllegalArgumentException("Unsupported storage method: " + configUtil.getStorageMethod());
        }
    }

    public void saveHome(Player player, String homeName, Location location){
        homeStorage.saveHome(player.getUniqueId(), homeName, location);
    }

    public Location getHome(Player player, String homeName){
        return homeStorage.getHome(player.getUniqueId(), homeName);
    }

    public boolean deleteHome(Player player, String homeName){
        return homeStorage.deleteHome(player.getUniqueId(), homeName);
    }

    public List<String> getHomes(Player player){
        return homeStorage.getHomes(player.getUniqueId());
    }
}
