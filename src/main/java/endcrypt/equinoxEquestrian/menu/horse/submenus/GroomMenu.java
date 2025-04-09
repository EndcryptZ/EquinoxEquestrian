package endcrypt.equinoxEquestrian.menu.horse.submenus;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GroomMenu {

    private final EquinoxEquestrian plugin;
    public GroomMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Home Menu
    public Inventory menu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create("Groom", 1, "Groom");


        gui.setButton(0, brushButton());
        gui.setButton(1, sweatScraperButton());
        gui.setButton(2, hoofPickButton());
        gui.setButton(3, flySprayButton());
        gui.setButton(4, washButton());
        gui.setButton(5, conditionerButton());
        gui.setButton(6, shampooButton());
        gui.setButton(7, hoofOilButton());
        gui.setButton(8, sunCreamButton());


        return gui.getInventory();

    }

    private SGButton brushButton() {

        return new SGButton(
                new ItemBuilder(Material.FLINT)
                        .name("&fGroom")
                        .build()
        );
    }

    private SGButton sweatScraperButton() {

        return new SGButton(
                new ItemBuilder(Material.STRING)
                        .name("&fSweat Scraper")
                        .build()
        );
    }

    private SGButton hoofPickButton() {

        return new SGButton(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("&fHoof Pick")
                        .build()
        );
    }

    private SGButton flySprayButton() {

        return new SGButton(
                new ItemBuilder(Material.POTION)
                        .name("&fFly Spray")
                        .build()
        );
    }

    private SGButton washButton() {

        return new SGButton(
                new ItemBuilder(Material.WATER_BUCKET)
                        .name("&fWash")
                        .build()
        );
    }

    private SGButton conditionerButton() {

        return new SGButton(
                new ItemBuilder(Material.DRAGON_BREATH)
                        .name("&eConditioner")
                        .build()
        );
    }

    private SGButton shampooButton() {

        return new SGButton(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("&fShampoo")
                        .build()
        );
    }

    private SGButton hoofOilButton() {

        return new SGButton(
                new ItemBuilder(Material.GLASS_BOTTLE)
                        .name("&fHoof Oil")
                        .build()
        );
    }

    private SGButton sunCreamButton() {

        return new SGButton(
                new ItemBuilder(Material.AMETHYST_SHARD)
                        .name("&fSun Cream")
                        .build()
        );
    }
}
