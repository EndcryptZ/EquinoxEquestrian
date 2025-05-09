package endcrypt.equinoxEquestrian.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.EquineUtils;
import endcrypt.equinoxEquestrian.equine.attributes.Gender;
import endcrypt.equinoxEquestrian.utils.HeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.*;

public class    HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player player, ListOrganizeType listOrganizeType) {
        player.openInventory(createMenu(player, listOrganizeType));

    }

    private Inventory createMenu(Player player, ListOrganizeType listOrganizeType) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List", 4, "Horse List");

        try {
            List<UUID> horseIds = plugin.getDatabaseManager().getPlayerHorses(player);

            if (listOrganizeType == ListOrganizeType.AGE) {
                List<EquineHorse> equineHorses = horseIds.stream()
                        .map(Bukkit::getEntity)
                        .filter(e -> e instanceof AbstractHorse)
                        .map(e -> EquineUtils.fromAbstractHorse((AbstractHorse) e))
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(EquineHorse::getAge))
                        .toList();

                for (int i = 0; i < equineHorses.size(); i++) {
                    AbstractHorse horse = (AbstractHorse) Bukkit.getEntity(equineHorses.get(i).getUuid());
                    gui.setButton(i, horseButton(player, horse));
                }

            } else if (listOrganizeType == ListOrganizeType.ALPHABETICAL) {
                List<AbstractHorse> sorted = horseIds.stream()
                        .map(Bukkit::getEntity)
                        .filter(e -> e instanceof AbstractHorse)
                        .map(e -> (AbstractHorse) e)
                        .sorted(Comparator.comparing(h -> {
                            String name = h.getCustomName();
                            return name != null ? name.toLowerCase() : "";
                        }))
                        .toList();

                for (int i = 0; i < sorted.size(); i++) {
                    gui.setButton(i, horseButton(player, sorted.get(i)));
                }

            } else if (listOrganizeType == ListOrganizeType.GENDER) {
                // Define gender order: Mare = 0, Gelding = 1, Stallion = 2, None = 3
                Map<Gender, Integer> genderOrder = Map.of(
                        Gender.MARE, 0,
                        Gender.GELDING, 1,
                        Gender.STALLION, 2
                );

                List<EquineHorse> sortedByGender = horseIds.stream()
                        .map(Bukkit::getEntity)
                        .filter(e -> e instanceof AbstractHorse)
                        .map(e -> EquineUtils.fromAbstractHorse((AbstractHorse) e))
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(h -> genderOrder.getOrDefault(h.getGender(), 3)))
                        .toList();

                for (int i = 0; i < sortedByGender.size(); i++) {
                    AbstractHorse horse = (AbstractHorse) Bukkit.getEntity(sortedByGender.get(i).getUuid());
                    gui.setButton(i, horseButton(player, horse));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        gui.setButton(31, menuOrganiserButton(listOrganizeType));

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

    private SGButton horseButton(Player player, AbstractHorse horse) {
        boolean isSelected = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse() == horse;
        String displayName = "&f" + horse.getName() + (isSelected ? " &a(Selected)" : "");
        List<String> idList = Arrays.asList("49654", "7280", "49652", "49651", "1154", "3920", "3919", "2912", "7649");
        Random random = new Random();
        int randomID = random.nextInt(idList.size());
        String headID = idList.get(randomID);
        ItemStack head = HeadUtils.getItemHead(headID);


        return new SGButton(
                new ItemBuilder(head)
                        .name(displayName)
                        .build())


                .withListener((InventoryClickEvent event) -> {
                    plugin.getHorseMenuManager().getHorseInfoMenu().open(player, horse, ListOrganizeType.AGE);
                });
    }
}
