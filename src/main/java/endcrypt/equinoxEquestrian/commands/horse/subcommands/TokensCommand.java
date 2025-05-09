package endcrypt.equinoxEquestrian.commands.horse.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokensCommand {

    private final EquinoxEquestrian plugin;

    public TokensCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        if(args.length < 2) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + command.getLabel() +  " tokens <player>");
                return;
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&aYour tokens: &6" + plugin.getPlayerManager().getPlayerData(player).getTokens()));
        }

        if(args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&a" + target.getName() + "'s tokens: &6" + plugin.getPlayerManager().getPlayerData(target).getTokens()));
        }
    }
}
