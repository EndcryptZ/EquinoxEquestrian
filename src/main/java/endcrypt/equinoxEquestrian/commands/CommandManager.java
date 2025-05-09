package endcrypt.equinoxEquestrian.commands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.commands.equine.EquineCommand;
import endcrypt.equinoxEquestrian.commands.equine.EquineCommandTabCompleter;
import endcrypt.equinoxEquestrian.commands.equineadmin.EquineAdminCommand;
import endcrypt.equinoxEquestrian.commands.equineadmin.EquineAdminTabCompleter;
import endcrypt.equinoxEquestrian.commands.horse.HorseCommand;
import endcrypt.equinoxEquestrian.commands.horse.HorseCommandTabCompeter;

public class CommandManager {

    private final EquinoxEquestrian plugin;
    public CommandManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        plugin.getServer().getPluginCommand("eq").setExecutor(new EquineCommand(plugin));
        plugin.getServer().getPluginCommand("eq").setTabCompleter(new EquineCommandTabCompleter());

        plugin.getServer().getPluginCommand("horse").setExecutor(new HorseCommand(plugin));
        plugin.getServer().getPluginCommand("horse").setTabCompleter(new HorseCommandTabCompeter());

        plugin.getServer().getPluginCommand("eqadmin").setExecutor(new EquineAdminCommand(plugin));
        plugin.getServer().getPluginCommand("eqadmin").setTabCompleter(new EquineAdminTabCompleter());
    }
}
