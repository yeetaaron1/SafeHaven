package oi.yeetaaron1.org.Commands.Player.TabCompleter;

import oi.yeetaaron1.org.System.Server.HomeSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelHomeCompleter implements TabCompleter {

    private final HomeSystem homeSystem;

    public DelHomeCompleter(HomeSystem homeSystem) {
        this.homeSystem = homeSystem;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                List<String> homes = homeSystem.getHomes(player);
                completions.addAll(homes);
            }
        }
        return completions;
    }
}