package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand {

    private final EquinoxEquestrian plugin;

    public SetHomeCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, Command command, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        if (args.length < 2 || !(args[1].equalsIgnoreCase("stall") || args[1].equalsIgnoreCase("pasture"))) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /" + command.getLabel() + " sethome pasture/stall");
            return;
        }

        plugin.getEquineHandler().getEquineHome().setHome(player, args[1]);

        // Add your logic to set the home here
    }
}
