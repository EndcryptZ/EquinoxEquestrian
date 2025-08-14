package endcrypt.equinox.equine.gaits;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EquineGaits {

    private final EquinoxEquestrian plugin;

    // Map to store the progress of the gait for each player
    private final Map<Player, Integer> playerCurrentProgress = new HashMap<>();
    private final Map<Player, Gaits> playerCurrentGaits = new HashMap<>();

    public EquineGaits(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineGaitsTask(plugin);
    }

}
