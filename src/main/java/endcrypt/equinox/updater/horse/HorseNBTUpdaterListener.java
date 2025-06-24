package endcrypt.equinox.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

public class HorseNBTUpdaterListener implements Listener {

    private final EquinoxEquestrian plugin;
    public HorseNBTUpdaterListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent event) {

        for (Entity entity : event.getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if((horse.getOwner() == null)) {
                continue;
            }

            EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
            equineLiveHorse.setLastLocation(horse.getLocation());

            if(plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
                equineLiveHorse.update();
                plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
                plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Load] Updated horse in database: " + horse.getName()));
                continue;
            }

            plugin.getDatabaseManager().getDatabaseHorses().addHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Load] Added horse to database: " + horse.getName()));

            equineLiveHorse.update();
            }
        }

    @EventHandler
    public void onEntitiesUnload(EntitiesUnloadEvent event) {
        for (Entity entity : event.getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if (!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
            equineLiveHorse.setLastLocation(horse.getLocation());

            if(plugin.getDatabaseManager().getDatabaseHorses().horseExists(horse)) {
                equineLiveHorse.update();
                plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);
                plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Unload] Updated horse in database: " + horse.getName()));
            }
        }
    }
}
