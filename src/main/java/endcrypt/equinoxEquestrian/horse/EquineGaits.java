package endcrypt.equinoxEquestrian.horse;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.enums.Gaits;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EquineGaits implements Listener {


    private final EquinoxEquestrian plugin;

    public EquineGaits(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startGaitsUpdater();
    }

    private final Map<Player, Gaits> playerCurrentGaits = new HashMap<>();


    private void startGaitsUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : playerCurrentGaits.keySet()) {
                Gaits playerCurrentGait = playerCurrentGaits.get(player);


                if (playerCurrentGait == Gaits.WALK) {
                    sendActionBarMessage(player, "§f[§a§l||||§7§l|||||||||||||||||||||§f] §aWalk");
                }

                if (playerCurrentGait == Gaits.TROT) {
                    sendActionBarMessage(player, "§f[§a§l||||§e§l||||§7§l|||||||||||||||||§f] §eTrot");
                }

                if (playerCurrentGait == Gaits.CANTER) {
                    sendActionBarMessage(player, "§f[§a§l||||§e§l||||§6§l|||||§7§l||||||||||||§f] §6Canter");
                }

                if (playerCurrentGait == Gaits.GALLOP) {
                    sendActionBarMessage(player, "§f[§a§l||||§e§l||||§6§l|||||§4§l||||||||||||§f] §4Gallop");
                }

            }
        }, 20L, 10L);
    }

    private void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(message));
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null) {
            return;
        }

        if (item.getType() != Material.STICK) {
            return;
        }
        if (!(player.getVehicle() instanceof AbstractHorse equineHorse)) {
            return;
        }

        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
           Gaits currentGait = playerCurrentGaits.get(player);

           if (Gaits.getNextGait(currentGait) != null) {

               playerCurrentGaits.put(player, Gaits.getNextGait(currentGait));
               ((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(playerCurrentGaits.get(player).getSpeed() * EquineUtils.getBaseSpeed(equineHorse));
           }
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Gaits currentGait = playerCurrentGaits.get(player);

            if (Gaits.getPreviousGait(currentGait) != null) {
                playerCurrentGaits.put(player, Gaits.getPreviousGait(currentGait));
                ((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(playerCurrentGaits.get(player).getSpeed() * EquineUtils.getBaseSpeed(equineHorse));
            }
        }
    }


    @EventHandler
    public void onMount(VehicleEnterEvent event) {
        if(!(event.getEntered() instanceof Player)) {
            return;
        }

        if(!(event.getVehicle() instanceof AbstractHorse)) {
            return;
        }

        if(!EquineUtils.isLivingEquineHorse((AbstractHorse) event.getVehicle())) {
            return;
        }


        Player player = (Player) event.getEntered();
        AbstractHorse equineHorse = (AbstractHorse) event.getVehicle();
        playerCurrentGaits.put(player, Gaits.WALK);
        ((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(playerCurrentGaits.get(player).getSpeed() * EquineUtils.getBaseSpeed(equineHorse));

    }

    @EventHandler
    public void onUnmount(VehicleExitEvent event) {
        if(!(event.getExited() instanceof Player)) {
            return;
        }

        if(!(event.getVehicle() instanceof AbstractHorse)) {
            return;
        }
        if(!EquineUtils.isLivingEquineHorse((AbstractHorse) event.getVehicle())) {
            return;
        }

        Player player = (Player) event.getExited();
        AbstractHorse equineHorse = (AbstractHorse) event.getVehicle();
        playerCurrentGaits.remove(player);
        ((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(EquineUtils.getBaseSpeed(equineHorse));

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!(player.getVehicle() instanceof AbstractHorse)) {
            return;
        }

        if(EquineUtils.isLivingEquineHorse((AbstractHorse) player.getVehicle())) {
            return;
        }

        AbstractHorse equineHorse = (AbstractHorse) player.getVehicle();
        playerCurrentGaits.put(player, Gaits.WALK);
        ((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(playerCurrentGaits.get(player).getSpeed() * EquineUtils.getBaseSpeed(equineHorse));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if(!(player.getVehicle() instanceof AbstractHorse)) {
            return;
        }

        if(EquineUtils.isLivingEquineHorse((AbstractHorse) player.getVehicle())) {
            return;
        }

        AbstractHorse equineHorse = (AbstractHorse) player.getVehicle();
        playerCurrentGaits.remove(player);
        ((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(EquineUtils.getBaseSpeed(equineHorse));
    }
}
