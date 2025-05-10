package endcrypt.equinox.commands.horse.subcommands;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.horse.internal.ListOrganizeType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class InfoCommand {

    private final EquinoxEquestrian plugin;

    public InfoCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou have not selected a horse!"));
            return;
        }

        plugin.getHorseMenuManager().getHorseInfoMenu().open(player, horse, ListOrganizeType.AGE);
    }
}
