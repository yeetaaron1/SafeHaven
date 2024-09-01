package oi.yeetaaron1.org.System.Server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.Utils.ConfigUtil;
import org.bukkit.entity.Player;

public class MessageSystem {

    private final ConfigUtil configUtil;
    private final MiniMessage miniMessage = MiniMessage.builder().build();

    // Predefined message formats
    private static final String SUCCESS_MESSAGE = "<green>Operation completed successfully!</green>";
    private static final String MISSING_PERMISSION_MESSAGE = "<red>You do not have permission to execute this command.</red>";
    private static final String INFO_COMMAND_MESSAGE = "<blue>Use /info for more details.</blue>";
    private static final String ERROR_COMMAND_MESSAGE = "<red>An error occurred while processing your request.</red>";

    public MessageSystem(SafeHaven plugin) {
        this.configUtil = plugin.getConfigUtil();
    }

    /**
     * Formats a message by including the prefix and applying MiniMessage formatting.
     *
     * @param message The MiniMessage formatted message.
     * @return The formatted message with prefix.
     */
    private String formatMessage(String message) {
        String prefix = configUtil.getPluginPrefix();
        if (prefix.isEmpty()) {
            return message; // No prefix, return the message as is
        } else {
            // Add the prefix to the message
            return prefix + " " + message;
        }
    }

    /**
     * Sends a success message to a player.
     *
     * @param player The player to send the message to.
     */
    public void sendSuccessMessage(Player player) {
        sendMessage(player, SUCCESS_MESSAGE);
    }

    /**
     * Sends a missing permission message to a player.
     *
     * @param player The player to send the message to.
     */
    public void sendMissingPermissionMessage(Player player) {
        sendMessage(player, MISSING_PERMISSION_MESSAGE);
    }

    /**
     * Sends an info command message to a player.
     *
     * @param player The player to send the message to.
     */
    public void sendInfoCommandMessage(Player player) {
        sendMessage(player, INFO_COMMAND_MESSAGE);
    }

    /**
     * Sends an error command message to a player.
     *
     * @param player The player to send the message to.
     */
    public void sendErrorCommandMessage(Player player) {
        sendMessage(player, ERROR_COMMAND_MESSAGE);
    }

    /**
     * Sends a custom message to a player, applying the prefix.
     *
     * @param player The player to send the message to.
     * @param customMessage The custom MiniMessage formatted message.
     */
    public void sendCustomMessage(Player player, String customMessage) {
        sendMessage(player, customMessage);
    }

    /**
     * Sends a message to a player using MiniMessage for parsing.
     *
     * @param player The player to send the message to.
     * @param message The MiniMessage formatted message.
     */
    private void sendMessage(Player player, String message) {
        String formattedMessage = formatMessage(message);
        Component component = miniMessage.deserialize(formattedMessage);
        player.sendMessage(component);
    }
}