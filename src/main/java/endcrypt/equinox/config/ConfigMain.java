package endcrypt.equinox.config;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class ConfigMain {

    private final EquinoxEquestrian plugin;
    private ConfigurationSection databaseSection;

    public ConfigMain(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        initializeConfigFile();
        initializeConfigurationSections();
    }

    private void initializeConfigFile() {
        // Ensure the plugin's data folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Save the default config if it doesn't exist
        plugin.saveDefaultConfig();
    }

    private void initializeConfigurationSections() {
        databaseSection = plugin.getConfig().getConfigurationSection("database");
    }
}
