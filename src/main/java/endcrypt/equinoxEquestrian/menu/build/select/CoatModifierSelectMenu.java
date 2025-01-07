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

public class CoatModifierSelectMenu {

    private final EquinoxEquestrian plugin;
    public CoatModifierSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Coat Color Menu
    public Inventory coatModifierMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Coat Color", 3, "Select Coat Color");


        int slot = 0;
        for(CoatModifier coatModifier : CoatModifier.values()) {
            SGButton coatModifierButton = coatModifierButton(player, coatModifier, equineHorse);
            gui.setButton(slot, coatModifierButton);
            slot++;
        }

        return gui.getInventory();

    }

    private SGButton coatModifierButton(Player player, CoatModifier coatModifier, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(coatModifier.getCoatModifierName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    equineHorse.setCoatModifier(coatModifier);
                    plugin.getBuildAHorseMenu().openWithParameters(player, equineHorse);
                });
    }

}
