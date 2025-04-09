package endcrypt.equinoxEquestrian.commands.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand {

    private final EquinoxEquestrian plugin;

    public SetHomeCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        if (args.length < 2 || !(args[1].equalsIgnoreCase("stall") || args[1].equalsIgnoreCase("pasture"))) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /" + args[0] + " sethome pasture/stall");
            return;
        }

        plugin.getEquineHandler().getEquineHome().setHome(player, args[1]);

        // Add your logic to set the home here
    }
}
