package endcrypt.equinox.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.items.Item;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class EquineAdminCommand {

    private final EquinoxEquestrian plugin;
    public EquineAdminCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    String[] equineItems = Arrays.stream(Item.values()).map(Enum::name).toArray(String[]::new);

    private void registerCommands() {
        new CommandAPICommand("equineadmin")
                .withAliases("eqadmin")
                .withSubcommand(new CommandAPICommand("token")
                        .withPermission("equinox.cmd.equineadmin.token")
                        .withArguments(new MultiLiteralArgument("action", "set", "give", "take"))
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new IntegerArgument("amount"))
                        .executes(this::token))

                .withSubcommand(new CommandAPICommand("autospawn")
                        .withPermission("equinox.cmd.equineadmin.autospawn")
                        .withArguments(new StringArgument("name"))
                        .executes(this::autoSpawn))

                .withSubcommand(new CommandAPICommand("give")
                        .withPermission("equinox.cmd.equineadmin.give")
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new MultiLiteralArgument("item", equineItems))
                        .withArguments(new IntegerArgument("amount", 1).setOptional(true))
                        .executes(this::giveEquineItem))

                .register();
    }

    private void token(CommandSender commandSender, CommandArguments args) {
        String action = (String) args.get("action");
        Player target = (Player) args.get("player");
        int amount = (int) args.get("amount");
        assert target != null;
        String targetName = target.getName();

        switch (Objects.requireNonNull(action).toLowerCase()) {
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

    private void autoSpawn(CommandSender sender, CommandArguments args) {
        EquineHorseBuilder equineHorseBuilder = new EquineHorseBuilder(plugin);
        Player player = (Player) sender;
        String name = (String) args.get("name");
        equineHorseBuilder.spawnHorse(player, equineHorseBuilder.randomHorse(name));
        player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<green>You have spawned a randomized horse!"));

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
                assert target != null;
                target.getInventory().addItem(itemStack);
            }
            assert target != null;
            commandSender.sendMessage(ColorUtils.color("<green>Gave <amount>x <item> to <player>.",
                    Placeholder.parsed("amount", String.valueOf(amount)),
                    Placeholder.parsed("item", item.name()),
                    Placeholder.parsed("player", target.getName())));
            return;
        }

        itemStack.setAmount(amount);
        assert target != null;
        target.getInventory().addItem(itemStack);
        commandSender.sendMessage(ColorUtils.color("<green>Gave <amount>x <item> to <player>.",
                Placeholder.parsed("amount", String.valueOf(amount)),
                Placeholder.parsed("item", item.name()),
                Placeholder.parsed("player", target.getName())));
    }



}
