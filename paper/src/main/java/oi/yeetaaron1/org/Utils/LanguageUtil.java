package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing and retrieving localized messages from language files.
 * <p>
 * This class handles the creation of the language folder, saving default language files,
 * and loading the specified language file. It allows retrieval of messages using keys from
 * the loaded language file.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class LanguageUtil {

    private final SafeHaven plugin;
    private final Map<String, String> messages = new HashMap<>();

    /**
     * Constructs a new {@code LanguageUtil} instance and initializes the language files.
     *
     * @param plugin the {@link SafeHaven} plugin instance associated with this utility
     */
    public LanguageUtil(SafeHaven plugin) {
        this.plugin = plugin;
        createLanguageFolder();
        saveDefaultLanguageFiles();
        loadLanguageFile(plugin.getConfig().getString("messages.language", "en_us"));
    }

    /**
     * Creates the language folder in the plugin's data directory if it does not already exist.
     */
    private void createLanguageFolder() {
        File languageFolder = new File(plugin.getDataFolder(), "language");
        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }
    }

    /**
     * Saves default language files to the language folder if they do not already exist.
     */
    private void saveDefaultLanguageFiles() {
        saveLanguageFile("en_us.yml");
        saveLanguageFile("es_es.yml");
    }

    /**
     * Saves a specified language file from the JAR resource to the language folder.
     *
     * @param fileName the name of the language file to save
     */
    private void saveLanguageFile(String fileName) {
        File languageFolder = new File(plugin.getDataFolder(), "language");
        File languageFile = new File(languageFolder, fileName);

        if (!languageFile.exists()) {
            try (InputStream in = plugin.getResource("language/" + fileName);
                 OutputStream out = Files.newOutputStream(languageFile.toPath())) {
                if (in != null) {
                    Files.copy(in, languageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    plugin.getLogger().warning("Default language file not found in JAR: " + fileName);
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to save default language file: " + e.getMessage());
            }
        }
    }

    /**
     * Loads the specified language file and parses messages from it.
     *
     * @param languageCode the language code for the file to load (e.g., "en_us")
     */
    private void loadLanguageFile(String languageCode) {
        File languageFile = new File(plugin.getDataFolder(), "language" + File.separator + languageCode + ".yml");
        if (languageFile.exists()) {
            FileConfiguration languageConfig = YamlConfiguration.loadConfiguration(languageFile);
            loadMessages(languageConfig);
        } else {
            plugin.getLogger().warning("Language file not found: " + languageFile.getPath());
        }
    }

    /**
     * Loads messages from the provided {@link FileConfiguration} into the messages map.
     *
     * @param languageConfig the {@link FileConfiguration} containing the messages
     */
    private void loadMessages(FileConfiguration languageConfig) {
        for (String key : languageConfig.getKeys(true)) {
            if (languageConfig.isString(key)) {
                messages.put(key, languageConfig.getString(key));
            }
        }
    }

    /**
     * Retrieves a message by its key from the loaded language file.
     *
     * @param path the key of the message to retrieve
     * @return the localized message, or a placeholder if the key is not found
     */
    public String getMessage(String path) {
        return messages.getOrDefault(path, "Message not found: " + path);
    }
}
