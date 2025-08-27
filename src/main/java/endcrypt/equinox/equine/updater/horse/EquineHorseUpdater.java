package endcrypt.equinox.equine.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.EquineUtils;
import org.bukkit.entity.AbstractHorse;

import java.util.Objects;

public class EquineHorseUpdater {

    private final EquinoxEquestrian plugin;
    public EquineHorseUpdater(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void loadHorse(AbstractHorse horse, UpdateAction action) {
        if (!EquineUtils.isLivingEquineHorse(horse)) return;
        if (horse.getOwner() == null) return;

        String actionText = "[" + action.getName() + "] ";

        // apply gait speeds if missing
        if (!Keys.hasPersistentData(horse, Keys.WALK_SPEED)) {
            EquineUtils.applySpeedsToHorse(horse);
            plugin.getLogger().info("Applied new gait speeds to horse '"
                    + horse.getName() + "' (" + horse.getUniqueId() + ")");
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setLastLocation(horse.getLocation());
        equineLiveHorse.setInFoodTask(false);
        equineLiveHorse.setInWaterTask(false);

        if (plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
            equineLiveHorse.update();
            plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>" + actionText +  "Updated horse in database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + horse.getOwner().getName() + ")")))
            ;
        } else {
            plugin.getDatabaseManager().getDatabaseHorses().addHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>" + actionText +  "Added horse to database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + horse.getOwner().getName() + ")")))
            ;
            equineLiveHorse.update();
        }
    }

    public void saveHorse(AbstractHorse horse, UpdateAction action) {
        if (!EquineUtils.isLivingEquineHorse(horse)) return;

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setLastLocation(horse.getLocation());

        String actionText = "[" + action.getName() + "] ";

        if (plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
            equineLiveHorse.update();
            plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>" + actionText + "Updated horse in database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + Objects.requireNonNull(horse.getOwner()).getName() + ")")));
        }
    }

    // Efficiently updates the horse's last location (use this when only the last location needs updating)
    public void updateLastLocation(AbstractHorse horse, UpdateAction action) {
        if (!EquineUtils.isLivingEquineHorse(horse)) return;

        String actionText = "[" + action.getName() + "] ";

        Keys.writePersistentData(horse, Keys.LAST_LOCATION_X, horse.getLocation().getX());
        Keys.writePersistentData(horse, Keys.LAST_LOCATION_Y, horse.getLocation().getY());
        Keys.writePersistentData(horse, Keys.LAST_LOCATION_Z, horse.getLocation().getZ());
        Keys.writePersistentData(horse, Keys.LAST_WORLD, horse.getWorld().getName());

        if (plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
            plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>" + actionText + "Updated last location of horse in database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + Objects.requireNonNull(horse.getOwner()).getName() + ")")));
        }
    }
}
