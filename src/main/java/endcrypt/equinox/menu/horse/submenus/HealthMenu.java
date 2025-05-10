package endcrypt.equinox.menu.horse.submenus;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HealthMenu {

    private final EquinoxEquestrian plugin;
    public HealthMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Health Menu
    public Inventory menu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create("Health Team", 3, "Health Team");


        gui.setButton(0, farrierButton());
        gui.setButton(1, vetButton());
        gui.setButton(2, chiropractorButton());
        gui.setButton(3, dentistButton());


        return gui.getInventory();

    }

    private SGButton farrierButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fFarrier")
                        .build()
        );
    }

    private SGButton vetButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fVet")
                        .build()
        );
    }

    private SGButton chiropractorButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fChiropractor")
                        .build()
        );
    }

    private SGButton dentistButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fDentist")
                        .build()
        );
    }

}
