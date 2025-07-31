package endcrypt.equinox.equine.crosstie;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.TaskUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

@Getter
public class EquineCrossTie {

    private final EquinoxEquestrian plugin;
    private final ArrayList<AbstractHorse> crosstiedHorses = new ArrayList<>();

    public EquineCrossTie(EquinoxEquestrian plugin){
        this.plugin = plugin;
        new EquineCrossTieTask(plugin);
        TaskUtils.runTaskLater(2, this::addAllLoadedCrossTiedHorses);
    }

    public void add(AbstractHorse horse) {
        crosstiedHorses.add(horse);
    }

    public void remove(AbstractHorse horse) {
        crosstiedHorses.remove(horse);
    }

    public boolean contains(AbstractHorse horse) {
        return crosstiedHorses.contains(horse);
    }

    public void removeAll() {
        crosstiedHorses.clear();
    }

    private void addAllLoadedCrossTiedHorses() {
        plugin.getLogger().info("[Equine] Loading all currently cross-tied horses from loaded worlds...");
        for (World world : Bukkit.getWorlds()) {
            for (AbstractHorse abstractHorse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(abstractHorse)) continue;

                if (EquineUtils.isCrossTied(abstractHorse)) {
                    crosstiedHorses.add(abstractHorse);
                }
            }
        }
        plugin.getLogger().info("[Equine] Loaded " + crosstiedHorses.size() + " cross-tied horses from loaded worlds.");
    }

}
