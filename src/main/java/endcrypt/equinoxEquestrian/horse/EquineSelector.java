package endcrypt.equinoxEquestrian.horse;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.api.events.EquinePlayerSelectHorseEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EquineSelector implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineSelector(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractHorse(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) {
            return;
        }

        if(!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        EquinePlayerSelectHorseEvent equinePlayerSelectHorseEvent = new EquinePlayerSelectHorseEvent(player, horse);
        plugin.getServer().getPluginManager().callEvent(equinePlayerSelectHorseEvent);

        if(equinePlayerSelectHorseEvent.isCancelled()) {
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&aYou have selected " + horse.getName() + "!"));
        plugin.getPlayerManager().getPlayerData(player).setSelectedHorse(horse);




    }

}
