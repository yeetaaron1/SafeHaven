package oi.yeetaaron1.org.System.Menu;

import oi.yeetaaron1.org.SafeHaven;
import oi.yeetaaron1.org.System.Server.HomeSystem;
import oi.yeetaaron1.org.System.Server.MessageSystem;
import oi.yeetaaron1.org.System.Server.TeleportSystem;

public class GuiAdminSystem {

    private final SafeHaven plugin;
    private final HomeSystem homeSystem;
    private final TeleportSystem teleportSystem;
    private final MessageSystem messageSystem;

    public GuiAdminSystem(SafeHaven plugin, HomeSystem homeSystem){
        this.plugin = plugin;
        this.homeSystem = homeSystem;
        this.teleportSystem = new TeleportSystem(plugin, homeSystem);
        this.messageSystem = new MessageSystem(plugin);
    }


}
