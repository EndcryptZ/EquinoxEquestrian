package endcrypt.equinox.equine.gaits;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
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

import java.util.Objects;

public class EquineGaitsListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineGaitsListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

        if (Keys.readBoolean(equineHorse, Keys.IS_CROSS_TIED)) {
            return;
        }

        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Gaits currentGait = plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player);

            if (Gaits.getNextGait(currentGait) != null) {

                plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().put(player, Gaits.getNextGait(currentGait));
                Objects.requireNonNull(((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player).getGaitSpeedKey()));
            }
        }

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Gaits currentGait = plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player);

            if (Gaits.getPreviousGait(currentGait) != null) {
                plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().put(player, Gaits.getPreviousGait(currentGait));
                Objects.requireNonNull(((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player).getGaitSpeedKey()));
            }
        }
    }

    @EventHandler
    public void onMount(VehicleEnterEvent event) {
        if(!(event.getEntered() instanceof Player player)) {
            return;
        }

        if(!(event.getVehicle() instanceof AbstractHorse equineHorse)) {
            return;
        }

        if(!EquineUtils.isLivingEquineHorse((AbstractHorse) event.getVehicle())) {
            return;
        }


        plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().put(player, Gaits.WALK);

        if(Keys.readBoolean(equineHorse, Keys.IS_CROSS_TIED)) {
            Objects.requireNonNull(((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0);
            return;
        }
        Objects.requireNonNull(((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player).getGaitSpeedKey()));

    }

    @EventHandler
    public void onUnmount(VehicleExitEvent event) {
        if(!(event.getExited() instanceof Player player)) {
            return;
        }

        if(!(event.getVehicle() instanceof AbstractHorse equineHorse)) {
            return;
        }
        if(!EquineUtils.isLivingEquineHorse((AbstractHorse) event.getVehicle())) {
            return;
        }

        plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().remove(player);
        Objects.requireNonNull(((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, Keys.BASE_SPEED));
        player.sendActionBar(ColorUtils.color(""));

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(!(player.getVehicle() instanceof AbstractHorse equineHorse)) {
            return;
        }

        if(EquineUtils.isLivingEquineHorse((AbstractHorse) player.getVehicle())) {
            return;
        }

        plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().put(player, Gaits.WALK);
        Objects.requireNonNull(((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player).getGaitSpeedKey()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }

        if(!(player.getVehicle() instanceof AbstractHorse equineHorse)) {
            return;
        }

        if(EquineUtils.isLivingEquineHorse((AbstractHorse) player.getVehicle())) {
            return;
        }

        plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().remove(player);
        Objects.requireNonNull(((AbstractHorse) player.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(Keys.readDouble(equineHorse, Keys.BASE_SPEED));
    }
}
