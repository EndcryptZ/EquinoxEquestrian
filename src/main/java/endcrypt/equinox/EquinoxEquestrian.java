package endcrypt.equinox;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import endcrypt.equinox.bedrock.menu.BedrockBuildForm;
import endcrypt.equinox.commands.CommandManager;
import endcrypt.equinox.config.ConfigManager;
import endcrypt.equinox.database.DatabaseManager;
import endcrypt.equinox.equine.breeding.EquineBreedingListener;
import endcrypt.equinox.equine.breeding.inheat.EquineBreedingInHeatListener;
import endcrypt.equinox.equine.bypass.EquineBypassListener;
import endcrypt.equinox.equine.crosstie.EquineCrossTieListener;
import endcrypt.equinox.equine.groom.EquineGroomListener;
import endcrypt.equinox.equine.leveling.EquineLevelingListener;
import endcrypt.equinox.equine.pregnancy.EquinePregnancyListener;
import endcrypt.equinox.equine.privacy.EquinePrivacyListener;
import endcrypt.equinox.equine.selector.EquineSelectorListener;
import endcrypt.equinox.token.TokenListener;
import endcrypt.equinox.equine.waste.EquineWasteListener;
import endcrypt.equinox.hologram.HologramManager;
import endcrypt.equinox.hooks.placeholderapi.Placeholders;
import endcrypt.equinox.equine.EquineManager;
import endcrypt.equinox.menu.MenuManager;
import endcrypt.equinox.menu.build.BuildMenuListener;
import endcrypt.equinox.menu.horse.HorseMenuListener;
import endcrypt.equinox.permissions.PermissionManager;
import endcrypt.equinox.player.data.PlayerDataListener;
import endcrypt.equinox.player.data.PlayerDataManager;
import endcrypt.equinox.token.TokenManager;
import endcrypt.equinox.updater.horse.HorseNBTUpdaterListener;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.sql.SQLException;

@Getter
public final class EquinoxEquestrian extends JavaPlugin {

    public static EquinoxEquestrian instance;

    private SpiGUI spiGUI;
    private Economy econ;
    private BedrockBuildForm bedrockBuildForm;
    private FloodgateApi floodgateApi;

    private MenuManager menuManager;
    private EquineManager equineManager;
    private ProtocolManager protocolManager;
    private PlayerDataManager playerDataManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private TokenManager tokenManager;
    private ConfigManager configManager;
    private PermissionManager permissionManager;
    private HologramManager hologramManager;

    @Setter
    private boolean databaseLoaded = false;

    @Override
    public void onLoad() {
        instance = this;
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
        );
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        this.initializeInstances();
        this.initializeListeners();
        this.setupEconomy();
        this.commandManager.registerCommands();
        this.loadPlaceholderAPI();


    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        this.unloadDatabase();
    }

    private void initializeInstances() {
        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);

        floodgateApi = FloodgateApi.getInstance();
        spiGUI = new SpiGUI(this);
        bedrockBuildForm = new BedrockBuildForm(this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        menuManager = new MenuManager(this);

        equineManager = new EquineManager(this);
        playerDataManager = new PlayerDataManager(this);
        commandManager = new CommandManager(this);
        tokenManager = new TokenManager(this);
        permissionManager = new PermissionManager(this);
        hologramManager = new HologramManager();
    }

    private void initializeListeners() {
        new PlayerDataListener(this);
        new BuildMenuListener(this);
        new HorseMenuListener(this);
        new EquineCrossTieListener(this);
        new EquineGroomListener(this);
        new HorseNBTUpdaterListener(this);
        new EquineSelectorListener(this);
        new EquinePrivacyListener(this);
        new EquineBypassListener(this);
        new EquineBreedingListener(this);
        new EquinePregnancyListener(this);
        new EquineBreedingInHeatListener(this);
        new EquineLevelingListener(this);
        new EquineWasteListener(this);
        new TokenListener(this);

    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }

        econ = rsp.getProvider();

        if(!econ.isEnabled() || econ == null) {
            getServer().getLogger().severe("&cCouldn't find any economy provider plugin. Disabling the Plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    private void unloadDatabase() {
        try {
            // Close the database connection when the plugin is disabled
            if (databaseManager != null) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    playerDataManager.save(player);
                }
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            this.getLogger().severe("Failed to close database connection: " + e.getMessage());
        }
    }

    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new Placeholders(this).register(); //
        }
    }

    public String getPrefix() {
        return "<dark_gray>[<aqua>Equinox<dark_gray>] ";
    }

}
