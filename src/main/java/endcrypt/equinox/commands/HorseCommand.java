package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.horse.internal.ListOrganizeType;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class HorseCommand {


    private final EquinoxEquestrian plugin;
    public HorseCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands() {
        new CommandAPICommand("horse")
                .withAliases("h")
                .withSubcommand(new CommandAPICommand("info")
                        .executesPlayer(this::info))

                .withSubcommand(new CommandAPICommand("list")
                        .executesPlayer(this::list))

                .withSubcommand(new CommandAPICommand("tokens")
                        .withArguments(new PlayerArgument("player").setOptional(true))
                        .executes(this::tokens));
    }

    private void info(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You have not selected a horse!"));
            return;
        }

        plugin.getHorseMenuManager().getHorseInfoMenu().open(player, horse, ListOrganizeType.AGE);
    }

    private void list(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        plugin.getHorseMenuManager().getHorseListMenu().open(player, ListOrganizeType.AGE);
    }

    private void tokens(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        Player target = (Player) args.get("player");

        if(target != null) {
            sender.sendMessage(ColorUtils.color(plugin.getPrefix() + "<green><target>'s tokens: <gold><tokens>",
                    Placeholder.parsed("target", target.getName()),
                    Placeholder.parsed("tokens", String.valueOf(plugin.getTokenManager().getTokens(target)))));
            return;
        }

        sender.sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>Your tokens: <gold><tokens>",
                Placeholder.parsed("tokens", String.valueOf(plugin.getTokenManager().getTokens(player)))));

    }

}
