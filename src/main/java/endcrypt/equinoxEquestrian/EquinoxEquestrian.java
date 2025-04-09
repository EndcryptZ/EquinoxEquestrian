package endcrypt.equinoxEquestrian;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.samjakob.spigui.SpiGUI;
import endcrypt.equinoxEquestrian.commands.MainCommand;
import endcrypt.equinoxEquestrian.commands.MainCommandTabCompleter;
import endcrypt.equinoxEquestrian.horse.EquineHandler;
import endcrypt.equinoxEquestrian.menu.build.BuildMenu;
import endcrypt.equinoxEquestrian.menu.horse.HorseMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EquinoxEquestrian extends JavaPlugin {

    private SpiGUI spiGUI;
    private Economy econ;

    private BuildMenu buildMenu;
    private HorseMenu horseMenu;

    private EquineHandler equineHandler;

    private ProtocolManager protocolManager;



    @Override
    public void onEnable() {


        protocolManager = ProtocolLibrary.getProtocolManager();

        setupEconomy();
        if(econ == null) {
            getServer().getLogger().severe("&cCouldn't find any economy provider plugin. Disabling the Plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

        spiGUI = new SpiGUI(this);

        buildMenu = new BuildMenu(this);
        horseMenu = new HorseMenu(this);
        equineHandler = new EquineHandler(this);

        getServer().getPluginManager().registerEvents(horseMenu, this);

        getServer().getPluginCommand("eq").setExecutor(new MainCommand(this));
        getServer().getPluginCommand("eq").setTabCompleter(new MainCommandTabCompleter());
        // Plugin startup logic


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();

    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}
