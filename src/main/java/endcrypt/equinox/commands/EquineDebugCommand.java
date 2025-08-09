package endcrypt.equinox.commands;

import de.oliver.fancyholograms.api.FancyHologramsPlugin;
import de.oliver.fancyholograms.api.HologramManager;
import de.oliver.fancyholograms.api.data.TextHologramData;
import de.oliver.fancyholograms.api.hologram.Hologram;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.HeadUtils;
import endcrypt.equinox.utils.TimeUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EquineDebugCommand {

    private final EquinoxEquestrian plugin;
    public EquineDebugCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands() {
        new CommandAPICommand("equinedebug")
                .withSubcommand(new CommandAPICommand("debugplayerhead")
                        .executesPlayer(this::debugPlayerHead))

                .withSubcommand(new CommandAPICommand("debugholo")
                        .executesPlayer(this::debugHolo))

                .withSubcommand(new CommandAPICommand("stresstesthoresesspawn")
                        .withArguments(new IntegerArgument("count"))
                        .executesPlayer(this::stressTestHorsesSpawn))

                .withSubcommand(new CommandAPICommand("debugbirthdate")
                        .withArguments(new LongArgument("epoch"))
                        .executesPlayer(this::debugBirthTime))

                .withSubcommand(new CommandAPICommand("debughorseeatanimation")
                        .executesPlayer(this::debugHorseEatAnimation))

                .withSubcommand(new CommandAPICommand("checkfoodhorse")
                        .executesPlayer(this::checkFoodHorse))

                .register();
    }

    private void debugPlayerHead(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if(!isExecutorDeveloper(player)) {
            return;
        }

        HeadUtils.placeHeadFromHDB(player.getLocation(), "1682");
    }

    private void debugHolo(CommandSender commandSender, CommandArguments args) {
        HologramManager hologramManager = FancyHologramsPlugin.get().getHologramManager();
        Player player = (Player) commandSender;
        if(!isExecutorDeveloper(player)) {
            return;
        }

        String holoId = UUID.randomUUID().toString();
        TextHologramData textHologramData = new TextHologramData(holoId, player.getLocation().add(0, 1, 0));
        textHologramData.removeLine(0);
        textHologramData.addLine("This is a test hologram.");
        textHologramData.setPersistent(false);
        textHologramData.setTextUpdateInterval(10);

        Hologram hologram = hologramManager.create(textHologramData);
        hologramManager.addHologram(hologram);

        hologram.queueUpdate();
        hologram.forceUpdate();


        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            hologramManager.removeHologram(hologram);
        }, 60L);
    }

    private void stressTestHorsesSpawn(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        if (!isExecutorDeveloper(player)) {
            return;
        }

        EquineHorseBuilder equineHorseBuilder = new EquineHorseBuilder(plugin);
        int count = args.getUnchecked("count");

        Bukkit.broadcastMessage("§c[Warning] §eStress test initiated by " + player.getName() + " - spawning " + count + " horses. Server performance may be affected.");

        for (int i = 0; i < count; i++) {
            equineHorseBuilder.spawnHorse(player.getUniqueId().toString(), player.getLocation(), equineHorseBuilder.randomHorse("Stress Test Horse"), false);
        }
    }

    private void debugBirthTime(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        long newBirthTime = args.getUnchecked("epoch");
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if(!isExecutorDeveloper(player)) {
            return;
        }

        if(horse == null) {
            player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You have not selected a horse!"));
            return;
        }

        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        equineLiveHorse.setBirthTime(newBirthTime);
        equineLiveHorse.update();
        player.sendMessage(ColorUtils.color("<green>You adjusted the birth time of <horse> to <date>",
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name())),
                Placeholder.parsed("date", TimeUtils.formatEpochToDate(newBirthTime))));
    }

    private void debugHorseEatAnimation(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if(!isExecutorDeveloper(player)) {
            return;
        }

        if(horse == null) {
            player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You have not selected a horse!"));
            return;
        }

        CraftHorse nmsHorse = (CraftHorse) horse;
        nmsHorse.getHandle().setEating(true);
    }

    private void checkFoodHorse(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if(!isExecutorDeveloper(player)) {
            return;
        }

        if(horse == null) {
            player.sendMessage(ColorUtils.color(plugin.getPrefix() + "<red>You have not selected a horse!"));
            return;
        }

        plugin.getEquineManager().getEquineHunger().checkFood(horse);
    }


    private boolean isExecutorDeveloper(Player player) {
        if (!player.getName().equalsIgnoreCase("Endcrypt")) {
            player.sendMessage(ColorUtils.color("<red>This command is reserved for the plugin developer (Endcrypt)."));
            player.sendMessage(ColorUtils.color("<red>Warning: Using debug commands incorrectly can severely break the plugin or corrupt server data. Only use on a development server."));
            return false;
        }

        return true;
    }
}
