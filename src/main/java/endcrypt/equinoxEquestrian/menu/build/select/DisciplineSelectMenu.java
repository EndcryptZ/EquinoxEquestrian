package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.attributes.Discipline;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class DisciplineSelectMenu implements Listener {


    private final EquinoxEquestrian plugin;
    private final Map<Player, EquineHorse> playerMap = new HashMap<>();
    public DisciplineSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Select Discipline Menu
    public Inventory disciplineMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Discipline", 3, "Discipline Menu");

        int slot = 0;
        for(Discipline discipline : Discipline.values()) {
            if(discipline == Discipline.NONE) continue;
            SGButton disciplineButton = disciplineButton(player, discipline, equineHorse);
            gui.setButton(slot, disciplineButton);
            slot++;
        }

        playerMap.put(player, equineHorse);

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
                    plugin.getBuildMenuManager().getBuildMenu().openWithParameters(player, equineHorse);
                });
    }

}
