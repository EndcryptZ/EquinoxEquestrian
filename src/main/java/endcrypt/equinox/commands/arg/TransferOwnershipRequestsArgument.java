package endcrypt.equinox.commands.arg;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class TransferOwnershipRequestsArgument extends CustomArgument<OfflinePlayer, String> {
    private static final EquinoxEquestrian plugin = EquinoxEquestrian.instance;

    public TransferOwnershipRequestsArgument() {
        super(new StringArgument("target"), info -> {
            String targetName = info.currentInput();
            Player sender = (Player) info.sender();
            Player target = Bukkit.getPlayer(targetName);
            if(!plugin.getEquineManager().getEquineTransferManager().getRequestMap().containsKey(sender)) {
                throw CustomArgumentException.fromString("No pending request from: " + targetName);
            }
            if(plugin.getEquineManager().getEquineTransferManager().getRequestMap().get(sender).stream().anyMatch(transfer -> transfer.getReceiver().equals(target))) {
                throw CustomArgumentException.fromString("No pending request from: " + targetName);
            }
            return target;
        });

        this.replaceSuggestions(ArgumentSuggestions.stringCollectionAsync(info ->
            CompletableFuture.supplyAsync(() -> {
                Player player = (Player) info.sender();
                if(!plugin.getEquineManager().getEquineTransferManager().getRequestMap().containsKey(player)) {
                    return null;
                }
                return plugin.getEquineManager().getEquineTransferManager().getRequestMap().get(player).stream().map(equineTransfer -> equineTransfer.getSender().getName()).toList();
                    }
            )
        ));
    }
}
