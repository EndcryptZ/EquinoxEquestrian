package endcrypt.equinox.equine.aging;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

public class EquineAgingTask {

    private final EquinoxEquestrian plugin;
    public EquineAgingTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkAging, 0L, 20L);
    }

    private void checkAging() {
        for (World world : Bukkit.getWorlds()) {
            for (AbstractHorse abstractHorse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(abstractHorse)) continue;

                abstractHorse.setAge(0);
                abstractHorse.setAgeLock(true);
                long birthTime = Keys.readLong(abstractHorse, Keys.BIRTH_TIME);
                if (birthTime == 0) continue;

                int currentAge = Keys.readInt(abstractHorse, Keys.AGE);
                long now = System.currentTimeMillis();

                // Time difference in milliseconds
                long elapsed = now - birthTime;

                // 1 month in milliseconds (approx. 30 days)
                long monthMillis = 30L * 24 * 60 * 60 * 1000;

                // Calculate how many horse years have passed
                int calculatedAge = (int) (elapsed / monthMillis);

                // Update the horse's age only if it's changed
                if (calculatedAge != currentAge) {
                    Keys.writeInt(abstractHorse, Keys.AGE, calculatedAge);
                }

                if (currentAge == 0) {
                    if (abstractHorse.isAdult()) {
                        abstractHorse.setAgeLock(false);
                        abstractHorse.setBaby();
                        return;
                    }
                }

                if (currentAge >= 3) {
                    if (!abstractHorse.isAdult()) {
                        abstractHorse.setAgeLock(false);
                        abstractHorse.setAdult();
                    }
                } else {
                    if (abstractHorse.isAdult()) {
                        abstractHorse.setAgeLock(false);
                        abstractHorse.setBaby();
                    }
                }
            }
        }
    }

}
