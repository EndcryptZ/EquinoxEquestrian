package endcrypt.equinox;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import endcrypt.equinox.bedrock.menu.BedrockBuildForm;
import endcrypt.equinox.commands.CommandManager;
import endcrypt.equinox.database.DatabaseManager;
import endcrypt.equinox.equine.crosstie.EquineCrossTieListener;
import endcrypt.equinox.equine.groom.EquineGroomListener;
import endcrypt.equinox.hooks.placeholderapi.Placeholders;
import endcrypt.equinox.equine.EquineManager;
import endcrypt.equinox.menu.build.BuildMenuListener;
import endcrypt.equinox.menu.build.BuildMenuManager;
import endcrypt.equinox.menu.horse.HorseMenuListener;
import endcrypt.equinox.menu.horse.HorseMenuManager;
import endcrypt.equinox.player.data.PlayerDataListener;
import endcrypt.equinox.player.data.PlayerDataManager;
import endcrypt.equinox.token.TokenManager;
import endcrypt.equinox.updater.horse.HorseNBTUpdaterListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.sql.SQLException;

public final class EquinoxEquestrian extends JavaPlugin {

    private SpiGUI spiGUI;
    private Economy econ;
    private BedrockBuildForm bedrockBuildForm;
    private FloodgateApi floodgateApi;

    private BuildMenuManager buildMenuManager;
    private HorseMenuManager horseMenuManager;
    private EquineManager equineManager;
    private ProtocolManager protocolManager;
    private PlayerDataManager playerDataManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private TokenManager tokenManager;

    @Override
    public void onLoad() {
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
        this.initializeDatabase();
        floodgateApi = FloodgateApi.getInstance();
        spiGUI = new SpiGUI(this);
        bedrockBuildForm = new BedrockBuildForm(this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        buildMenuManager = new BuildMenuManager(this);
        horseMenuManager = new HorseMenuManager(this);
        equineManager = new EquineManager(this);
        playerDataManager = new PlayerDataManager(this);
        commandManager = new CommandManager(this);
        tokenManager = new TokenManager(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerDataManager.addPlayer(player);
        }
    }

    private void initializeListeners() {
        new PlayerDataListener(this);
        new BuildMenuListener(this);
        new HorseMenuListener(this);
        new EquineCrossTieListener(this);
        new EquineGroomListener(this);
        new HorseNBTUpdaterListener(this);

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

        if(econ == null) {
            getServer().getLogger().severe("&cCouldn't find any economy provider plugin. Disabling the Plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    private void unloadDatabase() {
        try {
            // Close the database connection when the plugin is disabled
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase() {
        try {
            // Ensure the plugin's data folder exists
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            // Initialize the DatabaseManager with the path to the database file
            // we make it 'databaseManager =' because we will use it when registering events.
            // The file name will be 'database.db' but you can change that here.
            databaseManager = new DatabaseManager(this, getDataFolder().getAbsolutePath() + "/database.db");
        } catch (SQLException e) {
            e.printStackTrace();
            // Disable the plugin if the database connection fails, because we don't want enabled plugin with no functionality.
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void loadPlaceholderAPI() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new Placeholders(this).register(); //
        }
    }

    public SpiGUI getSpiGUI() {
        return spiGUI;
    }
    public String getPrefix() {
        return "<dark_gray>[<aqua>Equinox<dark_gray>] ";
    }
    public Economy getEcon() {
        return econ;
    }
    public BedrockBuildForm getBedrockBuildForm() {
        return bedrockBuildForm;
    }
    public FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }

    public BuildMenuManager getBuildMenuManager() {
        return buildMenuManager;
    }
    public EquineManager getEquineManager() {
        return equineManager;
    }
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    public HorseMenuManager getHorseMenuManager() {
        return horseMenuManager;
    }
    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    public TokenManager getTokenManager() {
        return tokenManager;
    }

}
