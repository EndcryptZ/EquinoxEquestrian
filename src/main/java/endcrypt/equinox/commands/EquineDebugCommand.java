package endcrypt.equinox.commands;

import com.maximde.hologramlib.hologram.RenderMode;
import com.maximde.hologramlib.hologram.TextHologram;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.HeadUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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

                .withSubcommand(new CommandAPICommand("debugholo")
                        .executesPlayer(this::debugHolo))

                .register();
    }

    private void debugPlayerHead(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if(!isExecutorDeveloper(player)) {
            return;
        }

        HeadUtils.placeHeadFromHDB(player.getLocation(), "1682");
    }

    private void debugHolo(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if(!isExecutorDeveloper(player)) {
            return;
        }

        String holoId = UUID.randomUUID().toString();
        TextHologram hologram = new TextHologram(holoId, RenderMode.NEARBY);
        hologram
                .setText("This is a test hologram!")
                .update();

        plugin.getHologramManager().spawn(hologram, player.getLocation().add(0, 1, 0));

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getHologramManager().remove(hologram);
        }, 60L);
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
