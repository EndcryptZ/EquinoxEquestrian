package endcrypt.equinoxEquestrian.commands.horsesubcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    private final EquinoxEquestrian plugin;

    public ListCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command is only available for players!");
            return;
        }

        plugin.getHorseMenu().getHorseListMenu().open(player);
    }


}
