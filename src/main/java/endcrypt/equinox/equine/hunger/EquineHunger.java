package endcrypt.equinox.equine.hunger;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.bukkit.entity.AbstractHorse;

public class EquineHunger {

    private final EquinoxEquestrian plugin;
    public EquineHunger(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineHungerTask(plugin);
    }

    /**
     * Calculates and applies hunger loss based on the real-world time elapsed
     * since the last hunger update for this horse.
     * <p>
     * This method should be called when the horse entity loads into the world,
     * ensuring hunger stays consistent regardless of whether the horse was loaded.
     *
     * @param horse The horse whose hunger should be recalculated.
     */
    public void calculateHungerElapsed(@NotNull AbstractHorse horse) {
        if (!EquineUtils.isLivingEquineHorse(horse)) {
            plugin.getLogger().warning(() -> "[Equine] Tried to calculate hunger for a non-equine horse: " + horse.getUniqueId());
            return;
        }

        double currentHunger = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);
        long lastUpdate = Keys.readPersistentData(horse, Keys.LAST_HUNGER_UPDATE);
        long now = System.currentTimeMillis();

        if (lastUpdate <= 0L) {
            // If no recorded update, set baseline
            Keys.writePersistentData(horse, Keys.LAST_HUNGER_UPDATE, now);
            return;
        }

        long elapsedMillis = now - lastUpdate;
        if (elapsedMillis <= 0L) return; // No time passed or system clock issue

        // Hunger decreases 3% per real hour
        final double percentPerSecond = 3.0 / 3600.0;
        double elapsedSeconds = elapsedMillis / 1000.0;
        double hungerLoss = percentPerSecond * elapsedSeconds;

        double newHunger = Math.max(0.0, Math.min(100.0, currentHunger - hungerLoss));

        Keys.writePersistentData(horse, Keys.HUNGER_PERCENTAGE, newHunger);
        Keys.writePersistentData(horse, Keys.LAST_HUNGER_UPDATE, now);
    }
}
