package endcrypt.equinox.commands.equineadmin.subcommands;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TokenCommand {

    private final EquinoxEquestrian plugin;

    public TokenCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, Command command, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /token <set|give|take> <player> <amount>");
            return;
        }

        String action = args[0];
        String playerName = args[1];
        int amount;

        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ColorUtils.color("<red>Amount must be a number."));
            return;
        }

        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage(ColorUtils.color("<red>Player not found."));
            return;
        }

        switch (action.toLowerCase()) {
            case "set":
                setTokens(target, amount);
                sender.sendMessage(ColorUtils.color("<green>Set " + amount + " tokens for " + playerName));
                break;
            case "give":
                giveTokens(target, amount);
                sender.sendMessage(ColorUtils.color("<green>Gave " + amount + " tokens to " + playerName));
                break;
            case "take":
                takeTokens(target, amount);
                sender.sendMessage(ColorUtils.color("<green>Took " + amount + " tokens from " + playerName));
                break;
            default:
                sender.sendMessage(ColorUtils.color("<red>Invalid action. Usage: /token <set|give|take> <player> <amount>")); // Send an error message for invalid action (C);
        }
    }

    private void setTokens(Player player, int amount) {
        plugin.getTokenManager().setTokens(player, amount);
    }

    private void giveTokens(Player player, int amount) {
        int currentTokens = plugin.getTokenManager().getTokens(player);
        plugin.getTokenManager().setTokens(player, currentTokens + amount);
    }

    private void takeTokens(Player player, int amount) {
        int currentTokens = plugin.getTokenManager().getTokens(player);
        int newAmount = Math.max(0, currentTokens - amount); // Prevent negative tokens
        plugin.getTokenManager().setTokens(player, newAmount);
    }
}
