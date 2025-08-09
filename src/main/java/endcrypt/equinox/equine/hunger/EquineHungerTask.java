package endcrypt.equinox.equine.hunger;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

public class EquineHungerTask {

    private final EquinoxEquestrian plugin;
    public EquineHungerTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }


    private void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::checkHunger, 20L, 20L);
    }

    private void checkHunger() {
        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(horse)) continue;

                double currentHungerPercentage = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);
                long lastHungerUpdate = Keys.readPersistentData(horse, Keys.LAST_HUNGER_UPDATE);
                currentHungerPercentage -= 1;

                if (System.currentTimeMillis() > lastHungerUpdate + TimeUtils.minutesToMillis(20)) continue;

                if (currentHungerPercentage <= 0) currentHungerPercentage = 0;

                Keys.writePersistentData(horse, Keys.LAST_HUNGER_UPDATE, System.currentTimeMillis());
                Keys.writePersistentData(horse, Keys.HUNGER_PERCENTAGE, currentHungerPercentage);
            }
        }
    }

}
