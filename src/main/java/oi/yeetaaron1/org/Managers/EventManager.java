package oi.yeetaaron1.org.Managers;

import oi.yeetaaron1.org.Listener.Server.BedEvent;
import oi.yeetaaron1.org.Listener.Server.DeathEvent;
import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Menu.GuiSystem;
import oi.yeetaaron1.org.System.Server.HomeSystem;

public class EventManager {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;

    public EventManager(SafeHaven plugin) {
        this.plugin = plugin;
        this.homeSystem = new HomeSystem(plugin);
    }

    public void registerEvents() {
        plugin.getServer().getPluginManager().registerEvents(new BedEvent(homeSystem), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GuiSystem(plugin, homeSystem), plugin);
        plugin.getServer().getPluginManager().registerEvents(new DeathEvent(plugin, homeSystem), plugin);
        // Add other event listeners as needed
    }
}
