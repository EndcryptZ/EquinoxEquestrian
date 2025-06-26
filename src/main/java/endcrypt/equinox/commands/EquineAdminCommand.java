package endcrypt.equinox.commands;

import de.tr7zw.changeme.nbtapi.NBT;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.attributes.*;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class EquineAdminCommand {

    private final EquinoxEquestrian plugin;
    public EquineAdminCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    String[] equineItems = Arrays.stream(Item.values()).map(Enum::name).filter(name -> !name.equals("NONE")).toArray(String[]::new);
    String[] equineCoatColors = Arrays.stream(CoatColor.values()).map(Enum::name).filter(name -> !name.equals("NONE")).toArray(String[]::new);
    String[] equineCoatModifier = Arrays.stream(CoatModifier.values()).map(Enum::name).toArray(String[]::new);
    String[] equineHeight = Arrays.stream(Height.values()).map(height -> String.valueOf(height.getHands())).toArray(String[]::new);


    private void registerCommands() {
        new CommandAPICommand("equineadmin")
                .withAliases("eqadmin")
                .withPermission("equinox.cmd.equineadmin")
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

                .withSubcommand(new CommandAPICommand("bypass")
                        .withPermission("equinox.cmd.equineadmin.bypass")
                        .executesPlayer(this::bypass))

                .withSubcommand(new CommandAPICommand("modify")
                        .withPermission("equinox.cmd.equineadmin.edit")
                        .withSubcommand(new CommandAPICommand("colour")
                                .withArguments(new MultiLiteralArgument("colour", equineCoatColors))
                                .executesPlayer(this::modifyColour))
                        .withSubcommand(new CommandAPICommand("modifier")
                                .withArguments(new MultiLiteralArgument("modifier", equineCoatModifier))
                                .executesPlayer(this::modifyModifier))
                        .withSubcommand(new CommandAPICommand("height")
                                .withArguments(new DoubleArgument("height").replaceSuggestions(ArgumentSuggestions.strings(equineHeight)))
                                .executesPlayer(this::modifyHeight)))

                .withSubcommand(new CommandAPICommand("set")
                        .withPermission("equinox.cmd.equineadmin.edit")
                        .withSubcommand(new CommandAPICommand("jump")
                                .withArguments(new DoubleArgument("jump"))
                                .executesPlayer(this::setJump))
                        .withSubcommand(new CommandAPICommand("speed")
                                .withArguments(new DoubleArgument("speed"))
                                .executesPlayer(this::setSpeed))
                        .withSubcommand(new CommandAPICommand("traits")
                                .withArguments(new ListArgumentBuilder<Trait>("traits")
                                        .allowDuplicates(false)
                                        .withList(Stream.of(Trait.values()).filter(trait -> trait != Trait.NONE).toList())
                                        .withMapper(Trait::getTraitName)
                                        .buildGreedy())
                                .executesPlayer(this::setTraits))

                        .withSubcommand(new CommandAPICommand("age")
                                .withArguments(new IntegerArgument("age"))
                                .executesPlayer(this::setAge))

                        .withSubcommand(new CommandAPICommand("inheat")
                            .withArguments(new BooleanArgument("inheat"))
                            .executesPlayer(this::setInHeat))

                        .withSubcommand(new CommandAPICommand("instafoal")
                                .executesPlayer(this::setInstaFoal))

                        .withSubcommand(new CommandAPICommand("instabreed")
                                .executesPlayer(this::setInstaBreed)))

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
        equineHorseBuilder.spawnHorse(player.getUniqueId().toString(), player.getLocation(), equineHorseBuilder.randomHorse(name), false);
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

    private void modifyColour(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse abstractHorse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if (abstractHorse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the colour!"));
            return;
        }

        CoatColor coatColor = CoatColor.getByName(args.getUnchecked("colour"));

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(abstractHorse);
        equineLiveHorse.setCoatColor(coatColor);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<prefix><green>You set the color of <horse> to <color>",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", abstractHorse.getName()),
                Placeholder.parsed("color", coatColor.getCoatColorName())));

    }

    private void modifyModifier(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse abstractHorse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if (abstractHorse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the modifier!"));
            return;
        }

        CoatModifier coatModifier = CoatModifier.getByName(args.getUnchecked("modifier"));

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(abstractHorse);
        equineLiveHorse.setCoatModifier(coatModifier);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<prefix><green>You set the color of <horse> to <color>",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", abstractHorse.getName()),
                Placeholder.parsed("color", coatModifier.getCoatModifierName())));
    }

    private void modifyHeight(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse abstractHorse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if (abstractHorse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the height!"));
            return;
        }

        Height height = Height.getByHands(args.getUnchecked("height"));
        if(height == null) {
            commandSender.sendMessage(ColorUtils.color("<red>Invalid height!"));
            return;
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(abstractHorse);
        equineLiveHorse.setHeight(height);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<prefix><green>You set the height of <horse> to <height> hands",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", abstractHorse.getName()),
                Placeholder.parsed("height", String.valueOf(height.getHands()))));
    }

    private void setJump(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the jump strength!"));
            return;
        }

        double jumpStrength = (double) args.get("jump");
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

    private void setSpeed(CommandSender commandSender, CommandArguments args) {
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

    private void setTraits(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the traits!"));
            return;
        }

        List<Trait> traitList = (List<Trait>) args.get("traits");
        if(traitList.size() > 3) {
            commandSender.sendMessage(ColorUtils.color("<red>You can only select 3 traits!"));
            return;
        }
        
        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setTraits(traitList);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You set the traits of <horse> to <traits>",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("traits", String.join(", ", traitList.stream().map(Trait::getTraitName).toList()))));

    }

    private void setAge(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the age!"));
            return;
        }

        int ageInput = (int) args.get("age");
        if(ageInput < 0 || ageInput > 45) {
            commandSender.sendMessage(ColorUtils.color("<red>Age must be between 0 and 45!"));
            return;
        }
        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setAge(ageInput);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You set the age of <horse> to <age>",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("age", String.valueOf(equineLiveHorse.getAge()))));
    }

    private void setInHeat(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        boolean inHeat = (boolean) args.get("inheat");
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the in heat state!"));
            return;
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);

        if(equineLiveHorse.getGender() != Gender.MARE) {
            commandSender.sendMessage(ColorUtils.color("<red>You can only change the in heat state of mares!"));
            return;
        }

        if(inHeat && equineLiveHorse.isInHeat()) {
            commandSender.sendMessage(ColorUtils.color("<red>This horse is already in heat!"));
            return;
        }

        if(!inHeat && !equineLiveHorse.isInHeat()) {
            commandSender.sendMessage(ColorUtils.color("<red>This horse is already not in heat!"));
            return;
        }

        if(equineLiveHorse.isPregnant()) {
            commandSender.sendMessage(ColorUtils.color("<red>You can't change the in heat state of pregnant horses!"));
            return;
        }

        equineLiveHorse.setInHeat(inHeat);
        if(inHeat) {
            equineLiveHorse.setLastInHeat(System.currentTimeMillis());
        }
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You set the in heat state of <horse> to <inheat>",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("inheat", String.valueOf(equineLiveHorse.isInHeat()))));
    }

    private void setInstaFoal(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the instant foal state!"));
            return;
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        if(equineLiveHorse.getGender() != Gender.MARE) {
            commandSender.sendMessage(ColorUtils.color("<red>You can only change the instant foal state of mares!"));
            return;
        }

        if(!equineLiveHorse.isPregnant()) {
            commandSender.sendMessage(ColorUtils.color("<red>You can't change the instant foal state of non-pregnant horses!"));
            return;
        }

        equineLiveHorse.setInstantFoal(true);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You set the instant foal state of <horse> to <instafoal>",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("instafoal", String.valueOf(equineLiveHorse.isInstantFoal()))));


    }

    private void setInstaBreed(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            commandSender.sendMessage(ColorUtils.color("<red>You must select a horse to change the instant breed state!"));
            return;
        }


        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        if(equineLiveHorse.getGender() != Gender.MARE) {
            commandSender.sendMessage(ColorUtils.color("<red>You can only change the instant breed state of mares!"));
            return;
        }

        if(!equineLiveHorse.isBreeding()) {
            commandSender.sendMessage(ColorUtils.color("<red>You can't change the instant breed state of non-breeding horses!"));
            return;
        }

        equineLiveHorse.setInstantBreed(true);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You set the instant breed state of <horse> to <instabreed>",
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("instabreed", String.valueOf(equineLiveHorse.isInstantBreed()))));

    }
}
