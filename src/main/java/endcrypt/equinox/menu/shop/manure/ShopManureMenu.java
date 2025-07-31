package endcrypt.equinox.menu.shop.manure;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ShopManureMenu {


    private final EquinoxEquestrian plugin;

    public ShopManureMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        player.openInventory(createMenu(player));

    }

    private Inventory createMenu(Player player) {
        int playerManureAmount = plugin.getPlayerDataManager().getPlayerData(player).getManure();
        SGMenu gui = plugin.getSpiGUI().create("Manure Market (" + playerManureAmount + " Manure)", 2, "Manure Market");

        gui.setButton(0, sellButton(player, 5, playerManureAmount));
        gui.setButton(1, sellButton(player, 10, playerManureAmount));
        gui.setButton(2, sellButton(player, 25, playerManureAmount));

        gui.setButton(8, sellAllButton(player, playerManureAmount));

        gui.setButton(9, backButton(player));



        return gui.getInventory();
    }

    private SGButton sellButton(Player player, int amount, int playerManureAmount) {

        boolean hasEnough = playerManureAmount >= amount;
        List<String> lore = new java.util.ArrayList<>(List.of());
        String name = "&fSell" + amount;
        if (hasEnough) {
            name = "&aSell " + amount;
            lore.add("&aClick to sell " + amount);
            lore.add("&amanure for $" + amount * 0.5);
        } else {
            lore.add("&7You don't have enough");
            lore.add("&7manure to sell.");
        }

        return new SGButton(
                new ItemBuilder(Material.RED_TERRACOTTA)
                        .name(name)
                        .lore(lore)
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    sellPlayerManure(player, amount);
                });
    }

    private SGButton sellAllButton(Player player, int playerManureAmount) {

        boolean hasEnough = playerManureAmount > 0;
        List<String> lore = new java.util.ArrayList<>(List.of());
        String name = "&fSell" + playerManureAmount;
        if (hasEnough) {
            name = "&aSell " + playerManureAmount;
            lore.add("&aClick to sell " + playerManureAmount);
            lore.add("&amanure for $" + playerManureAmount * 0.5);
        } else {
            lore.add("&7You don't have enough");
            lore.add("&7manure to sell.");
        }

        return new SGButton(
                new ItemBuilder(Material.RED_TERRACOTTA)
                        .name(name)
                        .lore(lore)
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    sellPlayerManure(player, playerManureAmount);
                });
    }

    private SGButton backButton(Player player) {
        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&7[<] Previous Page")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    player.performCommand("shop");
                });
    }

    private void sellPlayerManure(Player player, int amount) {
        int playerManureAmount = plugin.getPlayerDataManager().getPlayerData(player).getManure();
        boolean hasEnough = playerManureAmount >= amount;
        if(!hasEnough || playerManureAmount <= 0) {
            player.sendMessage(ColorUtils.color("<prefix><red>You don't have enough manure to sell!",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
            return;
        }

        int manureResult = playerManureAmount - amount;
        double gain = amount * 0.5;
        plugin.getPlayerDataManager().getPlayerData(player).setManure(manureResult);
        plugin.getEcon().depositPlayer(player, gain);
        player.sendMessage(ColorUtils.color(
                "<prefix><green>You've sold <yellow><amount> <green>manure for <gold>$<gain><green>!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("amount", String.valueOf(amount)),
                Placeholder.parsed("gain", String.valueOf(gain))
        ));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        open(player);
    }
}
