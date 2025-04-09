package endcrypt.equinoxEquestrian.commands.subcommands;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.EquineUtils;
import endcrypt.equinoxEquestrian.horse.enums.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GiveCommand {

    private final EquinoxEquestrian plugin;

    public GiveCommand(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /eq give <player> <item> <amount>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return;
        }

        Item item;
        try {
            item = Item.valueOf(args[2].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid item name.");
            return;
        }

        int amount = 1;
        if (args.length >= 4) {
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Amount must be a number.");
                return;
            }
        }

        ItemStack itemStack = item.getItem();

        if(EquineUtils.isGroomItem(itemStack)) {
            for(int i = 0; i < amount; i++) {
                NBT.modify(itemStack, NBT -> {
                    NBT.setString("UNSTACKABLE", UUID.randomUUID().toString());
                });
                target.getInventory().addItem(itemStack);
            }
            sender.sendMessage(ChatColor.GREEN + "Gave " + amount + "x " + item.name() + " to " + target.getName() + ".");
            return;
        }

        itemStack.setAmount(amount);
        target.getInventory().addItem(itemStack);
        sender.sendMessage(ChatColor.GREEN + "Gave " + amount + "x " + item.name() + " to " + target.getName() + ".");
    }

}
