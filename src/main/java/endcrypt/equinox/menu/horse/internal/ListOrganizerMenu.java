package endcrypt.equinox.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ListOrganizerMenu {

    private final EquinoxEquestrian plugin;

    public ListOrganizerMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
        player.openInventory(createMenu(player, listOrganizeType, isTrustedHorse));

    }

    public void openToOther(Player player, OfflinePlayer target, ListOrganizeType listOrganizeType, boolean isTrustedHorses) {
        player.openInventory(createMenu(target, listOrganizeType, isTrustedHorses));
    }


    private Inventory createMenu(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List Organizer", 3, "Horse List Organizer");

        gui.setButton(11, disciplineButton(player, listOrganizeType, isTrustedHorse));
        gui.setButton(12, alphabeticalButton(player, listOrganizeType, isTrustedHorse));
        gui.setButton(13, ageButton(player, listOrganizeType, isTrustedHorse));
        gui.setButton(14, levelButton(player, listOrganizeType, isTrustedHorse));
        gui.setButton(15, genderButton(player, listOrganizeType, isTrustedHorse));

        return gui.getInventory();
    }

    private SGButton disciplineButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
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

    private SGButton alphabeticalButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
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
            if(player != event.getWhoClicked()) {
                plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, ListOrganizeType.ALPHABETICAL, isTrustedHorse);
                return;
            }
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), ListOrganizeType.ALPHABETICAL, isTrustedHorse);
        });
    }

    private SGButton ageButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
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
            if(player != event.getWhoClicked()) {
                plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, ListOrganizeType.AGE, isTrustedHorse);
                return;
            }
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), ListOrganizeType.AGE, isTrustedHorse);
        });
    }

    private SGButton levelButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
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

    private SGButton genderButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
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
            if(player != event.getWhoClicked()) {
                plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, ListOrganizeType.GENDER, isTrustedHorse);
                return;
            }
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), ListOrganizeType.GENDER, isTrustedHorse);
        });
    }

}
