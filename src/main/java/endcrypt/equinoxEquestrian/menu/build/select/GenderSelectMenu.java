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

public class GenderSelectMenu {

    private final EquinoxEquestrian plugin;
    public GenderSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Coat Color Menu
    public Inventory genderMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Gender", 3, "Select Gender");


        int slot = 0;
        for(Gender gender : Gender.values()) {
            SGButton genderButton = genderButton(player, gender, equineHorse);
            gui.setButton(slot, genderButton);
            slot++;
        }

        return gui.getInventory();

    }

    private SGButton genderButton(Player player, Gender gender, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(gender.getGenderName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    equineHorse.setGender(gender);
                    plugin.getBuildAHorseMenu().openWithParameters(player, equineHorse);
                });
    }
}
