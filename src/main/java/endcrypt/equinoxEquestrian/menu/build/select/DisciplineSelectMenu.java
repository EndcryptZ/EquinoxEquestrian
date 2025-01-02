package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.enums.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class DisciplineSelectMenu {


    private final EquinoxEquestrian plugin;
    public DisciplineSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Discipline Menu
    public Inventory disciplineMenu(Player player, String name, Breed breed, CoatColor coatColor, Gender gender, Trait[] traits) {

        SGMenu gui = plugin.getSpiGUI().create("Select Discipline", 3, "Select Discipline");


        int slot = 0;
        for(Discipline discipline : Discipline.values()) {
            SGButton disciplineButton = disciplineButton(player, name, discipline, breed, coatColor, gender, traits);
            gui.setButton(slot, disciplineButton);
            slot++;
        }

        return gui.getInventory();

    }

    private SGButton disciplineButton(Player player, String name, Discipline discipline, Breed breed, CoatColor coatColor, Gender gender, Trait[] traits) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(discipline.getDisciplineName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    plugin.getBuildAHorseMenu().openWithParameters(player, name, discipline, breed, coatColor, gender, traits);
                });
    }
}
