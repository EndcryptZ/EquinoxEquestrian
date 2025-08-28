package endcrypt.equinox.equine.breeding.inheat;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

public class EquineBreedingInHeatTask {

    private final EquinoxEquestrian plugin;
    public EquineBreedingInHeatTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkInHeat, 20L, 20L);
    }

    private void checkInHeat() {
        for (AbstractHorse horse : new ArrayList<>(plugin.getEquineManager().getEquineBreeding().getBreedingInHeat().getMareHorses())) {
            if(horse == null) continue;
            if(Keys.readPersistentData(horse, Keys.GENDER) != Gender.MARE) continue;
            EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
            checkHorseInHeat(liveHorse);
        }
    }


    private void checkHorseInHeat(EquineLiveHorse liveHorse) {
        long calculatedEndInHeat = liveHorse.getLastInHeat() + TimeUtils.daysToMillis(3);
        long calculatedNextInHeat = liveHorse.getLastInHeat() + TimeUtils.daysToMillis(28);

        if (liveHorse.getAge() < 3) {
            liveHorse.setInHeat(false);
            liveHorse.update();
            return;
        }
        if (liveHorse.getAge() > 12) {
            liveHorse.setInHeat(false);
            liveHorse.update();
            return;
        }

        if (liveHorse.isInHeat()) {
            if (calculatedEndInHeat < System.currentTimeMillis()) {
                liveHorse.setInHeat(false);
                liveHorse.update();
                return;
            }
        }

        if (calculatedNextInHeat > System.currentTimeMillis()) return;
        liveHorse.setInHeat(true);
        liveHorse.setLastInHeat(System.currentTimeMillis());
        liveHorse.update();
    }
}
