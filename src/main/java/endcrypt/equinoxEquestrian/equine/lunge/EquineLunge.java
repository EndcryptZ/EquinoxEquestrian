package endcrypt.equinoxEquestrian.equine.lunge;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EquineLunge {

    private final EquinoxEquestrian plugin;

    public EquineLunge(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private List<Location> makeCylinder(Location center, double radius, int height) {
        List<Location> placedLocations = new ArrayList<>();

        // Get the Y coordinate (feet of the player or given position)
        double y = center.getY();

        // Calculate the number of points around the circle based on the radius
        int points = (int)(2 * Math.PI * radius); // Approximate points for smooth circle

        // Loop through the points around the circle
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points; // Evenly distribute points around the circle
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));

            // Loop through the height of the cylinder
            for (int j = 0; j < height; j++) {
                // Create the location at the calculated coordinates with the given material
                Location point = new Location(center.getWorld(), x, y + j, z);

                // Add the location to the list of placed locations
                placedLocations.add(point);
            }
        }

        // Return the list of locations where blocks were placed
        return placedLocations;
    }


    public void lungeHorse(Player player) {

        List<Entity> leashedList = EquineUtils.getLeashedEntities(player);

        if (leashedList.size() > 1) {
            player.sendMessage("§cPlease only leash one horse at a time.");
            return;
        }

        if (leashedList.isEmpty()) {
            player.sendMessage("§cYou don't have a leashed horse to lunge.");
            return;
        }

        LivingEntity leashedEntity = (LivingEntity) leashedList.getFirst();

        if (!(leashedEntity instanceof AbstractHorse horse)) {
            player.sendMessage("§cYou can only lunge a horse! The entity you're leashed to is a " + leashedEntity.getType() + ".");
            return;
        }

        if (!(horse.getOwner() == player)) {
            player.sendMessage("§cYou can only lunge your own tamed horse!");
            return;
        }

        if (EquineUtils.isLunging(horse)) {
            NBT.modifyPersistentData(horse, nbt -> {
                nbt.setString("EQUINE_IS_LUNGING", "false");
            });
            return;
        }


        // Find the closest point to the horse
        Location horseLocation = horse.getLocation();
        List<Location> pathwayPoints = makeCylinder(player.getLocation(), 5, 1);
        double closestDistance = Double.MAX_VALUE;

        int closestIndex = -1; // Initialize with an invalid index

        for (int i = 0; i < pathwayPoints.size(); i++) {
            Location point = pathwayPoints.get(i);
            double distance = horseLocation.distance(point);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i; // Store the index of the closest point
            }
        }

        int[] pathIndex = {closestIndex};

        horse.setAI(false);
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setString("EQUINE_IS_LUNGING", "true");

        });

        Bukkit.getScheduler().runTaskTimer(plugin, (lungeScheduler) -> {

            if (!horse.isLeashed()) {
                lungeScheduler.cancel();
                horse.setAI(true);
                NBT.modifyPersistentData(horse, nbt -> {
                    nbt.setString("EQUINE_IS_LUNGING", "false");

                });
            }

            if(horse.getLeashHolder() != player) {
                lungeScheduler.cancel();
                horse.setAI(true);
                NBT.modifyPersistentData(horse, nbt -> {
                    nbt.setString("EQUINE_IS_LUNGING", "false");

                });
            }

            if(!(EquineUtils.isLunging(horse))) {
                lungeScheduler.cancel();
                horse.setAI(true);
            }

            Location playerLocation = player.getLocation();
            List<Location> newPathwayPoints = makeCylinder(playerLocation, 5, 1);

            Location nextPoint = newPathwayPoints.get(pathIndex[0]);

            Location currentLocation = horse.getLocation();
            Vector direction = nextPoint.toVector().subtract(currentLocation.toVector());

            float yaw = (float) Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;

            Location nextLocation = newPathwayPoints.get(pathIndex[0]);
            nextLocation.setYaw(yaw);

            horse.teleport(nextLocation);

            pathIndex[0]++;
            if (pathIndex[0] >= newPathwayPoints.size()) {
                pathIndex[0] = 0;
            }


        }, 0, 3L);



    }



}
