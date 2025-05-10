package endcrypt.equinox.commands.equineadmin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EquineAdminTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("tokens");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("tokens")) {
            return Arrays.asList("give", "take", "set");
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("tokens")) {
            String subCommand = args[1].toLowerCase();
            if (subCommand.equals("give") || subCommand.equals("take") || subCommand.equals("set")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
            }
        }

        return null;
    }

}
