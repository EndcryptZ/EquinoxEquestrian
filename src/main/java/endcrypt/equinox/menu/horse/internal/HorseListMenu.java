package endcrypt.equinox.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.player.data.PlayerData;
import endcrypt.equinox.utils.HeadUtils;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class    HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player player, ListOrganizeType listOrganizeType) {
        player.openInventory(createMenu(player, listOrganizeType));

    }

    public void openToOther(Player player, Player target, ListOrganizeType listOrganizeType) {
        player.openInventory(createMenu(target, listOrganizeType));
    }

    private Inventory createMenu(Player player, ListOrganizeType listOrganizeType) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List", 4, "Horse List");

        List<EquineLiveHorse> horseIds = plugin.getDatabaseManager().getPlayerHorses(player);
        List<EquineLiveHorse> sortedHorses;

        switch (listOrganizeType) {
            case AGE -> sortedHorses = horseIds.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(EquineLiveHorse::getAge))
                    .toList();
            case ALPHABETICAL -> sortedHorses = horseIds.stream()
                    .sorted(Comparator.comparing(h -> h.getName() != null ? h.getName() : ""))
                    .toList();
            case GENDER -> {
                Map<Gender, Integer> genderOrder = Map.of(
                        Gender.MARE, 0,
                        Gender.GELDING, 1,
                        Gender.STALLION, 2
                );
                sortedHorses = horseIds.stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(h -> genderOrder.getOrDefault(h.getGender(), 3)))
                        .toList();
            }
            default -> sortedHorses = new ArrayList<>(horseIds);
        }

        // Set the organizer button in the last row
        gui.setButton(31, menuOrganiserButton(listOrganizeType));

        int tempoSlot = 0;
        int slot = 0;
        for (EquineLiveHorse equineHorse : sortedHorses ) {
            if(tempoSlot == 0) {
                if(gui.getCurrentPage() > 1) gui.setButton(slot + 30, previousPageButton(gui));
                if(gui.getMaxPage() != gui.getCurrentPage()) gui.setButton(slot + 32, nextPageButton(gui));
                gui.setButton(slot + 31, menuOrganiserButton(listOrganizeType));
            }
            SGButton horseButton = horseButton(player, equineHorse);
            if(tempoSlot == 27) {
                slot += 9;
                tempoSlot = 0;
                gui.setButton(slot, horseButton);
                continue;
            }


            gui.setButton(slot, horseButton);
            tempoSlot++;
            slot++;
        }



        return gui.getInventory();
    }


    private SGButton menuOrganiserButton(ListOrganizeType listOrganizeType) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oMenu Organizer")
                        .lore("&7▸ &bCurrent Organizer: &7" + listOrganizeType.getName())
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();
                    plugin.getHorseMenuManager().getListOrganizerMenu().open(player, listOrganizeType);


                });
    }

    private SGButton trustedHorsesButton() {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fTrusted Horses")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();


                });
    }

    private SGButton horseButton(Player player, EquineLiveHorse equineLiveHorse) {
        boolean isSelected;
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        AbstractHorse selectedHorse = playerData.getSelectedHorse();
        isSelected = selectedHorse != null && selectedHorse.getUniqueId().equals(equineLiveHorse.getUuid());
        String displayName = "&f" + equineLiveHorse.getName() + (isSelected ? " &a(Selected)" : "");
        ItemStack head = HeadUtils.getItemHead(equineLiveHorse.getSkullId());


        return new SGButton(
                new ItemBuilder(head)
                        .name(displayName)
                        .build())


                .withListener((InventoryClickEvent event) -> {
                    Player playerClick = (Player) event.getWhoClicked();
                    plugin.getHorseMenuManager().getHorseInfoMenu().open(playerClick, equineLiveHorse, ListOrganizeType.AGE);
                });
    }

    private SGButton previousPageButton(SGMenu gui) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&c&lPrevious Page")
                        .lore(
                                "&aClick to move back to",
                                "&apage " + (gui.getCurrentPage() - 1)
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    gui.setCurrentPage(gui.getCurrentPage() - 1);
                });
    }

    private SGButton nextPageButton(SGMenu gui) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&a&lNext Page")
                        .lore(
                                "&aClick to move forward to",
                                "&apage " + (gui.getCurrentPage() + 1)
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    gui.setCurrentPage(gui.getCurrentPage() + 1);
                });
    }
}
