package endcrypt.equinoxEquestrian.menu.horse;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class    HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player player) {
        player.openInventory(createMenu(player));

    }

    private Inventory createMenu(Player player) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List", 4, "Horse List");


        gui.setButton(31, menuOrganiserButton());
        gui.setButton(35, trustedHorsesButton());


        return gui.getInventory();
    }

    private SGButton menuOrganiserButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oMenu Organiser")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();


                });
    }

    private SGButton trustedHorsesButton() {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fTrusted Horses")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();


                });
    }
}
