package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.Player.HomeMenu;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeGuiCommand implements CommandExecutor {

    private final HomeMenu guiMenu;
    private final MessageSystem messageSystem;

    public HomeGuiCommand(SafeHaven plugin) {
        this.guiMenu = new HomeMenu(plugin);
        this.messageSystem = plugin.getMessageSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("safehaven.gui")) {
                messageSystem.sendLocalizedMessage(player, "command.common.no-permission");
                return true;
            }
            guiMenu.open(player);
            return true;
        } else {
            messageSystem.sendLocalizedMessage(sender, "command.common.player-only");
            return true;
        }
    }
}