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
import endcrypt.equinox.utils.CommandCooldownUtils;
import endcrypt.equinox.utils.TimeUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

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
                                .replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Bukkit.getOfflinePlayers())
                                        .map(OfflinePlayer::getName)
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
                        .withArguments(new StringArgument("target").replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Bukkit.getOfflinePlayers())
                                        .map(OfflinePlayer::getName)
                                        .toArray(String[]::new))))
                        .executesPlayer(this::trust))

                .withSubcommand(new CommandAPICommand("untrust")
                        .withArguments(new StringArgument("target").replaceSuggestions(ArgumentSuggestions.strings(Arrays.stream(Bukkit.getOfflinePlayers())
                                        .map(OfflinePlayer::getName)
                                        .toArray(String[]::new))))
                        .executesPlayer(this::untrust))

                .withSubcommand(new CommandAPICommand("manuremarket")
                        .withArguments(new PlayerArgument("target").setOptional(true)
                                .withPermission("equinox.cmd.horse.manuremarket.others"))
                        .executes(this::manureMarket))

                .withSubcommand(new CommandAPICommand("leads")
                        .executesPlayer(this::leads))

                .withSubcommand(new CommandAPICommand("water")
                        .executesPlayer(this::water))

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

        plugin.getMenuManager().getHorseMenuManager().getHorseInfoMenu().open(player, equineLiveHorse, ListOrganizeType.AGE, false);
    }

    private void list(CommandSender sender, CommandArguments args) {
        Player player = (Player) sender;
        String iniTarget = args.getUnchecked("target");

        if(iniTarget == null) {
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player, ListOrganizeType.AGE, false);
            return;
        }

        Player target = Bukkit.getPlayer(iniTarget);
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(iniTarget);

        if (offlineTarget.hasPlayedBefore()) {
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther(player, offlineTarget, ListOrganizeType.AGE, false);
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

        plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther(player, target, ListOrganizeType.AGE, false);

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
        plugin.getMenuManager().getBuildMenuManager().getBuildMenu().openDefault(player);
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
        player.sendMessage(ColorUtils.color("<prefix><green>Your selected horse has been teleported to you!",
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

        assert privacy != null;
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
        String iniTarget = args.getUnchecked("target");
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        assert iniTarget != null;
        Player target = Bukkit.getPlayer(iniTarget);
        OfflinePlayer offlineTarget = (target != null) ? target : Bukkit.getOfflinePlayer(iniTarget);

        if (!offlineTarget.hasPlayedBefore()) {
            player.sendMessage(ColorUtils.color("<prefix><red>Player named <target> is not found!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", iniTarget)));
            return;
        }

        if (Objects.equals(horse.getOwner(), offlineTarget)) {
            player.sendMessage(ColorUtils.color(
                    "<prefix><red>You cannot trust the owner of the horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }


        assert target != null;
        if (plugin.getDatabaseManager().getDatabaseTrustedPlayers().isTrustedToHorse(horse, offlineTarget)) {
            player.sendMessage(ColorUtils.color("<prefix><red><target> is already trusted!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", offlineTarget.getName())));
            return;
        }
        plugin.getDatabaseManager().getDatabaseTrustedPlayers().addTrustedPlayer(horse, offlineTarget);
        player.sendMessage(ColorUtils.color("<prefix><green><target> has been trusted!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("target", offlineTarget.getName())));

        if (offlineTarget.isOnline()) {
            Player onlineTarget = Bukkit.getPlayer(offlineTarget.getUniqueId());
            if (onlineTarget != null) {
                onlineTarget.sendMessage(ColorUtils.color("<prefix><green><player> has trusted you to <horse>!",
                        Placeholder.parsed("prefix", plugin.getPrefix()),
                        Placeholder.parsed("player", player.getName()),
                        Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))));
            }
        }
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

        assert iniTarget != null;
        Player target = Bukkit.getPlayer(iniTarget);
        OfflinePlayer offlineTarget = (target != null) ? target : Bukkit.getOfflinePlayer(iniTarget);

        if (!offlineTarget.hasPlayedBefore()) {
            player.sendMessage(ColorUtils.color("<prefix><red>Player named <target> is not found!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", iniTarget)));
            return;
        }

        if (Objects.equals(horse.getOwner(), offlineTarget)) {
            player.sendMessage(ColorUtils.color("<prefix><red>You can't untrust the owner of the horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            return;
        }

        if (!plugin.getDatabaseManager().getDatabaseTrustedPlayers().isTrustedToHorse(horse, offlineTarget)) {
            player.sendMessage(ColorUtils.color("<prefix><red><target> is not trusted!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", Objects.requireNonNull(offlineTarget.getName()))));
            return;
        }

        // Call event and untrust
        EquinePlayerUntrustEvent event = new EquinePlayerUntrustEvent(horse, player, target);
        Bukkit.getPluginManager().callEvent(event);
        plugin.getDatabaseManager().getDatabaseTrustedPlayers().removeTrustedPlayer(horse, offlineTarget);

        player.sendMessage(ColorUtils.color("<prefix><green><target> has been untrusted!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("target", Objects.requireNonNull(offlineTarget.getName()))));

        if (offlineTarget.isOnline()) {
            Player onlineTarget = Bukkit.getPlayer(offlineTarget.getUniqueId());
            if (onlineTarget != null) {
                onlineTarget.sendMessage(ColorUtils.color("<prefix><green><player> has untrusted you from <horse>!",
                        Placeholder.parsed("prefix", plugin.getPrefix()),
                        Placeholder.parsed("player", player.getName()),
                        Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))));
            }
        }
    }

    private void manureMarket(CommandSender commandSender, CommandArguments args) {
        Player target = (Player) args.get("target");

        if (target != null) {
            plugin.getMenuManager().getShopMenuManager().getShopManureMenu().open(target);
            commandSender.sendMessage(ColorUtils.color(
                    "<prefix><green>You have opened the Manure Market menu for <yellow><target><green>.",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("target", target.getName())
            ));
            return;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ColorUtils.color(
                    "<prefix><red>Only players can use this command without specifying a target.",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }

        plugin.getMenuManager().getShopMenuManager().getShopManureMenu().open((Player) commandSender);
    }

    public void leads(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;

        if(CommandCooldownUtils.isOnCooldown(player,  "horse leads")) {
            player.sendMessage(ColorUtils.color("<red>Please wait <time> before using this again!",
                    Placeholder.parsed("time", TimeUtils.formatDuration(CommandCooldownUtils.getRemaining(player, "horse leads")))));
            return;
        };

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ColorUtils.color("<red>Your inventory is full. Make some space to receive a stack of lead."));
            return;
        }

        CommandCooldownUtils.addCooldown(player, "horse leads", TimeUtils.minutesToMillis(5));
        ItemStack item = new ItemStack(Material.LEAD);
        item.setAmount(64);
        player.getInventory().addItem(item);
        player.sendMessage(ColorUtils.color("<green>You received a stack of lead."));
    }

    public void water(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;

        if (CommandCooldownUtils.isOnCooldown(player, "horse water")) {
            player.sendMessage(ColorUtils.color(
                    "<red>Please wait <time> before using this again!",
                    Placeholder.parsed("time", TimeUtils.formatDuration(CommandCooldownUtils.getRemaining(player, "horse water")))
            ));
            return;
        }

        int firstEmptySlot = player.getInventory().firstEmpty();
        if (firstEmptySlot == -1) {
            player.sendMessage(ColorUtils.color("<red>Your inventory is full. Make some space to receive a stack of water buckets."));
            return;
        }

        CommandCooldownUtils.addCooldown(player, "horse water", TimeUtils.minutesToMillis(5));

        ItemStack waterBucketItem = new ItemStack(Material.WATER_BUCKET, 64);
        player.getInventory().setItem(firstEmptySlot, waterBucketItem);

        player.sendMessage(ColorUtils.color("<green>You received a stack of water buckets."));
    }
}
