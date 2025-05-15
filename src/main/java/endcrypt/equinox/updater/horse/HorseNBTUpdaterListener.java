package endcrypt.equinox.updater.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class HorseNBTUpdaterListener implements Listener {

    private final EquinoxEquestrian plugin;
    public HorseNBTUpdaterListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        for (Entity entity : event.getChunk().getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if((horse.getOwner() == null)) {
                continue;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(horse.getOwner().getUniqueId());

            if(!offlinePlayer.isOnline()) {
                continue;
            }

            Player player = offlinePlayer.getPlayer();
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);

            if(playerData.getOwnedHorses().contains(entity.getUniqueId())) {
                return;
            }

            playerData.addOwnedHorse(entity.getUniqueId());

            new EquineLiveHorse(horse).updateDefault(horse);
        }

    }
}
