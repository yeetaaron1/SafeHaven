package oi.yeetaaron1.org.System.Server.Storage;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HomeStorage {
    void saveHome(UUID uuid, String homeName, Location location);
    Location getHome(UUID uuid, String homeName);
    boolean deleteHome(UUID uuid, String homeName);
    List<String> getHomes(UUID uuid);
}
