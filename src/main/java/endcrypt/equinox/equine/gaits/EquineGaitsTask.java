package endcrypt.equinox.equine.gaits;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class EquineGaitsTask {

    private final EquinoxEquestrian plugin;
    public EquineGaitsTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        startGaitsUpdater();
    }

    private void startGaitsUpdater() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().keySet()) {
                Gaits playerCurrentGait = plugin.getEquineManager().getEquineGaits().getPlayerCurrentGaits().get(player);

                if (Keys.readBoolean((AbstractHorse) player.getVehicle(), Keys.IS_CROSS_TIED)) {
                    continue;
                }

                // Get current progress (default to 0 if none exists)
                int currentProgress = plugin.getEquineManager().getEquineGaits().getPlayerCurrentProgress().getOrDefault(player, 0);
                int targetProgress = getTargetProgress(playerCurrentGait); // Get the target based on the gait

                // Smoothly increase or decrease progress
                if (currentProgress < targetProgress) {
                    currentProgress += 1; // Increase the progress gradually
                } else if (currentProgress > targetProgress) {
                    currentProgress -= 1; // Decrease the progress gradually
                }

                // Save updated progress
                plugin.getEquineManager().getEquineGaits().getPlayerCurrentProgress().put(player, currentProgress);

                // Send the updated action bar message based on the current progress
                Component progressBar = ColorUtils.color(getProgressBar(currentProgress, getGaitColor(playerCurrentGait), getGaitLabel(playerCurrentGait)));
                player.sendActionBar(progressBar);
            }
        }, 20L, 2L); // Runs every 10 ticks (0.5 seconds)
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

        // Yellow for the next 10 bars
        if (filled > 4) progressBar.append("<yellow><bold>").append("|".repeat(Math.min(filled - 4, 6)));

        // Orange for the next 15 bars
        if (filled > 10) progressBar.append("<gold><bold>").append("|".repeat(Math.min(filled - 10, 8)));

        // Red for the remaining 20 bars
        if (filled > 18) progressBar.append("<dark_red><bold>").append("|".repeat(Math.min(filled - 18, 12)));

        // Fill remaining with gray
        progressBar.append("<gray><bold>").append("|".repeat(remaining));

        progressBar.append("<white>] ").append(color).append(label);
        return progressBar.toString();
    }

    private String getGaitColor(Gaits gait) {
        return switch (gait) {
            case WALK -> "<reset><green>"; // Green for walk
            case TROT -> "<reset><yellow>"; // Yellow for trot
            case CANTER -> "<reset><gold>"; // Orange for canter
            case GALLOP -> "<reset><dark_red>"; // Red for gallop
            // Gray for default
        };
    }


    private String getGaitLabel(Gaits gait) {
        return switch (gait) {
            case WALK -> "Walk";
            case TROT -> "Trot";
            case CANTER -> "Canter";
            case GALLOP -> "Gallop";
        };
    }
}
