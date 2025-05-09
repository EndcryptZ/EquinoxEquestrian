package endcrypt.equinoxEquestrian.equine.crosstie;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.api.events.EquineCrossTieLeashEvent;
import endcrypt.equinoxEquestrian.api.events.EquineCrossTieLeashRemovedEvent;
import endcrypt.equinoxEquestrian.equine.EquineUtils;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.UUID;

public class EquineCrossTie {

    private final EquinoxEquestrian plugin;
    public EquineCrossTie(EquinoxEquestrian plugin){
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
                        continue;
                    }

                    if (!bat.isLeashed() || (!horse.isLeashed())) {
                        bat.remove();
                        Bukkit.getPluginManager().callEvent(new EquineCrossTieLeashRemovedEvent(horse));
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
