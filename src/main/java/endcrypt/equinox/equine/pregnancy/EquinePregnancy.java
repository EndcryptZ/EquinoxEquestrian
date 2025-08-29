package endcrypt.equinox.equine.pregnancy;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.utils.TaskUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

public class EquinePregnancy {

    private final EquinoxEquestrian plugin;
    @Getter
    private ArrayList<AbstractHorse> pregnantHorses = new ArrayList<>();

    public EquinePregnancy(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquinePregnancyTask(plugin);
        TaskUtils.runTaskLater(2, this::addAllLoadedPregnantHorses);
    }

    public void add(AbstractHorse horse) {
        pregnantHorses.add(horse);
    }

    public void remove(AbstractHorse horse) {
        pregnantHorses.remove(horse);
    }

    public boolean contains(AbstractHorse horse) {
        return pregnantHorses.contains(horse);
    }

    public void removeAll() {
        pregnantHorses.clear();
    }

    public int getPregnantCount() {
        return pregnantHorses.size();
    }

    private void addAllLoadedPregnantHorses() {
        plugin.getLogger().info("[Equine] Loading all currently pregnant horses from loaded worlds...");
        for (World world : Bukkit.getWorlds()) {
            for (AbstractHorse abstractHorse : world.getEntitiesByClass(AbstractHorse.class)) {
                if (!EquineUtils.isLivingEquineHorse(abstractHorse)) continue;

                if (Keys.readBoolean(abstractHorse, Keys.IS_PREGNANT)) {
                    pregnantHorses.add(abstractHorse);
                }
            }
        }
        plugin.getLogger().info("[Equine] Loaded " + pregnantHorses.size() + " pregnant horses from loaded worlds.");
    }
}
