package endcrypt.equinoxEquestrian.player.data;

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

public class PlayerDataManager implements Listener {

    private final EquinoxEquestrian plugin;
    public PlayerDataManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private final Map<Player, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player);
    }

    public Map<Player, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void addPlayer(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Add the player to the database
                // We don't need to check if player exists because we already do it in addPlayer method.
                plugin.getDatabaseManager().addPlayer(player);
                playerDataMap.put(player, new PlayerData(null, plugin.getDatabaseManager().getTokenAmount(player)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
