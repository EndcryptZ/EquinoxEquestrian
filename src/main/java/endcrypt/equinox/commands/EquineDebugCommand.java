package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.HeadUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EquineDebugCommand {

    private final EquinoxEquestrian plugin;
    public EquineDebugCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands() {
        new CommandAPICommand("equinedebug")
                .withSubcommand(new CommandAPICommand("debugplayerhead")
                        .executesPlayer(this::debugPlayerHead))

                .register();
    }

    private void debugPlayerHead(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if(!isExecutorDeveloper(player)) {
            return;
        }

        HeadUtils.placeHeadFromHDB(player.getLocation(), "1682");
    }


    private boolean isExecutorDeveloper(Player player) {
        if (!player.getName().equalsIgnoreCase("Endcrypt")) {
            player.sendMessage(ColorUtils.color("<red>This command is reserved for the plugin developer (Endcrypt)."));
            player.sendMessage(ColorUtils.color("<red>Warning: Using debug commands incorrectly can severely break the plugin or corrupt server data. Only use on a development server."));
            return false;
        }

        return true;
    }
}
