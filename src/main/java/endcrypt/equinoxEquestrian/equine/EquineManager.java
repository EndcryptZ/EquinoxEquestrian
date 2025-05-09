package endcrypt.equinoxEquestrian.equine;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.crosstie.EquineCrossTie;
import endcrypt.equinoxEquestrian.equine.gaits.EquineGaits;
import endcrypt.equinoxEquestrian.equine.groom.EquineGroomManager;
import endcrypt.equinoxEquestrian.equine.home.EquineHome;
import endcrypt.equinoxEquestrian.equine.invulnerable.EquineInvulnerable;
import endcrypt.equinoxEquestrian.equine.items.EquineItems;
import endcrypt.equinoxEquestrian.equine.lunge.EquineLunge;

public class EquineManager {

    private final EquineHorseBuilder equineHorseBuilder;
    private final EquineGaits equineGaits;
    private final EquineCrossTie equineCrossTie;
    private final EquineLunge equineLunge;
    private final EquineHome equineHome;
    private final EquineGroomManager equineGroomManager;
    private final EquineItems equineItems;
    private final EquineInvulnerable equineInvulnerable;
    private final EquineSelector equineSelector;
    private final EquineTeleport equineTeleport;

    private final EquinoxEquestrian plugin;
    public EquineManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        equineHorseBuilder = new EquineHorseBuilder(plugin);
        equineGaits = new EquineGaits(plugin);
        equineCrossTie = new EquineCrossTie(plugin);
        equineLunge = new EquineLunge(plugin);
        equineHome = new EquineHome(plugin);
        equineGroomManager = new EquineGroomManager(plugin);
        equineItems = new EquineItems(plugin);
        equineInvulnerable = new EquineInvulnerable(plugin);
        equineSelector = new EquineSelector(plugin);
        equineTeleport = new EquineTeleport(plugin);

    }

    public EquineLunge getEquineLunge() {
        return equineLunge;
    }

    public EquineHome getEquineHome() {
        return equineHome;
    }

    public EquineGroomManager getEquineGroomManager() {
        return equineGroomManager;
    }
}
