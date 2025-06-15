package endcrypt.equinox.equine.crosstie;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

@Getter
public class EquineCrossTie {

    private final EquinoxEquestrian plugin;
    private final ArrayList<AbstractHorse> crosstiedHorses = new ArrayList<>();

    public EquineCrossTie(EquinoxEquestrian plugin){
        this.plugin = plugin;
        new EquineCrossTieTask(plugin);
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



}
