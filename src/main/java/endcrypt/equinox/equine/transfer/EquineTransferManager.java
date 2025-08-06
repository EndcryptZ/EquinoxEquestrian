package endcrypt.equinox.equine.transfer;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquineTransferManager {


    @Getter
    private final Map<Player, EquineTransfer> requestMap = new HashMap<>();
    private final Map<Player, EquineTransfer> confirmationCache = new HashMap<>();
    private final EquinoxEquestrian plugin;
    public EquineTransferManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void requestTransferOwnership(Player receiver, Player sender, AbstractHorse abstractHorse) {
        if (EquineUtils.isPlayerHorseSlotsMax(receiver)) {
            sender.sendMessage(ColorUtils.color("<red>Transfer request failed: This player already has the maximum number of horse slots!"));
            return;
        }

        EquineLiveHorse liveHorse = new EquineLiveHorse(abstractHorse);

        if (receiver.getUniqueId().toString().equalsIgnoreCase(liveHorse.getOwnerUUID())) {
            sender.sendMessage(ColorUtils.color("<red>Transfer request failed: This player is already the owner of this horse!"));
            return;
        }

        // Check for existing pending request
        EquineTransfer existingTransfer = requestMap.get(sender);
        EquineTransfer newTransfer = new EquineTransfer(sender, receiver, abstractHorse);

        if (existingTransfer != null && existingTransfer.equals(newTransfer)) {
            sender.sendMessage(ColorUtils.color("<red>You have already sent a transfer request to this player for this horse."));
            return;
        }

        // Check if confirmation is required
        if (!confirmationCache.containsKey(sender) || !confirmationCache.get(sender).equals(newTransfer)) {
            confirmationCache.put(sender, newTransfer);
            sender.sendMessage(ColorUtils.color(
                    "\n\n" +
                            "<prefix><yellow>Are you sure you want to send a horse ownership transfer request?\n" +
                            "<prefix><green>Receiver: <yellow><receiver>\n" +
                            "<prefix><green>Horse: <reset><horse>\n" +
                            "<prefix><gray>Type the same command again to confirm." +
                            "\n\n",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("receiver", receiver.getName()),
                    Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(abstractHorse.name()))
            ));
            return;
        }

        // Confirmed, proceed with request
        requestMap.put(sender, newTransfer);
        confirmationCache.remove(sender);

        sender.sendMessage(ColorUtils.color(
                "\n\n" +
                        "<prefix><green>You have sent a horse ownership transfer request!\n" +
                        "<prefix><green>Receiver: <yellow><receiver>\n" +
                        "<prefix><green>Horse: <reset><horse>" +
                        "\n\n",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("receiver", receiver.getName()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(abstractHorse.name()))
        ));

        receiver.sendMessage(ColorUtils.color(
                "\n\n" +
                        "<prefix><green>You have received a horse ownership transfer request!\n" +
                        "<prefix><green>From: <yellow><sender>\n" +
                        "<prefix><green>Horse: <reset><horse>\n" +
                        "<prefix><green>To accept, use <yellow>/h toaccept <sender>" +
                        "\n\n",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("sender", sender.getName()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(abstractHorse.name()))
        ));
    }


    public void acceptTransferOwnership(Player receiver, Player sender) {
        if (EquineUtils.isPlayerHorseSlotsMax(receiver)) {
            receiver.sendMessage(ColorUtils.color("<red>You are unable to accept this transfer request as your horse slots are already full."));
            return;
        }

        EquineTransfer request = requestMap.get(sender);
        EquineTransfer matchedTransfer = null;

        if (request.getSender().equals(sender)) {
            matchedTransfer = request;
        }

        if (matchedTransfer == null) {
            sender.sendMessage(ColorUtils.color("<red>You have no pending request from this player."));
            return;
        }

        AbstractHorse horse = matchedTransfer.getHorse();
        if (horse == null) {
            sender.sendMessage(ColorUtils.color("<red>The sender's horse doesn't exist!"));
            return;
        }

        // Transfer ownership
        horse.setOwner(receiver);
        EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
        liveHorse.setOwnerName(receiver.getName());
        liveHorse.setOwnerUUID(receiver.getUniqueId().toString());
        liveHorse.update();
        plugin.getDatabaseManager().getDatabaseHorses().updateHorse(horse);

        // Remove the request
        requestMap.remove(sender);

        // Unset sender's selected horse if it's the one transferred
        AbstractHorse selected = plugin.getPlayerDataManager().getPlayerData(sender).getSelectedHorse();
        if (selected != null && selected.equals(horse)) {
            plugin.getPlayerDataManager().getPlayerData(sender).setSelectedHorse(null);
        }

// Receiver success message
        receiver.sendMessage(ColorUtils.color(
                "\n\n" + // extra spacing
                        plugin.getPrefix() + "<green>You have successfully accepted the horse\n" +
                        plugin.getPrefix() + "<green>ownership transfer request!\n" +
                        plugin.getPrefix() + "<green>Sender: <yellow><sender>\n" +
                        plugin.getPrefix() + "<green>Horse: <reset><horse>" +
                        "\n\n" ,
                Placeholder.parsed("sender", sender.getName()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))
        ));

// Sender notification message
        sender.sendMessage(ColorUtils.color(
                "\n\n" + // extra spacing
                        plugin.getPrefix() + "<yellow><receiver>\n" +
                        plugin.getPrefix() + "<green>has accepted your horse ownership\n" +
                        plugin.getPrefix() + "<green>transfer request!\n" +
                        plugin.getPrefix() + "<green>Horse: <reset><horse>" +
                        "\n\n" ,
                Placeholder.parsed("receiver", receiver.getName()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))
        ));
    }
}
