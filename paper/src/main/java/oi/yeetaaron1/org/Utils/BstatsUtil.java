package oi.yeetaaron1.org.Utils;

import oi.yeetaaron1.org.SafeHaven;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

// SafeHaven has the plugin id 23897
public class BstatsUtil {

    public void test(){
        int pluginId = 23897; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(JavaPlugin.getProvidingPlugin(SafeHaven.class), pluginId);

        // Optional: Add custom charts
    }
}
