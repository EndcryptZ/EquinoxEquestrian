package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class TpHereCommand {

    private final EquinoxEquestrian plugin;

    public TpHereCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }


        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou have not selected a horse!"));
            return;
        }

        horse.teleport(player.getLocation());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&f" + horse.getName() + " &7has been teleported to your location."));


    }
}
