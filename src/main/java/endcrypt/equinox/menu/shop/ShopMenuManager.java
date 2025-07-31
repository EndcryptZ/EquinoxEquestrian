package endcrypt.equinox.menu.shop;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.shop.manure.ShopManureMenu;
import lombok.Getter;

@Getter
public class ShopMenuManager {

    private final EquinoxEquestrian plugin;
    private final ShopManureMenu shopManureMenu;
    public ShopMenuManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        shopManureMenu = new ShopManureMenu(plugin);
    }
}
