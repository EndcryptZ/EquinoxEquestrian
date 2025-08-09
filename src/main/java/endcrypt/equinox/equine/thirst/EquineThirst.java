package endcrypt.equinox.equine.thirst;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftHorse;
import org.bukkit.entity.AbstractHorse;

import java.util.Set;

public class EquineThirst {

    private final EquinoxEquestrian plugin;
    public EquineThirst(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineThirstTask(plugin);
    }

    public void checkWater(AbstractHorse horse) {
        double thirst = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);

        // Find the nearest edible blocks for horse
        Block targetBlock = findNearestBlock(
                horse.getLocation(),
                Set.of(Material.WATER),
                7 // radius
        );

        if (targetBlock == null) return;
        if (Keys.readPersistentData(horse, Keys.IS_IN_FOOD_TASK)) return;
        Keys.writePersistentData(horse, Keys.IS_IN_WATER_TASK, true);

        // Schedule a repeating task to check distance until reached
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!horse.isValid() || horse.isDead()) {
                task.cancel();
                return;
            }

            double dist = horse.getLocation().distance(targetBlock.getLocation());
            horse.getPathfinder().moveTo(targetBlock.getLocation(), 1.2);
            if (dist <= 2) {
                task.cancel();

                // Start eating animation for 3 seconds
                CraftHorse craftHorse = (CraftHorse) horse;
                craftHorse.getHandle().setEating(true);

                // Play eating sound and happy particles immediately
                horse.getWorld().playSound(horse.getLocation(), Sound.ENTITY_HORSE_EAT, 1.0f, 1.0f);
                horse.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, horse.getLocation().add(0, 1, 0), 8);

                // After 3 seconds, stop eating and consume the block
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    craftHorse.getHandle().setEating(false);

                    // Play block break effect and particles
                    horse.getWorld().playSound(targetBlock.getLocation(), Sound.ENTITY_WITCH_DRINK, 1.0f, 1.0f);
                    horse.getWorld().spawnParticle(Particle.BLOCK, targetBlock.getLocation().add(0.5, 0.5, 0.5), 20,
                            0.3, 0.3, 0.3, targetBlock.getBlockData());


                    // Restore hunger
                    Keys.writePersistentData(horse, Keys.IS_IN_WATER_TASK, false);
                    Keys.writePersistentData(horse, Keys.THIRST_PERCENTAGE, Math.min(100, thirst + 10));

                }, 60L); // 3 seconds later
            }
        }, 0L, 10L); // check every 10 ticks
    }

    public static Block findNearestBlock(Location start, Set<Material> targetTypes, int radius) {
        if (start == null || targetTypes == null || targetTypes.isEmpty()) {
            return null;
        }

        World world = start.getWorld();
        if (world == null) {
            return null;
        }

        Block nearestBlock = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        // Scan a cube around the location
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = world.getBlockAt(
                            start.getBlockX() + x,
                            start.getBlockY() + y,
                            start.getBlockZ() + z
                    );

                    if (targetTypes.contains(block.getType())) {
                        double distSq = start.distanceSquared(block.getLocation().add(0.5, 0.5, 0.5));
                        if (distSq < nearestDistanceSq) {
                            nearestDistanceSq = distSq;
                            nearestBlock = block;
                        }
                    }
                }
            }
        }

        return nearestBlock;
    }


}
