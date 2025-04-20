package endcrypt.equinoxEquestrian.horse;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EquineInvulnerable implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineInvulnerable(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHorseDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof AbstractHorse) {
            event.setCancelled(true);
        }
    }
}
