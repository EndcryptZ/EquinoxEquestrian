package endcrypt.equinox.menu.shop.consumables;

import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ShopConsumablesMenu {

    private final EquinoxEquestrian plugin;

    public ShopConsumablesMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        player.openInventory(createMenu(player));

    }

    private Inventory createMenu(Player player) {
        SGMenu gui = plugin.getSpiGUI().create("Consumables", 1, "Consumables");

        return gui.getInventory();
    }


}
