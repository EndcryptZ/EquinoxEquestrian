package endcrypt.equinox.equine.thirst;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.Listener;

public class EquineThirstListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineThirstListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
