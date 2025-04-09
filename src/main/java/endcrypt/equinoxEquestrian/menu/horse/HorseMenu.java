package endcrypt.equinoxEquestrian.menu.horse;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.menu.horse.submenus.GroomMenu;
import endcrypt.equinoxEquestrian.menu.horse.submenus.HealthMenu;
import endcrypt.equinoxEquestrian.menu.horse.submenus.HomeMenu;
import endcrypt.equinoxEquestrian.menu.horse.submenus.InventoryHorseMenu;
import endcrypt.equinoxEquestrian.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class HorseMenu implements Listener {

    private final InventoryHorseMenu inventoryHorseMenu;
    private final HomeMenu homeMenu;
    private final GroomMenu groomMenu;
    private final HealthMenu healthMenu;

    private final EquinoxEquestrian plugin;
    public HorseMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        inventoryHorseMenu = new InventoryHorseMenu(plugin);
        homeMenu = new HomeMenu(plugin);
        groomMenu = new GroomMenu(plugin);
        healthMenu = new HealthMenu(plugin);


    }

    List<Material> allowedHorseRightClickItems = Arrays.asList(Material.LEAD, Material.WHEAT);

    public void open(Player player, AbstractHorse abstractHorse) {
        player.openInventory(createMenu(player, abstractHorse));

    }

    private Inventory createMenu(Player player, AbstractHorse abstractHorse) {
        SGMenu gui = plugin.getSpiGUI().create("Horse Menu", 1, "Horse Menu");

        SGButton inventoryButton = inventoryButton(abstractHorse);
        SGButton homeButton = homeButton(abstractHorse);
        SGButton groomButton = groomButton(abstractHorse);
        SGButton callVetButton = callVetButton(abstractHorse);
        SGButton healthButton = healthButton(abstractHorse);
        SGButton mountUnmountButton = mountUnmountButton(player, abstractHorse);

        gui.setButton(0, inventoryButton);
        gui.setButton(1, homeButton);
        gui.setButton(2, groomButton);

        gui.setButton(4, callVetButton);
        gui.setButton(5, healthButton);

        gui.setButton(8, mountUnmountButton);

        return gui.getInventory();
    }

    // Main Buttons
    private SGButton inventoryButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.CHEST)
                        .name("&fInventory")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    if(inventoryHorseMenu.isHorseInventoryOpened(abstractHorse)) {
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§fInventory", ChatColor.RED + "The inventory is currently being edited by another player!", null, null);
                        return;
                    }

                    player.openInventory(inventoryHorseMenu.inventoryMenu(player, abstractHorse));
                });
    }

    private SGButton homeButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.WHITE_BED)
                        .name("&fHome")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(homeMenu.menu(player, abstractHorse));
                });
    }

    private SGButton groomButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.FLINT)
                        .name("&fGroom")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(groomMenu.menu(player, abstractHorse));
                });
    }

    private SGButton callVetButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.NETHER_STAR)
                        .name("&fCall Vet")
                        .lore(
                                "&7If there's no vets available",
                                "&7call a vet from the clinic!",
                                "&7They cost a bit more than a",
                                "&7regular vet though."
                        )
                        .build()
        );
    }


    private SGButton healthButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.FIREWORK_ROCKET)
                        .name("&fHealth")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(healthMenu.menu(player, abstractHorse));
                });
    }

    private SGButton mountUnmountButton(Player player, AbstractHorse abstractHorse) {

        if(abstractHorse.getPassenger() == player) {
            return new SGButton(
                    new ItemBuilder(Material.SADDLE)
                            .name("&fUnmount")
                            .build()
            )
                    .withListener((InventoryClickEvent event) -> {
                        abstractHorse.removePassenger(player);
                        player.closeInventory();
                    });

        } else {
            return new SGButton(
                    new ItemBuilder(Material.SADDLE)
                            .name("&fMount")
                            .build()
            )
                    .withListener((InventoryClickEvent event) -> {
                        abstractHorse.addPassenger(player);
                        player.closeInventory();
                    });
        }
    }

    @EventHandler
    public void onHorseClick(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof AbstractHorse)) {
            return;
        }

        if(allowedHorseRightClickItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType())){
            return;
        }


        open(event.getPlayer(), (AbstractHorse) event.getRightClicked());
        event.setCancelled(true);
    }

    @EventHandler
    public void onHorseInventoryOpen(InventoryOpenEvent event) {
        if(!(event.getInventory() instanceof HorseInventory)) {
            return;
        }

        event.setCancelled(true);
        open((Player) event.getPlayer(), (AbstractHorse) event.getInventory().getHolder());
    }


}
