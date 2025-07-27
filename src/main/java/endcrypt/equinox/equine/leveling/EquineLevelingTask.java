package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;

public class EquineLevelingTask {

    private final EquinoxEquestrian plugin;
    public EquineLevelingTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                plugin.getEquineManager().getEquineLeveling().syncExpBar(player);
            });
        }, 0L , 20L);
    }
}
