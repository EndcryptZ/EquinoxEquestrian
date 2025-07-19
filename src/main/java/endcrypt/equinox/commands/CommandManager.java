package endcrypt.equinox.commands;

import endcrypt.equinox.EquinoxEquestrian;

public class CommandManager {

    private final EquinoxEquestrian plugin;
    public CommandManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;

    }

    public void registerCommands() {
        new HorseCommand(plugin);
        new EquineAdminCommand(plugin);
        new EquineDebugCommand(plugin);
    }
}
