package endcrypt.equinox.menu.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.HorseInventory;

import java.util.Arrays;
import java.util.List;

public class HorseMenuListener implements Listener {

    private final EquinoxEquestrian plugin;
    public HorseMenuListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    List<Material> allowedHorseRightClickItems = Arrays.asList(Material.LEAD, Material.WHEAT);

    @EventHandler
    public void onHorseClick(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof AbstractHorse horse)) {
            return;
        }

        if(allowedHorseRightClickItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType())){
            return;
        }

        if(!EquineUtils.isLivingEquineHorse(horse)) {
            event.getPlayer().sendMessage(MessageUtils.cantInteractWithNotEquineHorse());
            return;
        }

        if(!EquineUtils.hasPermissionToHorse(event.getPlayer(), horse)) {
            event.getPlayer().sendMessage(MessageUtils.cantInteractWithHorse(horse));
            event.setCancelled(true);
            return;
        }

        if(plugin.getEquineManager().getEquineGroomManager().isPlayerGrooming(event.getPlayer())) {


            if(plugin.getEquineManager().getEquineGroomManager().getHorse(event.getPlayer()) == event.getRightClicked()) {
                event.getPlayer().openInventory(plugin.getMenuManager().getHorseMenuManager().getGroomMenu().menu(event.getPlayer(), (AbstractHorse) event.getRightClicked()));

            } else {
                plugin.getMenuManager().getHorseMenuManager().getHorseMenu().open(event.getPlayer(), (AbstractHorse) event.getRightClicked());
            }

            plugin.getEquineManager().getEquineGroomManager().resetGroom(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        plugin.getEquineManager().getEquineGroomManager().resetGroom(event.getPlayer());
        plugin.getMenuManager().getHorseMenuManager().getHorseMenu().open(event.getPlayer(), (AbstractHorse) event.getRightClicked());
        event.setCancelled(true);
    }

    @EventHandler
    public void onHorseInventoryOpen(InventoryOpenEvent event) {
        if(!(event.getInventory() instanceof HorseInventory)) {
            return;
        }

        event.setCancelled(true);
        plugin.getMenuManager().getHorseMenuManager().getHorseMenu().open((Player) event.getPlayer(), (AbstractHorse) event.getInventory().getHolder());
    }
}
