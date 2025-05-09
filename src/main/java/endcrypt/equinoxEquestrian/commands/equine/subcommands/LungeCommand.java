package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LungeCommand {

    private final EquinoxEquestrian plugin;

    public LungeCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ColorUtils.color("<red>This command is only available for players!"));
            return;
        }

        plugin.getEquineManager().getEquineLunge().lungeHorse(player);
    }
}
