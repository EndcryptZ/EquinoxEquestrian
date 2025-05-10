package endcrypt.equinox.commands.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.commands.horse.subcommands.InfoCommand;
import endcrypt.equinox.commands.horse.subcommands.ListCommand;
import endcrypt.equinox.commands.horse.subcommands.TokensCommand;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HorseCommand implements CommandExecutor {


    private final EquinoxEquestrian plugin;
    private final ListCommand listCommand;
    private final TokensCommand tokensCommand;
    private final InfoCommand infoCommand;

    public HorseCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.listCommand = new ListCommand(plugin);
        this.tokensCommand = new TokensCommand(plugin);
        this.infoCommand = new InfoCommand(plugin);
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
                case "info":
                    infoCommand.execute(commandSender, args);
                    break;
                default:
                    commandSender.sendMessage(ColorUtils.color("<red>Unknown command."));
            }
        }
        return true;
    }
}
