package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;


public class EquineBreeding {

    private final EquinoxEquestrian plugin;
    public EquineBreeding(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineBreedingTask(plugin);
    }


}
