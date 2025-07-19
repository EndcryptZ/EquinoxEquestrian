package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.Listener;

public class EquineLevelingListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineLevelingListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
