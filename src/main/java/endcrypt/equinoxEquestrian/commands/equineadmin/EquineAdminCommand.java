package endcrypt.equinoxEquestrian.commands.equineadmin;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.commands.equineadmin.subcommands.TokenCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EquineAdminCommand implements CommandExecutor {

    private final EquinoxEquestrian plugin;

    private final TokenCommand tokenCommand;

    public EquineAdminCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.tokenCommand = new TokenCommand(plugin);

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "tokens":
                    tokenCommand.execute(commandSender, command, args);
                    break;
                default:
                    commandSender.sendMessage("Unknown command.");
            }
        }
        return true;
    }
}
