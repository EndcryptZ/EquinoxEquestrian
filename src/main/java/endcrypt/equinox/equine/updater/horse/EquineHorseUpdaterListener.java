package endcrypt.equinox.equine.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

public class EquineHorseUpdaterListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineHorseUpdaterListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {

        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            plugin.getEquineManager().getEquineHorseUpdater().loadHorse(horse);
        }


    }

    @EventHandler
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            plugin.getEquineManager().getEquineHorseUpdater().saveHorse(horse, true);
        }
    }
}
