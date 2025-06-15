package endcrypt.equinox.player.data;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    private final EquinoxEquestrian plugin;
    public PlayerDataManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
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
        this.playerDataMap.remove(player);
    }

    public void loadPlayer(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {

            plugin.getDatabaseManager().getDatabasePlayer().addPlayer(player);
            playerDataMap.put(player, new PlayerData(null, plugin.getDatabaseManager().getDatabasePlayer().getTokenAmount(player)));
        });
    }

    public void loadAllOnlinePlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
    }
    public void saveTokens(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        plugin.getDatabaseManager().getDatabasePlayer().setTokenAmount(player, playerData.getTokens());
    }
}
