package endcrypt.equinox.equine.waste;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.HeadUtils;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractHorse;

import java.util.Random;

public class EquineWasteTask {

    private final EquinoxEquestrian plugin;
    public EquineWasteTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            checkPoop();
            checkPee();
        }, 20L, 100L);
    }

    private void checkPoop() {
        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if(!EquineUtils.isLivingEquineHorse(horse)) continue;
                EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
                if (liveHorse.getLastPoop() + TimeUtils.hoursToMillis(2) < System.currentTimeMillis()) {
                    handlePoop(liveHorse);
                }
            }
        }
    }

    private void checkPee() {
        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if(!EquineUtils.isLivingEquineHorse(horse)) continue;
                EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
                if (liveHorse.getLastPee() + TimeUtils.hoursToMillis(2) < System.currentTimeMillis()) {
                    handlePee(liveHorse);
                }
            }
        }
    }

    private void handlePoop(EquineLiveHorse liveHorse) {
        if (new Random().nextBoolean()) {
            AbstractHorse horse = liveHorse.getHorse();
            if(horsePoop(horse)) {
                liveHorse.setLastPoop(System.currentTimeMillis());
                liveHorse.update();
            }
        }
    }

    private void handlePee(EquineLiveHorse liveHorse) {
        if (new Random().nextBoolean()) {
            AbstractHorse horse = liveHorse.getHorse();
            if(horsePee(horse)) {
                liveHorse.setLastPee(System.currentTimeMillis());
                liveHorse.update();
            }
        }
    }

    private boolean horsePoop(AbstractHorse horse) {
        Location horseLoc = horse.getLocation();
        int radius = 2;

        for (int y = -1; y <= 1; y++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Location check = horseLoc.clone().add(dx, y, dz);
                    Block ground = check.getBlock();
                    Block above = check.clone().add(0, 1, 0).getBlock();

                    if (!ground.getType().isSolid()) continue;
                    if (!above.getType().isAir()) continue;

                    String holoId = "Waste" + above.getWorld().getName() + above.getX() + above.getY() + above.getZ();
                    plugin.getDatabaseManager().getDatabaseWaste().addWasteBlock("Poop", above.getLocation());
                    Location holoLoc = above.getLocation().add(0.5, 1, 0.5);
                    plugin.getHologramManager().createTemporaryHolo(holoId, "<gold>Poop", holoLoc);
                    HeadUtils.placeHeadFromHDB(above.getLocation(), "1682");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean horsePee(AbstractHorse horse) {
        Location horseLoc = horse.getLocation();
        int radius = 2;

        for (int y = -1; y <= 1; y++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Location check = horseLoc.clone().add(dx, y, dz);
                    Block ground = check.getBlock();
                    Block above = check.clone().add(0, 1, 0).getBlock();

                    if (!ground.getType().isSolid()) continue;
                    if (!above.getType().isAir()) continue;

                    String holoId = "Waste" + above.getWorld().getName() + above.getX() + above.getY() + above.getZ();
                    plugin.getDatabaseManager().getDatabaseWaste().addWasteBlock("Pee", above.getLocation());

                    // Center the hologram by adding (0.5, 1, 0.5)
                    Location holoLoc = above.getLocation().add(0.5, 1, 0.5);
                    plugin.getHologramManager().createTemporaryHolo(holoId, "<yellow>Pee", holoLoc);

                    above.setType(Material.YELLOW_CARPET);
                    return true;
                }
            }
        }
        return false;
    }
}
