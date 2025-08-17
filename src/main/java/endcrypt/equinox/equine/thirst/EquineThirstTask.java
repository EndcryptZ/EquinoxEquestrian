package endcrypt.equinox.equine.thirst;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

public class EquineThirstTask {

    private final EquinoxEquestrian plugin;
    public EquineThirstTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            checkThirst();
            checkWater();
        }, 20L, 20L);
    }

    private void checkThirst() {
        long now = System.currentTimeMillis();

        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(horse)) continue;

                // Initialize thirst data if missing
                if (!Keys.hasPersistentData(horse, Keys.THIRST_PERCENTAGE)) {
                    Keys.writePersistentData(horse, Keys.THIRST_PERCENTAGE, Keys.THIRST_PERCENTAGE.getDefaultValue());
                }
                if (!Keys.hasPersistentData(horse, Keys.LAST_THIRST_UPDATE)) {
                    Keys.writePersistentData(horse, Keys.LAST_THIRST_UPDATE, now);
                    continue; // Skip this tick so decay doesn't happen instantly
                }

                double currentThirst = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);
                long lastUpdate = Keys.readPersistentData(horse, Keys.LAST_THIRST_UPDATE);

                // Time elapsed since last update
                long elapsedMillis = now - lastUpdate;
                if (elapsedMillis <= 0) continue; // No time passed

                // 50% per 12hrs = 0.0000011574 per second
                double decayPerMs = 50.0 / (12.0 * 60.0 * 60.0 * 1000.0);
                double decay = elapsedMillis * decayPerMs;

                currentThirst -= decay;
                if (currentThirst < 0) currentThirst = 0;

                Keys.writePersistentData(horse, Keys.THIRST_PERCENTAGE, currentThirst);
                Keys.writePersistentData(horse, Keys.LAST_THIRST_UPDATE, now);

            }
        }
    }

    private void checkWater() {
        long now = System.currentTimeMillis();

        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(horse)) continue;
                double currentThirst = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);

                if (!Keys.hasPersistentData(horse, Keys.LAST_SEEK_WATER)) {
                    Keys.writePersistentData(horse, Keys.LAST_SEEK_WATER, now);
                    continue; // Skip this tick so decay doesn't happen instantly
                }

                // Dismount passenger
                if (currentThirst < 30) {
                    horse.getPassengers().forEach(passenger -> {
                        horse.removePassenger(passenger);
                        passenger.sendMessage(ColorUtils.color("This horse is too thirsty to be ridden. It needs at least 30% thirst."));
                    });
                }


                long lastSeekWater = Keys.readPersistentData(horse, Keys.LAST_SEEK_WATER);
                if (currentThirst < 50 && lastSeekWater + TimeUtils.secondsToMillis(10) < now) {
                    plugin.getEquineManager().getEquineThirst().checkWater(horse);
                    Keys.writePersistentData(horse, Keys.LAST_SEEK_WATER, now);
                }

            }
        }
    }
}
