package endcrypt.equinoxEquestrian;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import endcrypt.equinoxEquestrian.bedrock.menu.BedrockBuildForm;
import endcrypt.equinoxEquestrian.commands.CommandManager;
import endcrypt.equinoxEquestrian.database.DatabaseManager;
import endcrypt.equinoxEquestrian.equine.crosstie.EquineCrossTieListener;
import endcrypt.equinoxEquestrian.equine.groom.EquineGroomListener;
import endcrypt.equinoxEquestrian.hooks.placeholderapi.Placeholders;
import endcrypt.equinoxEquestrian.equine.EquineManager;
import endcrypt.equinoxEquestrian.menu.build.BuildMenuListener;
import endcrypt.equinoxEquestrian.menu.build.BuildMenuManager;
import endcrypt.equinoxEquestrian.menu.horse.HorseMenuListener;
import endcrypt.equinoxEquestrian.menu.horse.HorseMenuManager;
import endcrypt.equinoxEquestrian.player.data.PlayerDataListener;
import endcrypt.equinoxEquestrian.player.data.PlayerDataManager;
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
    private BuildMenuManager buildMenuManager;
    private BedrockBuildForm bedrockBuildForm;
    private HorseMenuManager horseMenuManager;
    private EquineManager equineManager;
    private ProtocolManager protocolManager;
    private PlayerDataManager playerDataManager;
    private DatabaseManager databaseManager;
    private CommandManager commandManager;
    private FloodgateApi floodgateApi;

    @Override
    public void onEnable() {
        this.initializeInstances();
        this.initializeListeners();
        this.setupEconomy();
        this.commandManager.registerCommands();

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
        buildMenuManager = new BuildMenuManager(this);
        bedrockBuildForm = new BedrockBuildForm(this);
        horseMenuManager = new HorseMenuManager(this);
        equineManager = new EquineManager(this);
        playerDataManager = new PlayerDataManager(this);
        commandManager = new CommandManager(this);

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

    public BuildMenuManager getBuildMenuManager() {
        return buildMenuManager;
    }

    public EquineManager getEquineManager() {
        return equineManager;
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

    public BedrockBuildForm getBedrockBuildForm() {
        return bedrockBuildForm;
    }

    public FloodgateApi getFloodgateApi() {
        return floodgateApi;
    }
}
