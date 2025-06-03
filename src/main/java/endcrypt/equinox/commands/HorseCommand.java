package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
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
                        .executes(this::tokens))

                .withSubcommand(new CommandAPICommand("lunge")
                        .executesPlayer(this::lunge))

                .withSubcommand(new CommandAPICommand("menu")
                        .executes(this::openMenu))

                .withSubcommand(new CommandAPICommand("sethome")
                        .withArguments(new MultiLiteralArgument("home", "stall", "pasture"))
                        .executesPlayer(this::horseSetHome))

                .withSubcommand(new CommandAPICommand("tp")
                        .withAliases("teleport")
                        .withArguments(new MultiLiteralArgument("home", "stall", "pasture").setOptional(true))
                        .executesPlayer(this::horseTeleportHome))

                .withSubcommand(new CommandAPICommand("tphere")
                        .withAliases("teleporthere")
                        .executesPlayer(this::horseTeleportHere))

                .register();
    }

    private void info(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You have not selected a horse!"));
            return;
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);

        plugin.getHorseMenuManager().getHorseInfoMenu().open(player, equineLiveHorse, ListOrganizeType.AGE);
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


    private void lunge(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        plugin.getEquineManager().getEquineLunge().lungeHorse(player);
    }

    private void openMenu(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if (plugin.getFloodgateApi().isFloodgatePlayer(player.getUniqueId())) {
            plugin.getBedrockBuildForm().openDefault(player);
            return;
        }
        plugin.getBuildMenuManager().getBuildMenu().openDefault(player);
    }

    private void horseSetHome(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        String home = (String) args.get("home");
        plugin.getEquineManager().getEquineHome().setHome(player, home);

    }

    private void horseTeleportHome(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        String home = (String) args.get("home");

        if(home != null) {
            plugin.getEquineManager().getEquineHome().teleportHome(player, home);
            return;
        }

        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        player.teleport(horse);
        player.sendMessage(ColorUtils.color("<prefix><green>You have been teleported to your selected horse!",
                Placeholder.parsed("prefix", plugin.getPrefix())));

    }

    private void horseTeleportHere(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        horse.teleport(player.getLocation());
        player.sendMessage(ColorUtils.color("<prefix><green>You have been teleported to your selected horse!",
                Placeholder.parsed("prefix", plugin.getPrefix())));
    }

}
