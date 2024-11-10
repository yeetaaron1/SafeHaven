package oi.yeetaaron1.org.Commands.Admin.TabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SafeHavenCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player) && !sender.isOp()) {
            return Collections.emptyList();
        }

        if (!command.getName().equalsIgnoreCase("safehaven")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return getCommandSuggestions(args[0]);
        } else if (args.length == 2) {
            if (Arrays.asList("sethome", "home", "delhome", "list").contains(args[0].toLowerCase())) {
                return getPlayerSuggestions(args[1]);
            }
        } else if (args.length == 3) {
            if (Arrays.asList("sethome", "home", "delhome").contains(args[0].toLowerCase())) {
                Player player = (Player) sender;
                return getHomeNameSuggestions(player, args[2]);
            }
        }

        return Collections.emptyList();
    }

    private List<String> getCommandSuggestions(String currentArg) {
        List<String> commands = Arrays.asList("reload", "admingui", "sethome", "home", "delhome", "list");
        return getMatchingSuggestions(commands, currentArg);
    }

    private List<String> getPlayerSuggestions(String currentArg) {
        List<String> playerNames = new ArrayList<>();
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            playerNames.add(player.getName());
        }
        return getMatchingSuggestions(playerNames, currentArg);
    }

    private List<String> getHomeNameSuggestions(Player player, String currentArg) {
        List<String> homeNames = new ArrayList<>();
        // You need to implement the logic to get home names for the player
        // For example:
        // homeNames.addAll(homeSystem.getHomes(player)); // Assuming homeSystem is available here
        return getMatchingSuggestions(homeNames, currentArg);
    }

    private List<String> getMatchingSuggestions(List<String> options, String currentArg) {
        List<String> suggestions = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(currentArg.toLowerCase())) {
                suggestions.add(option);
            }
        }
        return suggestions;
    }
}