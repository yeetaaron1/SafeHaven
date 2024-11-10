package oi.yeetaaron1.org.Managers;

import oi.yeetaaron1.org.Commands.Admin.CommandExecutor.SafeHavenCommand;
import oi.yeetaaron1.org.Commands.Admin.TabCompleter.SafeHavenCompleter;
import oi.yeetaaron1.org.Commands.Player.CommandExecutor.*;
import oi.yeetaaron1.org.Commands.Player.TabCompleter.*;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;

import java.util.Objects;

public class CommandManager {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;

    public CommandManager(SafeHaven plugin) {
        this.plugin = plugin;
        this.homeSystem = new HomeSystem(plugin);
    }

    public void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("safehaven")).setExecutor(new SafeHavenCommand(plugin, homeSystem));

        Objects.requireNonNull(plugin.getCommand("sethome")).setExecutor(new SetHomeCommand(plugin, homeSystem));
        Objects.requireNonNull(plugin.getCommand("delhome")).setExecutor(new DelHomeCommand(plugin, homeSystem));
        Objects.requireNonNull(plugin.getCommand("home")).setExecutor(new HomeCommand(plugin, homeSystem));
        Objects.requireNonNull(plugin.getCommand("homes")).setExecutor(new HomesCommand(plugin, homeSystem));
        Objects.requireNonNull(plugin.getCommand("homegui")).setExecutor(new HomeGuiCommand(plugin));



        Objects.requireNonNull(plugin.getCommand("safehaven")).setTabCompleter(new SafeHavenCompleter());

        Objects.requireNonNull(plugin.getCommand("sethome")).setTabCompleter(new SetHomeCompleter());
        Objects.requireNonNull(plugin.getCommand("delhome")).setTabCompleter(new DelHomeCompleter(homeSystem));
        Objects.requireNonNull(plugin.getCommand("home")).setTabCompleter(new HomeCompleter(homeSystem));
        Objects.requireNonNull(plugin.getCommand("homes")).setTabCompleter(new HomesCompleter());
        Objects.requireNonNull(plugin.getCommand("homegui")).setTabCompleter(new HomeGuiCompleter());
        // You can add more commands here as needed
        // Example:
        // plugin.getCommand("someothercommand").setExecutor(new SomeOtherCommand(plugin));
    }
}
