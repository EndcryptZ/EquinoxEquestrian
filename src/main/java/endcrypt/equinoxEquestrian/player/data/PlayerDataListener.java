package endcrypt.equinoxEquestrian.player.data;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerDataListener implements Listener {

    private final EquinoxEquestrian plugin;
    public PlayerDataListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerDataManager().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws SQLException {
        plugin.getDatabaseManager().setTokenAmount(event.getPlayer(), plugin.getPlayerDataManager().getPlayerData(event.getPlayer()).getTokens());
        plugin.getPlayerDataManager().getPlayerDataMap().remove(event.getPlayer());
    }
}
