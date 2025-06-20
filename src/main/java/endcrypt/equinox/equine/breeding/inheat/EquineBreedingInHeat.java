package endcrypt.equinox.equine.breeding.inheat;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

public class EquineBreedingInHeat {

    private final EquinoxEquestrian plugin;
    @Getter
    private final ArrayList<AbstractHorse> mareHorses = new ArrayList<>();

    public EquineBreedingInHeat(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineBreedingInHeatTask(plugin);
    }


}
