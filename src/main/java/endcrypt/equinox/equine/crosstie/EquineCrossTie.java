package endcrypt.equinox.equine.crosstie;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;

@Getter
public class EquineCrossTie {

    private final EquinoxEquestrian plugin;
    private final ArrayList<AbstractHorse> leashedHorses = new ArrayList<>();

    public EquineCrossTie(EquinoxEquestrian plugin){
        this.plugin = plugin;
        new EquineCrossTieTask(plugin);
    }

    public void add(AbstractHorse horse) {
        leashedHorses.add(horse);
    }

    public void remove(AbstractHorse horse) {
        leashedHorses.remove(horse);
    }

    public boolean contains(AbstractHorse horse) {
        return leashedHorses.contains(horse);
    }

    public void removeAll() {
        leashedHorses.clear();
    }



}
