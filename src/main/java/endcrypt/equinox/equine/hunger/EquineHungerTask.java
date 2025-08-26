package endcrypt.equinox.equine.hunger;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.*;
import org.bukkit.entity.AbstractHorse;

public class EquineHungerTask {

    private final EquinoxEquestrian plugin;
    public EquineHungerTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }


    private void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            checkHunger();
            checkFood();
        }, 20L, 20L);
    }

    private void checkHunger() {
        long now = System.currentTimeMillis();

        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(horse)) continue;

                // Initialize hunger data if missing
                if (!Keys.hasPersistentData(horse, Keys.HUNGER_PERCENTAGE)) {
                    Keys.writePersistentData(horse, Keys.HUNGER_PERCENTAGE, Keys.HUNGER_PERCENTAGE.getDefaultValue());
                }
                if (!Keys.hasPersistentData(horse, Keys.LAST_HUNGER_UPDATE)) {
                    Keys.writePersistentData(horse, Keys.LAST_HUNGER_UPDATE, now);
                    continue; // Skip this tick so decay doesn't happen instantly
                }

                double currentHunger = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);
                long lastUpdate = Keys.readPersistentData(horse, Keys.LAST_HUNGER_UPDATE);

                // Time elapsed since last update
                long elapsedMillis = now - lastUpdate;
                if (elapsedMillis <= 0) continue; // No time passed

                // 3% per hour = 0.0008333% per second
                double decayPerMs = 3.0 / (60.0 * 60.0 * 1000.0);
                double decay = elapsedMillis * decayPerMs;

                double beforeHunger = currentHunger;
                currentHunger -= decay;
                if (currentHunger < 0) currentHunger = 0;

                Keys.writePersistentData(horse, Keys.HUNGER_PERCENTAGE, currentHunger);
                Keys.writePersistentData(horse, Keys.LAST_HUNGER_UPDATE, now);

                /*
                plugin.getLogger().info(String.format(
                        "[HungerCheck] Horse: %s | Elapsed: %d ms | Decay: %.4f%% | Before: %.2f%% | After: %.2f%%",
                        horse.getName(), elapsedMillis, decay, beforeHunger, currentHunger
                ));
                 */

            }
        }
    }

    private void checkFood() {
        long now = System.currentTimeMillis();

        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(horse)) continue;
                double currentHunger = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);

                if (!Keys.hasPersistentData(horse, Keys.LAST_SEEK_FOOD)) {
                    Keys.writePersistentData(horse, Keys.LAST_SEEK_FOOD, now);
                    continue; // Skip this tick so decay doesn't happen instantly
                }

                    // Dismount passenger
                    if (currentHunger < 30) {
                        horse.getPassengers().forEach(passenger -> {
                            horse.removePassenger(passenger);
                            passenger.sendMessage(ColorUtils.color("<red>This horse is too hungry to be ridden. It needs at least 30% hunger."));
                        });
                    }


                long lastSeekFood = Keys.readPersistentData(horse, Keys.LAST_SEEK_FOOD);
                if (currentHunger < 50 && lastSeekFood + TimeUtils.secondsToMillis(10) < now) {
                    plugin.getEquineManager().getEquineHunger().checkFood(horse);
                    Keys.writePersistentData(horse, Keys.LAST_SEEK_FOOD, now);
                    continue;
                }


                if (lastSeekFood + TimeUtils.hoursToMillis(1) < now) {
                    plugin.getEquineManager().getEquineHunger().checkFood(horse);
                    Keys.writePersistentData(horse, Keys.LAST_SEEK_FOOD, now);
                }
            }
        }
    }



}
