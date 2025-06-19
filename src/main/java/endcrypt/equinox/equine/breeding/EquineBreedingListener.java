package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

public class EquineBreedingListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineBreedingListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnEntityLoad(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(!EquineUtils.isBreeding(horse)) {
                continue;
            }

            plugin.getEquineManager().getEquineBreeding().add(horse);

        }
    }

    @EventHandler
    public void OnEntityUnload(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(!EquineUtils.isBreeding(horse)) {
                continue;
            }

            plugin.getEquineManager().getEquineBreeding().remove(horse);
        }
    }
}