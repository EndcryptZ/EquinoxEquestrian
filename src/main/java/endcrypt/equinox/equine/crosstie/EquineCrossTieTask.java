package endcrypt.equinox.equine.crosstie;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerCrossTieLeashRemovedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Bat;
import org.bukkit.util.Vector;

import java.util.UUID;

public class EquineCrossTieTask {

    private final EquinoxEquestrian plugin;
    public EquineCrossTieTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        startLeashedBatUpdater();
    }

    private void startLeashedBatUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Loop through all worlds (optional, restrict this if possible)
            for (World world : plugin.getServer().getWorlds()) {
                // Filter entities to reduce unnecessary checks
                for (Bat bat : world.getEntitiesByClass(Bat.class)) {

                    // Fetch the horse UUID stored in the bat's NBT data
                    String horseUUID = NBT.getPersistentData(bat, nbt -> nbt.getString("LEASHED_HORSE"));

                    if (horseUUID.isEmpty()) {
                        continue;
                    }

                    AbstractHorse horse = (AbstractHorse) Bukkit.getEntity(UUID.fromString(horseUUID));
                    if (horse == null) {
                        bat.setLeashHolder(null);
                        bat.remove();
                        continue;
                    }

                    if (!bat.isLeashed() || (!horse.isLeashed())) {
                        bat.setLeashHolder(null);
                        bat.remove();
                        Bukkit.getPluginManager().callEvent(new EquinePlayerCrossTieLeashRemovedEvent(horse));
                        continue;
                    }

                    // Calculate the spawn location based on the horse's direction
                    Location horseLocation = horse.getLocation();
                    Vector direction = horseLocation.getDirection().normalize();
                    Location batSpawnLocation = horseLocation.clone().add(direction.multiply(0.3));
                    batSpawnLocation.setY(batSpawnLocation.getY() + 0.9);

                    // Teleport bat only if its position has changed
                    if (!bat.getLocation().equals(batSpawnLocation)) {
                        bat.teleport(batSpawnLocation);
                    }
                }
            }
        }, 0L, 10L);  // Increase task interval to reduce frequency (10 ticks instead of 3)
    }
}
