package endcrypt.equinoxEquestrian.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.utils.HeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class    HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player player) {
        player.openInventory(createMenu(player));

    }

    private Inventory createMenu(Player player) {
        SGMenu gui = plugin.getSpiGUI().create("Horse List", 3, "Horse List");

        try {
            List<UUID> horses = plugin.getDatabaseManager().getPlayerHorses(player);

            for (int i = 0; i < horses.size(); i++) {
                gui.setButton(i, horseButton(player, (AbstractHorse) Bukkit.getEntity(horses.get(i))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        gui.setButton(31, menuOrganiserButton());
        gui.setButton(35, trustedHorsesButton());


        return gui.getInventory();
    }

    private SGButton menuOrganiserButton() {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oMenu Organiser")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();


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
        boolean isSelected = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse() == horse;
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
                    plugin.getHorseMenu().getHorseInfoMenu().open(player, horse);
                });
    }
}
