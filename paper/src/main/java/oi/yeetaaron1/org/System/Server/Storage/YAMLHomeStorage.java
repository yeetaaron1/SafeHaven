package oi.yeetaaron1.org.System.Server.Storage;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.LoggerUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class YAMLHomeStorage implements HomeStorage {

    private final SafeHaven plugin;
    private final File playerDataFolder;
    private final LoggerUtil loggerUtil;

    public YAMLHomeStorage(SafeHaven plugin, File dataFolder) {
        this.plugin = plugin;
        this.loggerUtil = plugin.getLoggerUtil();
        this.playerDataFolder = new File(dataFolder, "player");

        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    @Override
    public void saveHome(UUID uuid, String homeName, Location location) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        String path = "homes." + homeName;
        playerData.set(path + ".world", location.getWorld().getName());
        playerData.set(path + ".x", location.getX());
        playerData.set(path + ".y", location.getY());
        playerData.set(path + ".z", location.getZ());
        playerData.set(path + ".yaw", location.getYaw());
        playerData.set(path + ".pitch", location.getPitch());
        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            loggerUtil.logError("Failed to save home to YAML for player '%s' : %s".formatted(uuid, e.getMessage()));
        }
    }

    @Override
    public Location getHome(UUID uuid, String homeName) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!playerFile.exists()) {
            return null;
        }
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        String path = "homes." + homeName;
        if (!playerData.contains(path)) {
            return null;
        }
        String worldName = playerData.getString(path + ".world");
        double x = playerData.getDouble(path + ".x");
        double y = playerData.getDouble(path + ".y");
        double z = playerData.getDouble(path + ".z");
        float yaw = (float) playerData.getDouble(path + ".yaw");
        float pitch = (float) playerData.getDouble(path + ".pitch");
        return new Location(plugin.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!playerFile.exists()) {
            return false;
        }
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        String path = "homes." + homeName;
        if (playerData.contains(path)) {
            playerData.set(path, null);
            try {
                playerData.save(playerFile);
                return true;
            } catch (IOException e) {
                loggerUtil.logError("Failed to delete home from YAML for player '%s' : %s".formatted(uuid, e.getMessage()));
            }
        }
        return false;
    }

    @Override
    public List<String> getHomes(UUID uuid) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!playerFile.exists()) {
            return new ArrayList<>();
        }
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        Set<String> homeKeys = playerData.getConfigurationSection("homes").getKeys(false);
        return new ArrayList<>(homeKeys);
    }

    @Override
    public int getHomeCount(UUID uuid) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!playerFile.exists()) {
            return 0;
        }
        YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
        if (!playerData.contains("homes")) {
            return 0;
        }
        Set<String> homeKeys = playerData.getConfigurationSection("homes").getKeys(false);
        return homeKeys.size();
    }
}