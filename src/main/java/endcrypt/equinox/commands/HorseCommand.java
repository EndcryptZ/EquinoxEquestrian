package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerUntrustEvent;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.menu.horse.internal.ListOrganizeType;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
                        .withArguments(new StringArgument("target")
                                .setOptional(true)
                                .replaceSuggestions(ArgumentSuggestions.strings(Bukkit.getOnlinePlayers().stream()
                                .map(Player::getName)
                                .toArray(String[]::new)))
                                .withPermission("equinox.cmd.horse.list.others"))
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

                .withSubcommand(new CommandAPICommand("set")
                        .withArguments(new MultiLiteralArgument("privacy", "private", "public"))
                        .executesPlayer(this::horseSetPrivacy))

                .withSubcommand(new CommandAPICommand("trust")
                        .withArguments(new PlayerArgument("target"))
                        .executesPlayer(this::trust))

                .withSubcommand(new CommandAPICommand("untrust")
                        .withArguments(new StringArgument("target").replaceSuggestions(ArgumentSuggestions.strings(Bukkit.getOnlinePlayers().stream()
                                .map(Player::getName)
                                .toArray(String[]::new))))
                        .executesPlayer(this::untrust))
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

        plugin.getHorseMenuManager().getHorseInfoMenu().open(player, equineLiveHorse, ListOrganizeType.AGE, false);
    }

    private void list(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        String iniTarget = args.getUnchecked("target");

        if(iniTarget == null) {
            plugin.getHorseMenuManager().getHorseListMenu().open(player, ListOrganizeType.AGE, false);
            return;
        }

        Player target = Bukkit.getPlayer(iniTarget);
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(iniTarget);

        if (offlineTarget.hasPlayedBefore()) {
            plugin.getHorseMenuManager().getHorseListMenu().openToOther(player, offlineTarget, ListOrganizeType.AGE, false);
            return;
        }

        if (target == null) {
            target = Bukkit.getOfflinePlayer(iniTarget).getPlayer();
        }

        if(target == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>Player named <target> is not found!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", iniTarget)));
            return;
        }

        plugin.getHorseMenuManager().getHorseListMenu().openToOther(player, target, ListOrganizeType.AGE, false);

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

    private void horseSetPrivacy(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        String privacy = (String) args.get("privacy");

        String prefix = plugin.getPrefix();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", prefix)));
            return;
        }

        boolean isPublic = EquineUtils.isHorsePublic(horse);

        if (privacy.equalsIgnoreCase("public")) {
            if (isPublic) {
                player.sendMessage(ColorUtils.color("<prefix><red>Your horse is already public!",
                        Placeholder.parsed("prefix", prefix)));
            } else {
                plugin.getEquineManager().getEquinePrivacy().setPrivacy(true, horse);
                player.sendMessage(ColorUtils.color("<prefix><green>Your horse is now public!",
                        Placeholder.parsed("prefix", prefix)));
            }
        } else if (privacy.equalsIgnoreCase("private")) {
            if (!isPublic) {
                player.sendMessage(ColorUtils.color("<prefix><red>Your horse is already private!",
                        Placeholder.parsed("prefix", prefix)));
            } else {
                plugin.getEquineManager().getEquinePrivacy().setPrivacy(false, horse);
                player.sendMessage(ColorUtils.color("<prefix><green>Your horse is now private!",
                        Placeholder.parsed("prefix", prefix)));
            }
        }
    }

    private void trust(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        Player target = (Player) args.get("target");
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        if (horse.getOwner().equals(target)) {
            player.sendMessage(ColorUtils.color(
                    "<prefix><red>You cannot trust the owner of the horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }


        if (plugin.getDatabaseManager().isTrustedToHorse(horse, target)) {
            player.sendMessage(ColorUtils.color("<prefix><red><target> is already trusted!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", target.getName())));
            return;
        }
        plugin.getDatabaseManager().addTrustedPlayer(horse, target);
        player.sendMessage(ColorUtils.color("<prefix><green><target> has been trusted!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("target", target.getName())));
        target.sendMessage(ColorUtils.color("<prefix><green><player> has trusted you to <horse>!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("player", player.getName()),
                Placeholder.parsed("horse", horse.getName())));
    }

    private void untrust(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        String iniTarget = args.getUnchecked("target");
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        Player target = Bukkit.getPlayer(iniTarget);
        OfflinePlayer offlineTarget = (target != null) ? target : Bukkit.getOfflinePlayer(iniTarget);

        if (!offlineTarget.hasPlayedBefore()) {
            player.sendMessage(ColorUtils.color("<prefix><red>Player named <target> is not found!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", iniTarget)));
            return;
        }

        if (horse.getOwner().equals(offlineTarget)) {
            player.sendMessage(ColorUtils.color("<prefix><red>You can't untrust the owner of the horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        if (!plugin.getDatabaseManager().isTrustedToHorse(horse, offlineTarget)) {
            player.sendMessage(ColorUtils.color("<prefix><red><target> is not trusted!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", offlineTarget.getName())));
            return;
        }

        // Call event and untrust
        EquinePlayerUntrustEvent event = new EquinePlayerUntrustEvent(horse, player, target);
        Bukkit.getPluginManager().callEvent(event);
        plugin.getDatabaseManager().removeTrustedPlayer(horse, offlineTarget);

        player.sendMessage(ColorUtils.color("<prefix><green><target> has been untrusted!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("target", offlineTarget.getName())));

        if (offlineTarget.isOnline()) {
            Player onlineTarget = Bukkit.getPlayer(offlineTarget.getUniqueId());
            if (onlineTarget != null) {
                onlineTarget.sendMessage(ColorUtils.color("<prefix><green><player> has untrusted you from <horse>!",
                        Placeholder.parsed("prefix", plugin.getPrefix()),
                        Placeholder.parsed("player", player.getName()),
                        Placeholder.parsed("horse", horse.getName())));
            }
        }
    }

}
