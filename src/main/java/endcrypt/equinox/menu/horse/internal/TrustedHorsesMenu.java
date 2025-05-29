package endcrypt.equinox.menu.horse.internal;

import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TrustedHorsesMenu {

    private final EquinoxEquestrian plugin;
    public TrustedHorsesMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {

    }

    private Inventory createMenu(Player player) {
        SGMenu gui = plugin.getSpiGUI().create("Trusted Horses", 3, "Trusted Horses");

        return gui.getInventory();
    }
}
