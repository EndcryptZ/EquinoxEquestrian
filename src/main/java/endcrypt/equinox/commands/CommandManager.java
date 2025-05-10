package endcrypt.equinox.commands;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.commands.horse.HorseCommand;

public class CommandManager {

    private final EquinoxEquestrian plugin;
    public CommandManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;

    }

    public void registerCommands() {
        new EquineCommand(plugin);
        new HorseCommand(plugin);
    }
}
