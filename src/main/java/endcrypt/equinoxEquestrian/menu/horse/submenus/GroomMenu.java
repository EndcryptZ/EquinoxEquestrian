package endcrypt.equinoxEquestrian.menu.horse.submenus;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.groom.EquineGroomManager;
import endcrypt.equinoxEquestrian.equine.items.Item;
import endcrypt.equinoxEquestrian.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GroomMenu {

    private final EquinoxEquestrian plugin;
    public GroomMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Home Menu
    public Inventory menu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create("Groom", 1, "Groom");


        gui.setButton(0, hardBrushButton(player, horse));
        gui.setButton(1, softBrushButton(player, horse));
        gui.setButton(2, hoofPickButton(player, horse));
        gui.setButton(3, flySprayButton(player, horse));
        gui.setButton(4, washButton(player, horse));
        gui.setButton(5, conditionerButton(player, horse));
        gui.setButton(6, shampooButton(player, horse));
        gui.setButton(7, hoofOilButton(player, horse));
        gui.setButton(8, sunCreamButton(player, horse));


        return gui.getInventory();

    }

        private SGButton hardBrushButton(Player player, AbstractHorse abstractHorse) {

            return new SGButton(
                    new ItemBuilder(Material.STRING)
                            .name("&fHard Brush")
                            .build()
            )
                    .withListener((InventoryClickEvent event) -> {
                        EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                        boolean success = groom.initializeGroom(player, abstractHorse, Item.HARD_BRUSH);

                        if (!success) {
                            ItemUtils.itemMessage(
                                    plugin,
                                    event.getCurrentItem(),
                                    "§fHard Brush",
                                    "§cYou have no Hard Brush in your inventory.",
                                    null,
                                    null
                            );
                        }
                    });
        }

    private SGButton softBrushButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.BOWL)
                        .name("&fSoft Brush")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.SOFT_BRUSH);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fSoft Brush",
                                "§cYou have no Soft Brush in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton hoofPickButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("&fHoof Pick")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.HOOF_PICK);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fHoof Pick",
                                "§cYou have no Hoof Pick in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton flySprayButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.POTION)
                        .name("&fFly Spray")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.FLY_SPRAY);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fFly Spray",
                                "§cYou have no Fly Spray in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton washButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.WATER_BUCKET)
                        .name("&fWash")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.WASH_HORSE);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fWash",
                                "§cYou have no Wash item in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton conditionerButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.DRAGON_BREATH)
                        .name("&eConditioner")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.CONDITIONER);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§eConditioner",
                                "§cYou have no Conditioner in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton shampooButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("&fShampoo")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.SHAMPOO);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fShampoo",
                                "§cYou have no Shampoo in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton hoofOilButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.GLASS_BOTTLE)
                        .name("&fHoof Oil")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.HOOF_OIL);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fHoof Oil",
                                "§cYou have no Hoof Oil in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }

    private SGButton sunCreamButton(Player player, AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.AMETHYST_SHARD)
                        .name("&fSun Cream")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineGroomManager groom = plugin.getEquineManager().getEquineGroomManager();
                    boolean success = groom.initializeGroom(player, abstractHorse, Item.SUN_CREAM);

                    if (!success) {
                        ItemUtils.itemMessage(
                                plugin,
                                event.getCurrentItem(),
                                "§fSun Cream",
                                "§cYou have no Sun Cream in your inventory.",
                                null,
                                null
                        );
                    }
                });
    }
}
