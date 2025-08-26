package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.breeding.inheat.EquineBreedingInHeat;
import endcrypt.equinox.utils.TaskUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;



public class EquineBreeding {

    private final EquinoxEquestrian plugin;
    @Getter
    private final ArrayList<AbstractHorse> breedingHorses = new ArrayList<>();
    @Getter
    private EquineBreedingInHeat breedingInHeat;

    public EquineBreeding(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineBreedingTask(plugin);
        breedingInHeat = new EquineBreedingInHeat(plugin);
        TaskUtils.runTaskLater(2, this::addAllLoadedBreedingHorses);
    }

    public void add(AbstractHorse horse) {
        breedingHorses.add(horse);
    }

    public void remove(AbstractHorse horse) {
        breedingHorses.remove(horse);
    }

    public boolean contains(AbstractHorse horse) {
        return breedingHorses.contains(horse);
    }

    public void removeAll() {
        breedingHorses.clear();
    }

    public int getBreedingCount() {
        return breedingHorses.size();
    }

    private void addAllLoadedBreedingHorses() {
        plugin.getLogger().info("[Equine] Loading all currently breeding horses from loaded worlds...");
        for (World world : Bukkit.getWorlds()) {
            for (AbstractHorse abstractHorse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(abstractHorse)) continue;

                EquineLiveHorse equineLiveHorse = new EquineLiveHorse(abstractHorse);
                if (equineLiveHorse.isBreeding()) {
                    breedingHorses.add(abstractHorse);
                }
            }
        }
        plugin.getLogger().info("[Equine] Loaded " + breedingHorses.size() + " breeding horses from loaded worlds.");
    }

}
