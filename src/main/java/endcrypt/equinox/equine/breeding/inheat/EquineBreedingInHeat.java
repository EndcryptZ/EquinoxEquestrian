package endcrypt.equinox.equine.breeding.inheat;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.equine.nbt.Keys;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

public class EquineBreedingInHeat {

    private final EquinoxEquestrian plugin;
    @Getter
    private final ArrayList<AbstractHorse> mareHorses = new ArrayList<>();

    public EquineBreedingInHeat(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineBreedingInHeatTask(plugin);
        addAllMareHorses();
    }

    private void addAllMareHorses() {
        for (World world : Bukkit.getWorlds()) {
            for (AbstractHorse abstractHorse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(abstractHorse)) continue;


                if (Keys.readPersistentData(abstractHorse, Keys.GENDER).equals(Gender.MARE)) {
                    mareHorses.add(abstractHorse);
                }
            }
        }
    }


}
