package endcrypt.equinox.equine.leveling;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EquineLevelingListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineLevelingListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExpPickup(PlayerPickupExperienceEvent event) {
        event.getExperienceOrb().setExperience(0);
    }

}
