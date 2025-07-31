package endcrypt.equinox.utils;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;

public class TaskUtils {

    public static void runTaskLater(int seconds, Runnable task) {
        Bukkit.getServer().getScheduler().runTaskLater(EquinoxEquestrian.instance, task, seconds * 20L);
    }
}
