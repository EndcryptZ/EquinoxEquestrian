package endcrypt.equinoxEquestrian.player;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager implements Listener {

    private final EquinoxEquestrian plugin;
    public PlayerManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private final Map<Player, PlayerData> playerMap = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws SQLException {
        plugin.getDatabaseManager().setTokenAmount(event.getPlayer(), playerMap.get(event.getPlayer()).getTokens());
        playerMap.remove(event.getPlayer());
    }

    public PlayerData getPlayerData(Player player) {
        return playerMap.get(player);
    }

    public void addPlayer(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Add the player to the database
                // We don't need to check if player exists because we already do it in addPlayer method.
                plugin.getDatabaseManager().addPlayer(player);
                playerMap.put(player, new PlayerData(null, plugin.getDatabaseManager().getTokenAmount(player)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
