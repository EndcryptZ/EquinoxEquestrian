package endcrypt.equinox.equine;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EquineTeleport implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineTeleport(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if(event.getCause() != PlayerTeleportEvent.TeleportCause.COMMAND) {
            return;
        }

        if(!(event.getPlayer().getVehicle() instanceof AbstractHorse horse)) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            horse.teleport(event.getTo());
            horse.addPassenger(event.getPlayer());
        }, 1L);


    }

}
