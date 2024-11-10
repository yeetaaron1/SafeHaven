package oi.yeetaaron1.org.System.Server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import oi.yeetaaron1.org.Utils.LanguageUtil;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * Handles the sending of localized messages to command senders with optional message formatting.
 * <p>
 * This class uses a {@link LanguageUtil} instance to retrieve localized messages and a {@link ConfigUtil} instance to get the
 * plugin prefix. Messages are formatted with a prefix if configured and then sent to the {@link CommandSender} using the
 * Adventure API.
 * </p>
 *
 * @since 0.07-ALPHA
 */
public class MessageSystem {

    private final ConfigUtil configUtil;
    private final LanguageUtil languageUtil;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Constructs a new {@code MessageSystem} instance.
     *
     * @param plugin the {@link SafeHaven} plugin instance used to retrieve configuration and language utilities
     */
    public MessageSystem(SafeHaven plugin) {
        this.configUtil = plugin.getConfigUtil();
        this.languageUtil = plugin.getLanguageUtil();
    }

    /**
     * Sends a localized message to a {@link CommandSender}.
     * <p>
     * Retrieves the message from {@link LanguageUtil} using the provided message key and sends it to the {@link CommandSender}
     * with optional formatting.
     * </p>
     *
     * @param sender the {@link CommandSender} to receive the message
     * @param messageKey the key used to retrieve the localized message from the language file
     */
    public void sendLocalizedMessage(CommandSender sender, String messageKey) {
        String message = languageUtil.getMessage(messageKey);
        sendMessage(sender, message);
    }

    /**
     * Sends a localized message with placeholders replaced by specified values.
     * <p>
     * Retrieves the message from {@link LanguageUtil} using the provided message key, replaces placeholders in the message
     * with the provided values, and sends it to the {@link CommandSender}.
     * </p>
     *
     * @param sender the {@link CommandSender} to receive the message
     * @param messageKey the key used to retrieve the localized message from the language file
     * @param replacements values to replace placeholders in the message
     */
    public void sendLocalizedMessage(CommandSender sender, String messageKey, Object... replacements) {
        String message = languageUtil.getMessage(messageKey);
        for (int i = 0; i < replacements.length; i++) {
            String replacement = replacements[i].toString();
            message = message.replace("{" + i + "}", replacement);
        }
        sendMessage(sender, message);
    }

    /**
     * Retrieves a localized title message.
     * <p>
     * Gets the message from {@link LanguageUtil} using the provided key. This message is intended to be used as a title, which
     * can be sent to players.
     * </p>
     *
     * @param key the key used to retrieve the localized title message from the language file
     * @return the localized title message
     */
    public String sendLocalizedTitle(String key, Object... args) {
        String message = languageUtil.getMessage(key); // Get the localized message by key
        return MessageFormat.format(message, args); // Replace placeholders with provided arguments
    }

    /**
     * Sends a formatted message to a {@link CommandSender}.
     * <p>
     * Formats the message with the configured plugin prefix and deserializes it using MiniMessage before sending it.
     * </p>
     *
     * @param sender the {@link CommandSender} to receive the message
     * @param message the message to be sent
     */
    private void sendMessage(CommandSender sender, String message) {
        String formattedMessage = formatMessage(message);
        Component component = miniMessage.deserialize(formattedMessage);
        sender.sendMessage(component);
    }

    /**
     * Formats the message with the plugin prefix.
     * <p>
     * If a plugin prefix is configured, it is prepended to the message. Otherwise, the message is returned as is.
     * </p>
     *
     * @param message the original message
     * @return the formatted message with the prefix
     */
    private String formatMessage(String message) {
        String prefix = configUtil.getPluginPrefix();
        return prefix.isEmpty() ? message : prefix + " " + message;
    }
}
