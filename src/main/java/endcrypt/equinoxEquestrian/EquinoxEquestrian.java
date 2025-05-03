package endcrypt.equinoxEquestrian;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import endcrypt.equinoxEquestrian.bedrock.menu.BedrockBuildForm;
import endcrypt.equinoxEquestrian.commands.HorseCommand;
import endcrypt.equinoxEquestrian.commands.HorseCommandTabCompeter;
import endcrypt.equinoxEquestrian.commands.MainCommand;
import endcrypt.equinoxEquestrian.commands.MainCommandTabCompleter;
import endcrypt.equinoxEquestrian.database.DatabaseManager;
import endcrypt.equinoxEquestrian.hooks.placeholderapi.Placeholders;
import endcrypt.equinoxEquestrian.horse.EquineHandler;
import endcrypt.equinoxEquestrian.menu.build.BuildMenu;
import endcrypt.equinoxEquestrian.menu.horse.HorseMenu;
import endcrypt.equinoxEquestrian.player.PlayerManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
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
    private BuildMenu buildMenu;
    private BedrockBuildForm bedrockBuildForm;
    private HorseMenu horseMenu;
    private EquineHandler equineHandler;
    private ProtocolManager protocolManager;
    private PlayerManager playerManager;
    private DatabaseManager databaseManager;
    private HeadDatabaseAPI headDatabaseAPI;
    private FloodgateApi floodgateApi;




    @Override
    public void onEnable() {
        initializeInstances();
        setupEconomy();
        registerCommands();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new Placeholders(this).register(); //
        }


    }

    @Override
    public void onDisable() {
        try {
            // Close the database connection when the plugin is disabled
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Plugin shutdown logic
    }

    private void initializeInstances() {
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

        floodgateApi = FloodgateApi.getInstance();
        protocolManager = ProtocolLibrary.getProtocolManager();
        spiGUI = new SpiGUI(this);
        buildMenu = new BuildMenu(this);
        bedrockBuildForm = new BedrockBuildForm(this);
        horseMenu = new HorseMenu(this);
        equineHandler = new EquineHandler(this);
        playerManager = new PlayerManager(this);
        headDatabaseAPI = new HeadDatabaseAPI();

        for (Player player : Bukkit.getOnlinePlayers()) {
            playerManager.addPlayer(player);
        }
    }

    private void registerCommands() {
        getServer().getPluginCommand("eq").setExecutor(new MainCommand(this));
        getServer().getPluginCommand("eq").setTabCompleter(new MainCommandTabCompleter());
        getServer().getPluginCommand("horse").setExecutor(new HorseCommand(this));
        getServer().getPluginCommand("horse").setTabCompleter(new HorseCommandTabCompeter());
    }

    public BuildMenu getBuildMenu() {
        return buildMenu;
    }

    public HorseMenu getHorseMenu() {
        return horseMenu;
    }

    public EquineHandler getEquineHandler() {
        return equineHandler;
    }

    public SpiGUI getSpiGUI() {
        return spiGUI;
    }

    public String getPrefix() {
        return "§8[§bEquinox§8] ";
    }

    public Economy getEcon() {
        return econ;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
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

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public HeadDatabaseAPI getHeadDatabaseAPI() {
        return headDatabaseAPI;
    }

    public BedrockBuildForm getBedrockBuildForm() {
        return bedrockBuildForm;
    }

    public FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }
}
