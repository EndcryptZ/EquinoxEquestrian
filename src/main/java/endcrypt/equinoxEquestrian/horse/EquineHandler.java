package endcrypt.equinoxEquestrian.horse;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;

public class EquineHandler {

    private final EquineGaits equineGaits;
    private final EquineCrossTie equineCrossTie;
    private final EquineLunge equineLunge;
    private final EquineHome equineHome;
    private final EquineGroom equineGroom;
    private final EquineItems equineItems;

    private final EquinoxEquestrian plugin;
    public EquineHandler(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        equineGaits = new EquineGaits(plugin);
        equineCrossTie = new EquineCrossTie(plugin);
        equineLunge = new EquineLunge(plugin);
        equineHome = new EquineHome(plugin);
        equineGroom = new EquineGroom(plugin);
        equineItems = new EquineItems(plugin);

    }

    public EquineLunge getEquineLunge() {
        return equineLunge;
    }

    public EquineHome getEquineHome() {
        return equineHome;
    }
}
