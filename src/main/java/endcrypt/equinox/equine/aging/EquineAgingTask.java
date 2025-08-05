package endcrypt.equinox.equine.aging;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
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

                Long claimTime = Keys.readPersistentData(abstractHorse, Keys.CLAIM_TIME);
                if (claimTime == null) continue;

                int currentAge = Keys.readPersistentData(abstractHorse, Keys.AGE);
                long now = System.currentTimeMillis();

                // Time difference in milliseconds
                long elapsed = now - claimTime;

                // 1 month in milliseconds (approx. 30 days)
                long monthMillis = 30L * 24 * 60 * 60 * 1000;

                // Calculate how many horse years have passed
                int calculatedAge = (int) (elapsed / monthMillis);

                // Update the horse's age only if it's changed
                if (calculatedAge != currentAge) {
                    Keys.writePersistentData(abstractHorse, Keys.AGE, calculatedAge);
                }
            }
        }
    }
}
