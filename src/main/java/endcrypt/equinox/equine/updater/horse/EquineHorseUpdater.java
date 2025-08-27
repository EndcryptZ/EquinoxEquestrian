package endcrypt.equinox.equine.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.EquineUtils;
import org.bukkit.entity.AbstractHorse;

public class EquineHorseUpdater {

    private final EquinoxEquestrian plugin;
    public EquineHorseUpdater(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void loadHorse(AbstractHorse horse) {
        if (!EquineUtils.isLivingEquineHorse(horse)) return;
        if (horse.getOwner() == null) return;

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
                    ColorUtils.color(plugin.getPrefix() + "<green>[Load] Updated horse in database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + horse.getOwner().getName() + ")")))
            ;
        } else {
            plugin.getDatabaseManager().getDatabaseHorses().addHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>[Load] Added horse to database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + horse.getOwner().getName() + ")")))
            ;
            equineLiveHorse.update();
        }
    }

    public void saveHorse(AbstractHorse horse, boolean isUnload) {
        if (!EquineUtils.isLivingEquineHorse(horse)) return;

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setLastLocation(horse.getLocation());

        if (plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
            equineLiveHorse.update();
            plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
            String action = isUnload ? "[Unload]" : "[Save]";
            plugin.getServer().getConsoleSender().sendMessage(
                    ColorUtils.color(plugin.getPrefix() + "<green>" + action + " Updated horse in database: <white>")
                            .append(horse.name())
                            .append(ColorUtils.color(" <yellow>(Owner: " + horse.getOwner().getName() + ")")))
            ;
        }
    }
}
