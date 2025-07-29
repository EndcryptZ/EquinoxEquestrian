package endcrypt.equinox.utils;

import endcrypt.equinox.EquinoxEquestrian;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HoloUtils {

    public static void createPersistentHolo(String id, String text, Location location) {

        Hologram hologram = DHAPI.createHologram(id, location, true);
        DHAPI.setHologramLine(hologram, 0, 0, text);
    }

    public static void removePersistentHolo(String id) {
        DHAPI.removeHologram(id);
    }

    public static void createFlyoutHolo(String text, Location location) {
        String id = UUID.randomUUID().toString();
        Hologram hologram = DHAPI.createHologram(id, location);
        DHAPI.setHologramLine(hologram, 0, 0, text);

        // Animation config
        int duration = 40; // total ticks (1 second)
        double totalHeight = 0.8;
        double step = totalHeight / duration;

        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration) {
                    DHAPI.removeHologram(id);
                    cancel(); // this stops the task
                    return;
                }

                Location current = hologram.getLocation();
                if (current != null) {
                    current.add(0, step, 0);
                    hologram.setLocation(current);
                }

                ticks++;
            }
        }.runTaskTimer(EquinoxEquestrian.instance, 0L, 1L); // this runs the task
    }
}
