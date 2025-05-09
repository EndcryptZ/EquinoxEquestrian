package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class TeleportCommand {


    private final EquinoxEquestrian plugin;

    public TeleportCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, Command command, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        if (args.length < 2 || !(args[1].equalsIgnoreCase("stall") || args[1].equalsIgnoreCase("pasture"))) {
            AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();
            if (horse == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou have not selected a horse!"));
                return;
            }
            player.teleport(horse);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&fYou have been teleported to your selected horse!"));
            return;
        }

        plugin.getEquineHandler().getEquineHome().teleportHome(player, args[1]);
    }
}
