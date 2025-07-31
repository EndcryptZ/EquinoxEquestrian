package endcrypt.equinox.player.data;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
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
        this.saveData(player);
        this.playerDataMap.remove(player);
    }

    public void loadPlayer(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {

            plugin.getDatabaseManager().getDatabasePlayer().addPlayer(player);
            PlayerData playerData = new PlayerData(null, plugin.getDatabaseManager().getDatabasePlayer().getTokenAmount(player));
            playerData.setLevel(plugin.getDatabaseManager().getDatabasePlayer().getLevel(player));
            playerData.setExp(plugin.getDatabaseManager().getDatabasePlayer().getExp(player));
            playerData.setManure(plugin.getDatabaseManager().getDatabasePlayer().getManure(player));

            playerDataMap.put(player, playerData);
        });
    }

    public void loadAllOnlinePlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
    }
    public void saveData(Player player) {
        PlayerData playerData = playerDataMap.get(player);
        plugin.getDatabaseManager().getDatabasePlayer().setTokenAmount(player, playerData.getTokens());
        plugin.getDatabaseManager().getDatabasePlayer().setLevel(player, playerData.getLevel());
        plugin.getDatabaseManager().getDatabasePlayer().setExp(player, playerData.getExp());
        plugin.getDatabaseManager().getDatabasePlayer().setManure(player, playerData.getManure());
    }
}
