package endcrypt.equinoxEquestrian.commands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.commands.horsesubcommands.ListCommand;
import endcrypt.equinoxEquestrian.commands.horsesubcommands.TokensCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class HorseCommand implements CommandExecutor {


    private final EquinoxEquestrian plugin;
    private final ListCommand listCommand;
    private final TokensCommand tokensCommand;

    public HorseCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.listCommand = new ListCommand(plugin);
        this.tokensCommand = new TokensCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "list":
                    listCommand.execute(commandSender, args);
                    break;
                case "tokens":
                    tokensCommand.execute(commandSender, command, args);
                    break;
                default:
                    commandSender.sendMessage("Unknown command.");
            }
        }
        return true;
    }
}
