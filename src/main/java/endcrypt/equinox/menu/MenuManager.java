package endcrypt.equinox.menu;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.build.BuildMenuManager;
import endcrypt.equinox.menu.horse.HorseMenuManager;
import endcrypt.equinox.menu.shop.ShopMenuManager;
import lombok.Getter;

@Getter
public class MenuManager {

    private final EquinoxEquestrian plugin;
    private final BuildMenuManager buildMenuManager;
    private final HorseMenuManager horseMenuManager;
    private final ShopMenuManager shopMenuManager;

    public MenuManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        buildMenuManager = new BuildMenuManager(plugin);
        horseMenuManager = new HorseMenuManager(plugin);
        shopMenuManager = new ShopMenuManager(plugin);
    }
}
