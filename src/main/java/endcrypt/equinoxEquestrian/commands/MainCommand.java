package endcrypt.equinoxEquestrian.commands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.commands.subcommands.GiveCommand;
import endcrypt.equinoxEquestrian.commands.subcommands.MenuCommand;
import endcrypt.equinoxEquestrian.commands.subcommands.LungeCommand;
import endcrypt.equinoxEquestrian.commands.subcommands.SetHomeCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final EquinoxEquestrian plugin;
    private final MenuCommand menuCommand;
    private final LungeCommand lungeCommand;
    private final SetHomeCommand setHomeCommand;
    private final GiveCommand giveCommand;

    public MainCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.menuCommand = new MenuCommand(plugin);
        this.lungeCommand = new LungeCommand(plugin);
        this.setHomeCommand = new SetHomeCommand(plugin);
        this.giveCommand = new GiveCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "menu":
                    menuCommand.execute(commandSender, args);
                    break;
                case "lunge":
                    lungeCommand.execute(commandSender, args);
                    break;
                case "sethome":
                    setHomeCommand.execute(commandSender, args);
                    break;
                case "give":
                    giveCommand.execute(commandSender, args);
                    break;
                default:
                    commandSender.sendMessage("Unknown command.");
            }
        }
        return true;
    }
}
