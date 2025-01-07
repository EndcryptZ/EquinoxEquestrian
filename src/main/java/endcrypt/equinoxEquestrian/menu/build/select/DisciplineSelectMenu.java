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

public class DisciplineSelectMenu {


    private final EquinoxEquestrian plugin;
    public DisciplineSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Discipline Menu
    public Inventory disciplineMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Discipline", 3, "Select Discipline");


        int slot = 0;
        for(Discipline discipline : Discipline.values()) {
            SGButton disciplineButton = disciplineButton(player, discipline, equineHorse);
            gui.setButton(slot, disciplineButton);
            slot++;
        }

        return gui.getInventory();

    }

    private SGButton disciplineButton(Player player, Discipline discipline, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(discipline.getDisciplineName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    equineHorse.setDiscipline(discipline);
                    plugin.getBuildAHorseMenu().openWithParameters(player, equineHorse);
                });
    }
}
