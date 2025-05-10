package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EquineAdminCommand {

    private final EquinoxEquestrian plugin;
    public EquineAdminCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands() {
        new CommandAPICommand("equineadmin")
                .withAliases("eqadmin")
                .withSubcommand(new CommandAPICommand("token")
                        .withArguments(new MultiLiteralArgument("action", "set", "give", "take"))
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes(this::token));
    }

    private void token(CommandSender commandSender, CommandArguments args) {
        String action = (String) args.get("action");
        Player target = (Player) args.get("player");
        int amount = (int) args.get("amount");
        String targetName = target.getName();

        switch (action.toLowerCase()) {
            case "set":
                setTokens(target, amount);
                commandSender.sendMessage(ColorUtils.color("<green>Set " + amount + " tokens for " + targetName));
                break;
            case "give":
                giveTokens(target, amount);
                commandSender.sendMessage(ColorUtils.color("<green>Gave " + amount + " tokens to " + targetName));
                break;
            case "take":
                takeTokens(target, amount);
                commandSender.sendMessage(ColorUtils.color("<green>Took " + amount + " tokens from " + targetName));
                break;
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
