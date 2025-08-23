package endcrypt.equinox.equine.hunger;

import com.destroystokyo.paper.entity.Pathfinder;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftHorse;
import org.bukkit.entity.AbstractHorse;

import java.util.Set;

public class EquineHunger {

    Set<Material> allowedFoodSource = Set.of(Material.GRASS_BLOCK, Material.SHORT_GRASS, Material.TALL_GRASS);
    private final EquinoxEquestrian plugin;
    public EquineHunger(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineHungerTask(plugin);
    }

    public void checkFood(AbstractHorse horse) {
        double hunger = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);

        // Find the nearest edible blocks for horse
        Block targetBlock = findNearestBlock(
                horse.getLocation(),
                allowedFoodSource,
                7 // radius
        );

        if (targetBlock == null) {
            Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
            return;
        }

        if (Keys.readPersistentData(horse, Keys.IS_IN_WATER_TASK)) {
            Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
            return;
        }

        Pathfinder horsePathfinder = horse.getPathfinder();
        horsePathfinder.moveTo(targetBlock.getLocation(), 1.2);
        if (horsePathfinder.getCurrentPath() != null) {
           if (!horsePathfinder.getCurrentPath().canReachFinalPoint()) return;
        }

        Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, true);


        // Schedule a repeating task to check distance until reached
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (!horse.isValid() || horse.isDead()) {
                task.cancel();
                Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
                return;
            }

            if (!horse.getWorld().equals(targetBlock.getWorld())) {
                plugin.getLogger().warning(String.format(
                        "checkFood: cancelled — horse is in world '%s' but target block is in world '%s'.",
                        horse.getWorld().getName(),
                targetBlock.getWorld().getName()
                ));
                Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
                task.cancel();
                return; // worlds are different, so cancel the method
            }


            double dist = horse.getLocation().distance(targetBlock.getLocation());
            horsePathfinder.moveTo(targetBlock.getLocation(), 1.2);

            if (horsePathfinder.getCurrentPath() != null) {
                if (!horsePathfinder.getCurrentPath().canReachFinalPoint()) {
                    Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
                    task.cancel();
                    return;
                }
            }

            if (dist <= 2.5) {
                Location horseLoc = horse.getLocation();
                Location targetLoc = targetBlock.getLocation().clone().add(0.5, 0, 0.5); // center of block
                horseLoc.setDirection(targetLoc.toVector().subtract(horseLoc.toVector()));
                horse.teleport(horseLoc);


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

                    // Cancel the process if the block is not a valid food source
                    if (!allowedFoodSource.contains(targetBlock.getType())) {
                        Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
                        return;
                    }

                    // Play block break effect and particles
                    horse.getWorld().playSound(targetBlock.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);
                    horse.getWorld().spawnParticle(Particle.BLOCK, targetBlock.getLocation().add(0.5, 0.5, 0.5), 20,
                            0.3, 0.3, 0.3, targetBlock.getBlockData());


                    // Replace grass block with dirt
                    if(targetBlock.getType().equals(Material.GRASS_BLOCK)) {
                        targetBlock.setType(Material.DIRT);
                    } else {
                        targetBlock.setType(Material.AIR);
                    }

                    // Restore hunger
                    Keys.writePersistentData(horse, Keys.IS_IN_FOOD_TASK, false);
                    Keys.writePersistentData(horse, Keys.HUNGER_PERCENTAGE, Math.min(100, hunger + 10));

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
