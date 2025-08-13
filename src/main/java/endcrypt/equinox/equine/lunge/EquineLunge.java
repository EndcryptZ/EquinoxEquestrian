package endcrypt.equinox.equine.lunge;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerLungeHorseEvent;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EquineLunge {

    private final EquinoxEquestrian plugin;

    public EquineLunge(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    /** Create evenly spaced points in a flat circle */
    private List<Location> makeCircle(Location center, double radius, int height) {
        List<Location> points = new ArrayList<>();
        double y = center.getY();
        int samples = (int) Math.max(8, 2 * Math.PI * radius);

        for (int i = 0; i < samples; i++) {
            double angle = 2 * Math.PI * i / samples;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            for (int j = 0; j < height; j++) {
                points.add(new Location(center.getWorld(), x, y + j, z));
            }
        }
        return points;
    }

    public void lungeHorse(Player player) {
        List<Entity> leashedList = EquineUtils.getLeashedEntities(player);

        if (leashedList.size() != 1) {
            player.sendMessage("§cYou must have exactly one horse leashed to lunge.");
            return;
        }
        if (!(leashedList.getFirst() instanceof AbstractHorse horse)) {
            player.sendMessage("§cYou can only lunge a horse!");
            return;
        }
        if (!EquineUtils.hasPermissionToHorse(player, horse)) {
            player.sendMessage("§cYou can only lunge your own tamed horse!");
            return;
        }

        if (EquineUtils.isLunging(horse)) {
            Keys.writePersistentData(horse, Keys.IS_LUNGING, "false");
            return;
        }

        List<Location> path = makeCircle(player.getLocation(), 5, 1);
        int startIndex = findClosestIndex(horse.getLocation(), path);
        if (startIndex == -1) {
            player.sendMessage("§cNo starting point found.");
            return;
        }

        EquinePlayerLungeHorseEvent event = new EquinePlayerLungeHorseEvent(player, horse);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        horse.setAI(false);
        Keys.writePersistentData(horse, Keys.IS_LUNGING, "true");

        final int[] pathIndex = { startIndex };

        Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
            if (!horse.isLeashed() || horse.getLeashHolder() != player || !EquineUtils.isLunging(horse)) {
                stopLunging(horse, task);
                return;
            }

            List<Location> updatedPath = makeCircle(player.getLocation(), 5, 1);

            int nextIndex = (pathIndex[0] + 1) % updatedPath.size();
            Location intendedPoint = findGround(updatedPath.get(nextIndex));

            Location safePoint = findSafeAdjacent(intendedPoint, horse.getLocation());

            if (safePoint == null) {
                player.sendMessage("§ePath blocked, waiting...");
                return;
            }

            // Rotate horse toward target
            Vector dir = safePoint.toVector().subtract(horse.getLocation().toVector());
            float yaw = (float) Math.toDegrees(Math.atan2(dir.getZ(), dir.getX())) - 90;
            safePoint.setYaw(yaw);

            horse.teleport(safePoint);
            pathIndex[0] = nextIndex;

        }, 0, 3L);
    }

    private void stopLunging(AbstractHorse horse, BukkitTask task) {
        task.cancel();
        horse.setAI(true);
        Keys.writePersistentData(horse, Keys.IS_LUNGING, "false");
    }

    private int findClosestIndex(Location loc, List<Location> points) {
        double closest = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < points.size(); i++) {
            double dist = loc.distance(points.get(i));
            if (dist < closest) {
                closest = dist;
                index = i;
            }
        }
        return index;
    }

    private boolean isSafeLocation(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;

        Block feet = world.getBlockAt(loc);
        Block head = feet.getRelative(BlockFace.UP);
        Block aboveHead = head.getRelative(BlockFace.UP);

        return !feet.getType().isSolid()
                && !head.getType().isSolid()
                && !aboveHead.getType().isSolid();
    }

    private Location findGround(Location loc) {
        World world = loc.getWorld();
        if (world == null) return null;

        Location check = loc.clone();
        for (int y = loc.getBlockY(); y >= world.getMinHeight(); y--) {
            check.setY(y);
            if (world.getBlockAt(check).getType().isSolid()) {
                return check.clone().add(0, 1, 0);
            }
        }
        return null; // no ground found
    }

    /**
     * Checks intended point and nearby offsets.
     * Rejects points if:
     *  - No ground found
     *  - Height difference from horse is >= 2
     *  - Not safe for horse to stand
     */
    private Location findSafeAdjacent(Location target, Location horseLoc) {
        if (isValidForHorse(target, horseLoc)) return target;

        double step = 0.5;
        Vector[] offsets = {
                new Vector(step, 0, 0),
                new Vector(-step, 0, 0),
                new Vector(0, 0, step),
                new Vector(0, 0, -step),
                new Vector(step, 0, step),
                new Vector(-step, 0, step),
                new Vector(step, 0, -step),
                new Vector(-step, 0, -step)
        };

        for (Vector off : offsets) {
            Location alt = target.clone().add(off);
            alt = findGround(alt);
            if (isValidForHorse(alt, horseLoc)) {
                return alt;
            }
        }
        return null;
    }

    private boolean isValidForHorse(Location loc, Location horseLoc) {
        if (loc == null) return false;
        if (!isSafeLocation(loc)) return false;
        double heightDiff = Math.abs(loc.getY() - horseLoc.getY());
        return heightDiff < 2.0;
    }

}
