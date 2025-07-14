package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.player.data.PlayerData;
import org.bukkit.entity.Player;

public class EquineLeveling {

    private final EquinoxEquestrian plugin;
    public EquineLeveling(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void addExp(Player player, double amount) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setExp(playerData.getExp() + amount);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);
    }

}
