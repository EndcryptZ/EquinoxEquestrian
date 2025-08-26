package endcrypt.equinox.equine.pregnancy;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;

public class EquinePregnancyListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquinePregnancyListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPregnancyHorsesLoad(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if (!EquineUtils.isHorsePregnant(horse)) {
                continue;
            }

            plugin.getEquineManager().getEquinePregnancy().add(horse);
        }
    }

    @EventHandler
    public void onPregnancyHorsesUnload(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if (!EquineUtils.isHorsePregnant(horse)) {
                continue;
            }

            plugin.getEquineManager().getEquinePregnancy().remove(horse);
        }
    }

}
