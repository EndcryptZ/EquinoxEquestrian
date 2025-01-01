package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.enums.Breed;
import endcrypt.equinoxEquestrian.equine.enums.Discipline;
import endcrypt.equinoxEquestrian.equine.enums.Gender;
import endcrypt.equinoxEquestrian.equine.enums.Trait;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class BreedSelectMenu {


    private final EquinoxEquestrian plugin;
    public BreedSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Discipline Menu
    public Inventory breedMenu(Player player, String name, Discipline discipline, Gender gender, Trait[] traits) {

        SGMenu gui = plugin.getSpiGUI().create("Select Breed", 5, "Breed");


        int slot = 0;
        for(Breed breed : Breed.values()) {
            SGButton breedButton = breedButton(player, name, discipline, breed, gender, traits);
            gui.setButton(slot, breedButton);
            slot++;
        }

        gui.setAutomaticPaginationEnabled(true);
        return gui.getInventory();

    }

    private SGButton breedButton(Player player, String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(breed.getBreedName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    plugin.getBuildAHorseMenu().openWithParameters(player, name, discipline, breed, gender, traits);
                });
    }
}
