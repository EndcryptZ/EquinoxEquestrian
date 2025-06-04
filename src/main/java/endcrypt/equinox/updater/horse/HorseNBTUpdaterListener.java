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
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class HorseNBTUpdaterListener implements Listener {

    private final EquinoxEquestrian plugin;
    public HorseNBTUpdaterListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        for (Entity entity : event.getChunk().getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if((horse.getOwner() == null)) {
                continue;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(horse.getOwner().getUniqueId());

            if(!offlinePlayer.isOnline()) {
                continue;
            }

            EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
            equineLiveHorse.setLastLocation(horse.getLocation());

            if(plugin.getDatabaseManager().horseExists(horse)) {
                equineLiveHorse.update();
                plugin.getDatabaseManager().updateHorse(horse);
                plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Chunk Load] Updated horse in database: " + horse.getName()));
                return;
            }

            plugin.getDatabaseManager().addHorse(horse);
            plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Chunk Load] Added horse to database: " + horse.getName()));

            equineLiveHorse.update();
            }
        }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if (!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
            equineLiveHorse.setLastLocation(horse.getLocation());

            if(plugin.getDatabaseManager().horseExists(horse)) {
                equineLiveHorse.update();
                plugin.getDatabaseManager().updateHorse(horse);
                plugin.getServer().getConsoleSender().sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>[Chunk Unload] Updated horse in database: " + horse.getName()));
                return;
            }

        }
    }
}
