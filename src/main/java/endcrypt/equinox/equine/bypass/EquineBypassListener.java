package endcrypt.equinox.equine.bypass;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EquineBypassListener implements Listener {

    public EquineBypassListener(EquinoxEquestrian plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(!EquineBypass.hasBypass(event.getPlayer())) return;
        EquineBypass.remove(event.getPlayer());
    }
}
