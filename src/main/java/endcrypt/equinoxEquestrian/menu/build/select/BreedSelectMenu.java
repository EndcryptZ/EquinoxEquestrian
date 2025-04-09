package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.EquineHorse;
import endcrypt.equinoxEquestrian.horse.enums.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class BreedSelectMenu implements Listener {


    private final EquinoxEquestrian plugin;
    public BreedSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Select Discipline Menu
    public Inventory breedMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Breed", 5, "Breed Menu");


        int slot = 0;
        for(Breed breed : Breed.values()) {
            if (breed == Breed.NONE) continue;
            SGButton breedButton = breedButton(player, breed, equineHorse);
            gui.setButton(slot, breedButton);
            slot++;
        }

        gui.setAutomaticPaginationEnabled(true);

        return gui.getInventory();

    }

    private SGButton breedButton(Player player, Breed breed, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name(breed.getName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    equineHorse.setBreed(breed);
                    plugin.getBuildMenu().openWithParameters(player, equineHorse);
                });
    }
}
