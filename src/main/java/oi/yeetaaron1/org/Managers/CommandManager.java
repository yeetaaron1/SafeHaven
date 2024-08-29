package oi.yeetaaron1.org.Managers;

import oi.yeetaaron1.org.Commands.Admin.CommandExecutor.SafeHavenCommand;
import oi.yeetaaron1.org.SafeHaven;

public class CommandManager {

    private final SafeHaven plugin;

    public CommandManager(SafeHaven plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        plugin.getCommand("safehaven").setExecutor(new SafeHavenCommand(plugin));

        // You can add more commands here as needed
        // Example:
        // plugin.getCommand("someothercommand").setExecutor(new SomeOtherCommand(plugin));
    }
}
