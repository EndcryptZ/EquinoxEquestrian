package endcrypt.equinoxEquestrian.commands.horsesubcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.menu.horse.internal.ListOrganizeType;
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

        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou have not selected a horse!"));
            return;
        }

        plugin.getHorseMenu().getHorseInfoMenu().open(player, horse, ListOrganizeType.AGE);
    }
}
