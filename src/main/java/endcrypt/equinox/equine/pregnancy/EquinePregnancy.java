package endcrypt.equinox.equine.pregnancy;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

public class EquinePregnancy {

    private final EquinoxEquestrian plugin;
    @Getter
    private ArrayList<AbstractHorse> pregnantHorses = new ArrayList<>();

    public EquinePregnancy(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquinePregnancyTask(plugin);
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


}
