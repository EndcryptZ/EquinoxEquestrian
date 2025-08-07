package endcrypt.equinox.equine.transfer;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class EquineTransferListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineTransferListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quittingPlayer = event.getPlayer();
        Map<Player, EquineTransfer> requestMap = plugin.getEquineManager().getEquineTransferManager().getRequestMap();

        // Check if the quitting player is a sender of a pending request
        EquineTransfer pendingTransfer = requestMap.remove(quittingPlayer);
        if (pendingTransfer != null) {
            Player receiver = pendingTransfer.getReceiver();

            // Notify receiver if online
            if (receiver.isOnline()) {
                receiver.sendMessage(ColorUtils.color(
                        "\n\n" +
                                "<prefix><red>The horse transfer request from <yellow><sender> <red>has been cancelled.\n" +
                                "<prefix><gray>They left the server before it was accepted.\n\n",
                        Placeholder.parsed("prefix", plugin.getPrefix()),
                        Placeholder.parsed("sender", quittingPlayer.getName())
                ));
            }
        }
    }
}
