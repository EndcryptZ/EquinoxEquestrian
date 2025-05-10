package endcrypt.equinox.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.items.Item;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

public class EquineCommand {

    private final EquinoxEquestrian plugin;

    public EquineCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    String[] equineItems = Arrays.stream(Item.values()).map(Enum::name).toArray(String[]::new);

    private void registerCommands() {
        new CommandAPICommand("eq")
                .withSubcommand(new CommandAPICommand("give")
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new MultiLiteralArgument("item", equineItems))
                        .withArguments(new IntegerArgument("amount", 1).setOptional(true))
                        .executes(this::giveEquineItem))

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


    private void giveEquineItem(CommandSender commandSender, CommandArguments args) {
        Player target = (Player) args.get("player");
        Item item = Item.valueOf((String) args.get("item"));
        ItemStack itemStack = item.getItem();
        int amount;
        if(args.get("amount") != null) amount = (int) args.get("amount");
        else amount = 1;

        if(EquineUtils.isGroomItem(itemStack)) {
            for (int i = 0; i < amount; i++) {
                NBT.modify(itemStack, NBT -> {
                    NBT.setString("UNSTACKABLE", UUID.randomUUID().toString());
                });
                target.getInventory().addItem(itemStack);
            }
            commandSender.sendMessage(ColorUtils.color("<green>Gave <amount>x <item> to <player>.",
                    Placeholder.parsed("amount", String.valueOf(amount)),
                    Placeholder.parsed("item", item.name()),
                    Placeholder.parsed("player", target.getName())));
            return;
        }

        itemStack.setAmount(amount);
        target.getInventory().addItem(itemStack);
        commandSender.sendMessage(ColorUtils.color("<green>Gave <amount>x <item> to <player>.",
                Placeholder.parsed("amount", String.valueOf(amount)),
                Placeholder.parsed("item", item.name()),
                Placeholder.parsed("player", target.getName())));
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
