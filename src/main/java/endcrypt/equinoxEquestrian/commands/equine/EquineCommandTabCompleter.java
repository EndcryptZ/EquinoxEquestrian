package endcrypt.equinoxEquestrian.commands.equine;

import endcrypt.equinoxEquestrian.equine.items.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EquineCommandTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("menu", "lunge", "sethome", "give", "tp", "teleport");
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "sethome":
                case "tp":
                case "teleport":
                    return Arrays.asList("pasture", "stall");
                case "give":
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .collect(Collectors.toList());
            }
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return Arrays.stream(Item.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }

        return null;
    }
}
