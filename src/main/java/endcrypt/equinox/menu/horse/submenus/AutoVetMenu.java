package endcrypt.equinox.menu.horse.submenus;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AutoVetMenu {

    private final EquinoxEquestrian plugin;
    public AutoVetMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public Inventory menu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create("Veterinary Actions", 1, "Veterinary Actions");



        gui.setButton(0, examineButton(player, horse));
        gui.setButton(1, geneticsButton(player, horse));
        gui.setButton(2, geldButton(player, horse));
        gui.setButton(3, cureIllnessesButton(player, horse));
        gui.setButton(4, cureInjuriesButton(player, horse));
        gui.setButton(5, giveVaccinesButton(player, horse));
        gui.setButton(6, ultrasoundButton(player, horse));


        return gui.getInventory();

    }

    private SGButton examineButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fExamine")
                        .lore(
                                "&7Examine aspects of the Horse.",
                                "&a$75.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }


    private SGButton geneticsButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fGenetics")
                        .lore(
                                "&7Get an insight to your",
                                "&7Horse's genetics.",
                                "&a$50.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

    private SGButton geldButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.SHEARS)
                        .name("&fGeld")
                        .lore(
                                "&7Gelds the Horse.",
                                "&a$50.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

    private SGButton cureIllnessesButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.SUGAR)
                        .name("&fCure Illnesses")
                        .lore(
                                "&7Cure any Illnesses the",
                                "&7Horse has obtained.",
                                "&a$25.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

    private SGButton cureInjuriesButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.ARROW)
                        .name("&fCure Injuries")
                        .lore(
                                "&7Cure any Injuries the",
                                "&7Horse has obtained.",
                                "&a$25.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

    private SGButton giveVaccinesButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.POTION)
                        .name("&fGive Vaccines")
                        .lore(
                                "&7Give the Horse any",
                                "&7Vaccines it is missing.",
                                "&a$25.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

    private SGButton ultrasoundButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fUltrasound")
                        .lore(
                                "&7Get an insight to your",
                                "&7Horse's pregnancy.",
                                "&a$50.00"

                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                });
    }

}
