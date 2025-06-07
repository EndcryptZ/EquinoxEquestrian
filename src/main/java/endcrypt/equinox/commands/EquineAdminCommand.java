package endcrypt.equinox.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.bypass.EquineBypass;
import endcrypt.equinox.equine.items.Item;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
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

                .withSubcommand(new CommandAPICommand("speed")
                        .withPermission("equinox.cmd.equineadmin.speed")
                        .withArguments(new DoubleArgument("speed"))
                        .executes(this::speed))

                .withSubcommand(new CommandAPICommand("jumpstrength")
                        .withPermission("equinox.cmd.equineadmin.jumpstrength")
                        .withArguments(new DoubleArgument("jumpstrength"))
                        .executes(this::jumpStrength))

                .withSubcommand(new CommandAPICommand("bypass")
                        .withPermission("equinox.cmd.equineadmin.bypass")
                        .executesPlayer(this::bypass))

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

    private void speed(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the speed!"));
            return;
        }

        double speed = (double) args.get("speed");
        if(speed < 0.1) {
            commandSender.sendMessage(ColorUtils.color("<red>Speed must be greater than 0.1!"));
            return;
        }

        NBT.modifyPersistentData(horse, nbt -> {
            horse.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(EquineUtils.blocksToMnecraftSpeed(speed));
            nbt.setDouble(Keys.BASE_SPEED.getKey(), speed);
        });

        commandSender.sendMessage(ColorUtils.color("<green>You set the base speed of <horse>'s to <speed> blocks per second!",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("speed", String.valueOf(speed))
        ));
    }

    private void jumpStrength(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the jump strength!"));
            return;
        }

        double jumpStrength = (double) args.get("jumpstrength");
        if(jumpStrength < 0.1) {
            commandSender.sendMessage(ColorUtils.color("<red>Jump Power must be greater than 0.1!"));
            return;
        }

        NBT.modifyPersistentData(horse, nbt -> {
            horse.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(EquineUtils.blocksToMinecraftJumpStrength(jumpStrength));
            nbt.setDouble(Keys.BASE_JUMP.getKey(), EquineUtils.blocksToMinecraftJumpStrength(jumpStrength));
        });

        commandSender.sendMessage(ColorUtils.color("<green>You set the base jump strength of <horse>'s to <jumpstrength> blocks!",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("jumpstrength", String.valueOf(jumpStrength))
        ));
    }

    private void bypass(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if(!EquineBypass.hasBypass(player)) {
            EquineBypass.add(player);
            player.sendMessage(ColorUtils.color("<prefix><green>You have bypass enabled. You can now interact with any horses!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
        } else {
            EquineBypass.remove(player);
            player.sendMessage(ColorUtils.color("<prefix><green>You have bypass disabled. You can no longer interact with any horses!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
        }
    }


}
