package endcrypt.equinox.commands.arg;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.transfer.EquineTransfer;
import endcrypt.equinox.equine.transfer.EquineTransferManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TransferOwnershipRequestsArgument extends CustomArgument<OfflinePlayer, String> {
    private static final EquinoxEquestrian plugin = EquinoxEquestrian.instance;

    public TransferOwnershipRequestsArgument() {
        super(new StringArgument("target"), info -> {
            String targetName = info.currentInput();
            Player sender = (Player) info.sender();
            Player target = Bukkit.getPlayerExact(targetName);

            if (target == null) {
                throw CustomArgumentException.fromString("Player '" + targetName + "' is not online.");
            }

            EquineTransferManager transferManager = plugin.getEquineManager().getEquineTransferManager();
            Map<Player, EquineTransfer> requestMap = transferManager.getRequestMap();

            // Check if this sender has a pending request *from* the target
            boolean match = requestMap.values().stream()
                    .anyMatch(transfer -> transfer.getReceiver().equals(sender) && transfer.getSender().equals(target));

            if (!match) {
                throw CustomArgumentException.fromString("No pending request from: " + targetName);
            }

            return target;
        });

        this.replaceSuggestions(ArgumentSuggestions.stringCollectionAsync(info ->
                CompletableFuture.supplyAsync(() -> {
                    Player player = (Player) info.sender();
                    EquineTransferManager transferManager = plugin.getEquineManager().getEquineTransferManager();

                    return transferManager.getRequestMap().values().stream()
                            .filter(transfer -> transfer.getReceiver().equals(player))
                            .map(transfer -> transfer.getSender().getName())
                            .distinct()
                            .toList();
                })
        ));
    }
}
