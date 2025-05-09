package endcrypt.equinoxEquestrian.commands.equine.subcommands;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class TpHereCommand {

    private final EquinoxEquestrian plugin;

    public TpHereCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ColorUtils.color("<red>This command is only available for players!"));
            return;
        }


        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();

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
