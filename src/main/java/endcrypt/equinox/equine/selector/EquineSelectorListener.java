package endcrypt.equinox.equine.selector;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EquineSelectorListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineSelectorListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractHorse(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) {
            return;
        }

        if(!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        plugin.getEquineManager().getEquineSelector().selectHorse(player, horse);
    }
}
