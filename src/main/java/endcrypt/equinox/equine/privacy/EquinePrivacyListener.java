package endcrypt.equinox.equine.privacy;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EquinePrivacyListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquinePrivacyListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    // Event that cancels leashing of horses that are not owned by the player
    @EventHandler
    public void onLeashHorse(PlayerInteractEntityEvent event) {


        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if(!(event.getRightClicked() instanceof org.bukkit.entity.AbstractHorse horse)) {
            return;
        }

        if(!EquineUtils.isLivingEquineHorse(horse)) {
            return;
        }

        if(heldItem.getType() != Material.LEAD) {
            return;
        }
        
        // isOwner
        if(player == horse.getOwner()) {
            return;
        }

        player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You can't interact with this horse!"));
        event.setCancelled(true);

        
    }
}