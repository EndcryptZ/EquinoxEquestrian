package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.breeding.inheat.EquineBreedingInHeat;
import lombok.Getter;
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


}
