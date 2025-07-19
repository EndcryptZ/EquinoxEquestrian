package endcrypt.equinox.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.HeadUtils;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EquineDebugCommand {

    private final EquinoxEquestrian plugin;
    public EquineDebugCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.registerCommands();
    }

    private void registerCommands() {
        new CommandAPICommand("equinedebug")
                .withSubcommand(new CommandAPICommand("debugplayerhead"))
                .executesPlayer(this::debugPlayerHead)

                .register();
    }

    private void debugPlayerHead(CommandSender commandSender, CommandArguments args) {
        Player player = (Player) commandSender;

        Block playerBlock = player.getTargetBlock(null, 50);
        ItemStack pooHead = HeadUtils.getItemHead("1682");

        HeadUtils.copyHeadTextureToBlock(playerBlock, pooHead);
    }
}
