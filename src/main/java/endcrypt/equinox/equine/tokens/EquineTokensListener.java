package endcrypt.equinox.equine.tokens;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EquineTokensListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineTokensListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFirstJoin(PlayerJoinEvent event) {
        if(event.getPlayer().hasPlayedBefore()) {
            return;
        }

        plugin.getPlayerDataManager().getPlayerData(event.getPlayer()).setTokens(1);
    }
}
