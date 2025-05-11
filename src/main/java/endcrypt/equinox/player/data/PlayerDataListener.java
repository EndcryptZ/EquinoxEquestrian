package endcrypt.equinox.player.data;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {

    private final EquinoxEquestrian plugin;
    public PlayerDataListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerDataManager().load(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        plugin.getPlayerDataManager().save(event.getPlayer());
    }
}
