package endcrypt.equinox.equine.transfer;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EquineTransferListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineTransferListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quittingPlayer = event.getPlayer();
        plugin.getEquineManager().getEquineTransferManager().getRequestMap().remove(quittingPlayer);
    }
}
