package endcrypt.equinox.player.data;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public void load(Player player) {
        this.loadPlayer(player);
    }

    public void save(Player player) {
        this.saveTokens(player);
        this.savePlayerHorses(player);
        this.playerDataMap.remove(player);
    }

    public void loadPlayer(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                List<UUID> playerHorses = plugin.getDatabaseManager().getPlayerHorses(player);
                plugin.getDatabaseManager().addPlayer(player);
                playerDataMap.put(player, new PlayerData(null, plugin.getDatabaseManager().getTokenAmount(player), playerHorses));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void savePlayerHorses(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        List<UUID> playerHorses = playerData.getOwnedHorses();
        try {
            List<UUID> databaseHorses = plugin.getDatabaseManager().getPlayerHorses(player);
            List<UUID> newHorses = playerHorses.stream().filter(horse -> !databaseHorses.contains(horse)).toList();

            for (UUID uuid : newHorses) {
                plugin.getDatabaseManager().addHorse((AbstractHorse) Bukkit.getEntity(uuid));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveTokens(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        try {
            plugin.getDatabaseManager().setTokenAmount(player, playerData.getTokens());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
