package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.player.data.PlayerData;
import eu.decentsoftware.holograms.api.DHAPI;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EquineLeveling {

    private final EquinoxEquestrian plugin;
    public EquineLeveling(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void addExp(Player player, double amount, boolean notify) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setExp(playerData.getExp() + amount);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);
        if(notify) {
            Location location = player.getLocation().add(0, 1, 0);
            DHAPI.createHologram("Test", location);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }

}
