package endcrypt.equinox.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class HorseNBTUpdaterListener implements Listener {

    private final EquinoxEquestrian plugin;
    public HorseNBTUpdaterListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        for (Entity entity : event.getChunk().getEntities()) {
            if(!(entity instanceof AbstractHorse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse((AbstractHorse) entity)) {
                continue;
            }

            if(!(plugin.getDatabaseManager().horseExists((AbstractHorse) entity))) {
                plugin.getDatabaseManager().addHorse((AbstractHorse) entity);
            }

            AbstractHorse horse = (AbstractHorse) entity;
            new EquineLiveHorse(horse).updateDefault(horse);
        }

    }
}
