package oi.yeetaaron1.org.Commands.Player.CommandExecutor;

import oi.yeetaaron1.org.System.Menu.GuiSets.PlayerGuiMenu;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeGuiCommand implements CommandExecutor {

    private final PlayerGuiMenu playerGuiMenu;

    public HomeGuiCommand(HomeSystem homeSystem) {
        this.playerGuiMenu = new PlayerGuiMenu(homeSystem);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("safehaven.gui")) {
                player.sendMessage("You do not have permission to use this command.");
                return true;
            }
            playerGuiMenu.open(player);
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}