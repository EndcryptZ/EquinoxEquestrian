package endcrypt.equinoxEquestrian.commands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    private final EquinoxEquestrian plugin;
    public MainCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {


        if (args.length >= 1) {

            if (args[0].equalsIgnoreCase("menu")) {

                if (!(commandSender instanceof Player player)) {
                    commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
                    return false;
                }


                plugin.getBuildAHorseMenu().openDefault(player);
            }
        }
        return true;
    }
}
