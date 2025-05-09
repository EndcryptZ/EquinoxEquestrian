package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand {

    private final EquinoxEquestrian plugin;

    public MenuCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ColorUtils.color("<red>This command is only available for players!"));
            return;
        }


        if (plugin.getFloodgateApi().isFloodgatePlayer(player.getUniqueId())) {
            plugin.getBedrockBuildForm().openDefault(player);
            return;
        }
        plugin.getBuildMenu().openDefault(player);
    }
}
