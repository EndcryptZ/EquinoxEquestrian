package endcrypt.equinoxEquestrian.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ListOrganizerMenu {

    private final EquinoxEquestrian plugin;

    public ListOrganizerMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, ListOrganizeType listOrganizeType) {
        player.openInventory(createMenu(player, listOrganizeType));

    }

    private Inventory createMenu(Player player, ListOrganizeType listOrganizeType) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List Organizer", 3, "Horse List Organizer");

        gui.setButton(11, disciplineButton(player, listOrganizeType));
        gui.setButton(12, alphabeticalButton(player, listOrganizeType));
        gui.setButton(13, ageButton(player, listOrganizeType));
        gui.setButton(14, levelButton(player, listOrganizeType));
        gui.setButton(15, genderButton(player, listOrganizeType));

        return gui.getInventory();
    }

    private SGButton disciplineButton(Player player, ListOrganizeType listOrganizeType) {
        boolean isSelected = listOrganizeType == ListOrganizeType.DISCIPLINE;
        String loreIndicator = !isSelected ? "&c&l✘" : "&a&l✔";

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oDiscipline Order")
                        .lore(
                                loreIndicator,
                                "&7English - Western - Racing - Other"
                        )
                        .build()
        ).withListener(event -> {
            // Listener logic (currently empty)
        });
    }

    private SGButton alphabeticalButton(Player player, ListOrganizeType listOrganizeType) {
        boolean isSelected = listOrganizeType == ListOrganizeType.ALPHABETICAL;
        String loreIndicator = !isSelected ? "&c&l✘" : "&a&l✔";

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oAlphabetical Order")
                        .lore(
                                loreIndicator,
                                "  &7A > Z"
                        )
                        .build()
        ).withListener(event -> {
            plugin.getHorseMenu().getHorseListMenu().open(player, ListOrganizeType.ALPHABETICAL);
        });
    }

    private SGButton ageButton(Player player, ListOrganizeType listOrganizeType) {
        boolean isSelected = listOrganizeType == ListOrganizeType.AGE;
        String loreIndicator = !isSelected ? "&c&l✘" : "&a&l✔";

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oAge Order")
                        .lore(
                                loreIndicator,
                                "  &7Youngest > Oldest"
                        )
                        .build()
        ).withListener(event -> {
            plugin.getHorseMenu().getHorseListMenu().open(player, ListOrganizeType.AGE);
        });
    }

    private SGButton levelButton(Player player, ListOrganizeType listOrganizeType) {
        boolean isSelected = listOrganizeType == ListOrganizeType.LEVEL;
        String loreIndicator = !isSelected ? "&c&l✘" : "&a&l✔";

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oLevel Order")
                        .lore(
                                loreIndicator,
                                "  &7Lowest > Highest"
                        )
                        .build()
        ).withListener(event -> {
        });
    }

    private SGButton genderButton(Player player, ListOrganizeType listOrganizeType) {
        boolean isSelected = listOrganizeType == ListOrganizeType.GENDER;
        String loreIndicator = !isSelected ? "&c&l✘" : "&a&l✔";

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oGender Order")
                        .lore(
                                loreIndicator,
                                "&7Mare - Gelding - Stallion"
                        )
                        .build()
        ).withListener(event -> {
            plugin.getHorseMenu().getHorseListMenu().open(player, ListOrganizeType.GENDER);
        });
    }

}
