package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.EquineHorse;
import endcrypt.equinoxEquestrian.horse.enums.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class CoatColorSelectMenu {

    private final EquinoxEquestrian plugin;
    public CoatColorSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Coat Color Menu
    public Inventory coatColorMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Coat Color", 3, "Coat Color Menu");


        int slot = 0;
        for(CoatColor coatColor : CoatColor.values()) {
            if (coatColor == CoatColor.NONE) continue;
            SGButton coatColorButton = coatColorButton(player, coatColor, equineHorse);
            gui.setButton(slot, coatColorButton);
            slot++;
        }

        return gui.getInventory();

    }

    private SGButton coatColorButton(Player player, CoatColor coatColor, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(coatColor.getCoatColorName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    equineHorse.setCoatColor(coatColor);
                    plugin.getBuildMenu().openWithParameters(player, equineHorse);
                });
    }
}
