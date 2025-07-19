package endcrypt.equinox.equine.waste;

import com.samjakob.spigui.item.ItemBuilder;
import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.HeadUtils;
import endcrypt.equinox.utils.TimeUtils;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.ItemStack;

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

                    plugin.getDatabaseManager().getDatabaseWaste().addWasteBlock("Poop", above.getLocation());
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

                    plugin.getDatabaseManager().getDatabaseWaste().addWasteBlock("Pee", above.getLocation());
                    above.setType(Material.YELLOW_CARPET);
                    return true;
                }
            }
        }
        return false;
    }
}
