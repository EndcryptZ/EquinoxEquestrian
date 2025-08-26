package endcrypt.equinox.equine.invulnerable;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRemoveEvent;

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

    // Respawns Horses Removed by an External Plugin
    @EventHandler
    public void onHorseRemove(EntityRemoveEvent event) {
        if(event.getCause() != EntityRemoveEvent.Cause.PLUGIN) return;
        if(!(event.getEntity() instanceof AbstractHorse horse)) return;
        if(!EquineUtils.isLivingEquineHorse(horse)) return;
        if(!plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) return;
        Entity entity = event.getEntity();
        Entity copyEntity = event.getEntity().copy(event.getEntity().getLocation());
        plugin.getDatabaseManager().getDatabaseHorses().removeHorse(entity.getUniqueId());
        plugin.getDatabaseManager().getDatabaseHorses().addHorse((AbstractHorse) copyEntity);
        plugin.getLogger().warning("Equine Restore: Horse " + entity.getUniqueId() + " removed by an external plugin. Replaced with " + copyEntity.getUniqueId() + " in database.");
    }
}
