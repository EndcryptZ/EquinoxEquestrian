package endcrypt.equinox.equine.breeding.inheat;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.attributes.Gender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class EquineBreedingInHeatListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineBreedingInHeatListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void OnInHeatHorsesLoad(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(EquineUtils.getHorseGender(horse) != Gender.MARE) {
                continue;
            }

            plugin.getEquineManager().getEquineBreeding().getBreedingInHeat().getMareHorses().add(horse);

        }
    }

    @EventHandler
    public void OnInHeatHorsesUnload(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(EquineUtils.getHorseGender(horse) != Gender.MARE) {
                continue;
            }

            plugin.getEquineManager().getEquineBreeding().getBreedingInHeat().getMareHorses().remove(horse);
        }
    }

    @EventHandler
    public void OnSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        if(!EquineUtils.isLivingEquineHorse(horse)) {
            return;
        }

        if(EquineUtils.getHorseGender(horse) != Gender.MARE) {
            return;
        }

        plugin.getEquineManager().getEquineBreeding().getBreedingInHeat().getMareHorses().add(horse);
    }
}
