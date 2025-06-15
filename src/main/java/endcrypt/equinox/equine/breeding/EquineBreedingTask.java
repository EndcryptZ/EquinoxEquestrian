package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;

public class EquineBreedingTask {

    private final EquinoxEquestrian plugin;
    public EquineBreedingTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkBreeding, 20L, 20L);
    }

    private void checkBreeding() {
        for (AbstractHorse horse : plugin.getEquineManager().getEquineCrossTie().getCrosstiedHorses()) {
            EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
            if(liveHorse.getGender() != Gender.MARE) return;
            Bukkit.getServer().broadcast(ColorUtils.color(liveHorse.getName() + "is indeed a mare."));
        }
    }
}