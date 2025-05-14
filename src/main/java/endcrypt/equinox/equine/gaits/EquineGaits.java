package endcrypt.equinox.equine.gaits;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.Component;
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

    // Map to store the progress of the gait for each player
    private final Map<Player, Integer> playerCurrentProgress = new HashMap<>();
    private final Map<Player, Gaits> playerCurrentGaits = new HashMap<>();


    private void startGaitsUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : playerCurrentGaits.keySet()) {
                Gaits playerCurrentGait = playerCurrentGaits.get(player);

                if (EquineUtils.isCrossTied((AbstractHorse) player.getVehicle())) {
                    continue;
                }

                // Get current progress (default to 0 if none exists)
                int currentProgress = playerCurrentProgress.getOrDefault(player, 0);
                int targetProgress = getTargetProgress(playerCurrentGait); // Get the target based on the gait

                // Smoothly increase or decrease progress
                if (currentProgress < targetProgress) {
                    currentProgress += 1; // Increase the progress gradually
                } else if (currentProgress > targetProgress) {
                    currentProgress -= 1; // Decrease the progress gradually
                }

                // Save updated progress
                playerCurrentProgress.put(player, currentProgress);

                // Send the updated action bar message based on the current progress
                Component progressBar = ColorUtils.color(getProgressBar(currentProgress, getGaitColor(playerCurrentGait), getGaitLabel(playerCurrentGait)));
                sendActionBarMessage(player, progressBar);
            }
        }, 20L, 2L); // Runs every 10 ticks (0.5 seconds)
    }

    private void sendActionBarMessage(Player player, Component message) {
        player.sendActionBar(message);
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

        if (EquineUtils.isCrossTied(equineHorse)) {
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

    // Example method to determine the target progress based on the current gait
    private int getTargetProgress(Gaits gait) {
        return switch (gait) {
            case WALK -> 4; // For walk, set the target progress as 10
            case TROT -> 10; // For trot, set the target progress as 20
            case CANTER -> 18; // For canter, set the target progress as 30
            case GALLOP -> 30; // For gallop, set the target progress as 40
            // Default is 0 (no movement)
        };
    }

    // Example method to get the color associated with the gait
    private String getGaitColor(Gaits gait) {
        switch (gait) {
            case WALK:
                return "<reset><green>"; // Green for walk
            case TROT:
                return "<reset><yellow>"; // Yellow for trot
            case CANTER:
                return "<reset><gold>"; // Orange for canter
            case GALLOP:
                return "<reset><dark_red>"; // Red for gallop
            default:
                return "<reset><gray>"; // Gray for default
        }
    }

    // Example method to get the label associated with the gait
    private String getGaitLabel(Gaits gait) {
        switch (gait) {
            case WALK:
                return "Walk";
            case TROT:
                return "Trot";
            case CANTER:
                return "Canter";
            case GALLOP:
                return "Gallop";
            default:
                return "Idle";
        }
    }

    // Progress bar builder
    private String getProgressBar(int progress, String color, String label) {
        StringBuilder progressBar = new StringBuilder("<white>[");

        // The total progress is 50 (5 green + 10 yellow + 15 orange + 20 red)
        int totalBars = 30;

        // Calculate filled sections for the bar
        int filled = Math.min(progress, totalBars);  // Ensure the progress does not exceed 50
        int remaining = totalBars - filled;

        // Green for first 5 bars
        if (filled > 0) progressBar.append("<green><bold>").append("|".repeat(Math.min(filled, 4)));

        // Yellow for next 10 bars
        if (filled > 4) progressBar.append("<yellow><bold>").append("|".repeat(Math.min(filled - 4, 6)));

        // Orange for next 15 bars
        if (filled > 10) progressBar.append("<gold><bold>").append("|".repeat(Math.min(filled - 10, 8)));

        // Red for the remaining 20 bars
        if (filled > 18) progressBar.append("<dark_red><bold>").append("|".repeat(Math.min(filled - 18, 12)));

        // Fill remaining with gray
        progressBar.append("<gray><bold>").append("|".repeat(remaining));

        progressBar.append("<white>] ").append(color).append(label);
        return progressBar.toString();
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

        if(EquineUtils.isCrossTied(equineHorse)) {
            ((AbstractHorse) event.getVehicle()).getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
            return;
        }
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
        sendActionBarMessage(player, ColorUtils.color(""));

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
