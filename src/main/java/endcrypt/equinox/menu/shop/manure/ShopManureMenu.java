package endcrypt.equinox.menu.shop.manure;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Material;
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

        return gui.getInventory();
    }

    private SGButton sellButton(Player player, int amount, int playerManureAmount) {

        boolean hasEnough = playerManureAmount >= amount;
        List<String> lore = new java.util.ArrayList<>(List.of());
        if (hasEnough) {
            lore.add("&aClick to sell " + amount);
            lore.add("manure for $" + amount * 0.5);
        } else {
            lore.add("&7You don't have enough");
            lore.add("&7manure to sell.");
        }

        return new SGButton(
                new ItemBuilder(Material.RED_TERRACOTTA )
                        .name("&fSell " + amount)
                        .lore(lore)
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {

                });
    }
}
