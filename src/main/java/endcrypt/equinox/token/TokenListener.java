package endcrypt.equinox.token;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.TaskUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TokenListener implements Listener {

    private final EquinoxEquestrian plugin;
    public TokenListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        if(event.getPlayer().hasPlayedBefore()) {
            return;
        }

        TaskUtils.runTaskLater(1, () -> plugin.getPlayerDataManager().getPlayerData(event.getPlayer()).setTokens(1));
    }
}
