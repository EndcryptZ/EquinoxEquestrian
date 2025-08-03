package endcrypt.equinox.menu.shop;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.shop.manure.ShopManureSellMenu;
import lombok.Getter;

@Getter
public class ShopMenuManager {

    private final EquinoxEquestrian plugin;
    private final ShopManureSellMenu shopManureMenu;
    public ShopMenuManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        shopManureMenu = new ShopManureSellMenu(plugin);
    }
}
