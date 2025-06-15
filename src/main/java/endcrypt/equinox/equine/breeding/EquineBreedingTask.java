package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.equine.nbt.Keys;
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
            if (Keys.readPersistentData(horse, Keys.GENDER) == Gender.MARE) {
                plugin.getServer().broadcast(ColorUtils.color(horse.getName() + " is a mare"));
            }
        }
    }
}