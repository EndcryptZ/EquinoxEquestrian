package endcrypt.equinox.utils;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HoloUtils {

    public static void createPersistentHolo(String id, String text, Location location) {
        HologramManager manager = FancyHologramsPlugin.get().getHologramManager();

        TextHologramData textHologram = new TextHologramData(id, location);

        textHologram.removeLine(0);
        textHologram.addLine(text);
        textHologram.setPersistent(true);
        textHologram.setTextUpdateInterval(10);

        Hologram hologram = manager.create(textHologram);
        manager.addHologram(hologram);

        hologram.forceUpdate();
        hologram.queueUpdate();
    }

    public static void removePersistentHolo(String id) {
        HologramManager manager = FancyHologramsPlugin.get().getHologramManager();
        for (Hologram hologram : manager.getHolograms()) {
            if(hologram.getName().equals(id)) {
                manager.removeHologram(hologram);
                break;
            }
        }

    }

    public static void createFlyoutHolo(String text, Location location) {
        HologramManager manager = FancyHologramsPlugin.get().getHologramManager();

        TextHologramData textHologram = new TextHologramData(UUID.randomUUID().toString(), location);

        textHologram.setPersistent(false);
        textHologram.removeLine(0);
        textHologram.addLine(text);
        textHologram.setTextUpdateInterval(10);

        Hologram hologram = manager.create(textHologram);
        manager.addHologram(hologram);

        hologram.forceUpdate();
        hologram.queueUpdate();

        // Animation config
        int duration = 40; // total ticks (1 second)
        double totalHeight = 0.8;
        double step = totalHeight / duration;

        BukkitTask task = new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration) {
                    manager.removeHologram(hologram);
                    cancel(); // this stops the task
                    return;
                }

                Location current = textHologram.getLocation();
                if (current != null) {
                    current.add(0, step, 0);
                    textHologram.setLocation(current);
                    hologram.forceUpdate();
                    hologram.queueUpdate();
                }

                ticks++;
            }
        }.runTaskTimer(EquinoxEquestrian.instance, 0L, 1L); // this runs the task
    }
}
