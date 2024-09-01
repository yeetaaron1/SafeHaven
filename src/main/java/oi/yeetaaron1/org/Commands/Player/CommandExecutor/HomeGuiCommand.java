package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.Gui.Player.HomeMenu;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeGuiCommand implements CommandExecutor {

    private final HomeMenu guiMenu;
    private final MessageSystem messageSystem;

    public HomeGuiCommand(SafeHaven plugin) {
        this.guiMenu = new HomeMenu();
        this.messageSystem = new MessageSystem(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("safehaven.gui")) {
                player.sendMessage("You do not have permission to use this command.");
                messageSystem.sendMissingPermissionMessage(player);
                return true;
            }
            guiMenu.open(player);
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}