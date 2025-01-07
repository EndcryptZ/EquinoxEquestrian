package endcrypt.equinoxEquestrian;

import com.samjakob.spigui.SpiGUI;
import endcrypt.equinoxEquestrian.commands.MainCommand;
import endcrypt.equinoxEquestrian.commands.MainCommandTabCompleter;
import endcrypt.equinoxEquestrian.menu.build.BuildMenu;
import endcrypt.equinoxEquestrian.menu.build.select.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class EquinoxEquestrian extends JavaPlugin {

    private SpiGUI spiGUI;
    private Economy econ;

    private BuildMenu buildAHorseMenu;



    @Override
    public void onEnable() {

        spiGUI = new SpiGUI(this);

        buildAHorseMenu = new BuildMenu(this);


        getServer().getPluginCommand("eq").setExecutor(new MainCommand(this));
        getServer().getPluginCommand("eq").setTabCompleter(new MainCommandTabCompleter());
        // Plugin startup logic

        setupEconomy();
        if(econ == null) {
            getServer().getLogger().severe("&cCouldn't find any economy provider plugin. Disabling the Plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BuildMenu getBuildAHorseMenu() {
        return buildAHorseMenu;
    }

    public SpiGUI getSpiGUI() {
        return spiGUI;
    }

    public String getPrefix() {
        return "§8[§eEquinox§8] ";
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
}
