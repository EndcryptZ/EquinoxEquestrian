package endcrypt.equinoxEquestrian.commands.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
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
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        plugin.getBuildMenu().openDefault(player);
    }
}
