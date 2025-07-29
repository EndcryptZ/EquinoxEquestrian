package endcrypt.equinox.hologram;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.UUID;

public class HologramManager {

    public Hologram createPersistentHolo(String id, String text, Location location) {
        de.oliver.fancyholograms.api.HologramManager manager = FancyHologramsPlugin.get().getHologramManager();

        TextHologramData textHologram = new TextHologramData(id, location);

        textHologram.removeLine(0);
        textHologram.addLine(text);
        textHologram.setPersistent(true);
        textHologram.setTextUpdateInterval(10);
        textHologram.setSeeThrough(true);


        Hologram hologram = manager.create(textHologram);
        manager.addHologram(hologram);

        hologram.forceUpdate();
        hologram.queueUpdate();
        return hologram;
    }

    public Hologram createTemporaryHolo(String id, String text, Location location) {
        de.oliver.fancyholograms.api.HologramManager manager = FancyHologramsPlugin.get().getHologramManager();

        if(manager.getHologram(id).isPresent()) manager.getHologram(id).get();

        TextHologramData textHologram = new TextHologramData(id, location);

        textHologram.removeLine(0);
        textHologram.addLine(text);
        textHologram.setPersistent(false);
        textHologram.setTextUpdateInterval(10);
        textHologram.setSeeThrough(true);

        Hologram hologram = manager.create(textHologram);
        manager.addHologram(hologram);

        hologram.forceUpdate();
        hologram.queueUpdate();

        return hologram;
    }

    public boolean removeHolo(String id) {
        de.oliver.fancyholograms.api.HologramManager manager = FancyHologramsPlugin.get().getHologramManager();
        Optional<Hologram> hologram = manager.getHologram(id);
        if (hologram.isEmpty()) return false;
        manager.removeHologram(hologram.get());
        return true;

    }

    public void createFlyoutHolo(String text, Location location) {
        de.oliver.fancyholograms.api.HologramManager manager = FancyHologramsPlugin.get().getHologramManager();

        TextHologramData textHologram = new TextHologramData(UUID.randomUUID().toString(), location);

        textHologram.setPersistent(false);
        textHologram.removeLine(0);
        textHologram.addLine(text);
        textHologram.setTextUpdateInterval(10);
        textHologram.setSeeThrough(true);

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
