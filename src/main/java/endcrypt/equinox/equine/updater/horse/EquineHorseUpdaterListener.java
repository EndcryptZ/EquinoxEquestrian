package endcrypt.equinox.equine.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.server.PluginDisableEvent;
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
            if (!(entity instanceof AbstractHorse horse)) continue;

            plugin.getEquineManager().getEquineHorseUpdater().loadHorse(horse, UpdateAction.ENTITY_LOAD);
        }


    }

    @EventHandler
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) continue;

            plugin.getEquineManager().getEquineHorseUpdater().saveHorse(horse, UpdateAction.ENTITY_UNLOAD);
        }
    }

    @EventHandler
    public void onServerShutdown(PluginDisableEvent event) {
        if (event.getPlugin() != plugin) return;
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof AbstractHorse horse) {
                    plugin.getEquineManager().getEquineHorseUpdater().saveHorse(horse, UpdateAction.PLUGIN_DISABLE);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(EntityTeleportEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) return;

        plugin.getEquineManager().getEquineHorseUpdater().updateLastLocation(horse, event.getTo(), UpdateAction.TELEPORT);
    }
}
