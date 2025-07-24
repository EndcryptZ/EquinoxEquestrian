package endcrypt.equinox.config;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;

@Getter
public class ConfigManager {

    private final EquinoxEquestrian plugin;
    private final ConfigMain configMain;
    private final ConfigShop configShop;

    public ConfigManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        configMain = new ConfigMain(plugin);
        configShop = new ConfigShop(plugin);
    }
}
