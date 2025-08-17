package endcrypt.equinox.menu.horse;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

public class HorseMenu {

    private final EquinoxEquestrian plugin;

    public HorseMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

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

                    if(plugin.getMenuManager().getHorseMenuManager().getInventoryHorseMenu().isHorseInventoryOpened(abstractHorse)) {
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§fInventory", ChatColor.RED + "The inventory is currently being edited by another player!", null, null);
                        return;
                    }

                    player.openInventory(plugin.getMenuManager().getHorseMenuManager().getInventoryHorseMenu().inventoryMenu(player, abstractHorse));
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

                    player.openInventory(plugin.getMenuManager().getHorseMenuManager().getHomeMenu().menu(player, abstractHorse));
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

                    player.openInventory(plugin.getMenuManager().getHorseMenuManager().getGroomMenu().menu(player, abstractHorse));
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
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getMenuManager().getHorseMenuManager().getAutoVetMenu().menu(player, abstractHorse));
                });
    }


    private SGButton healthButton(AbstractHorse abstractHorse) {

        return new SGButton(
                new ItemBuilder(Material.FIREWORK_ROCKET)
                        .flag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                        .name("&fHealth")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getMenuManager().getHorseMenuManager().getHealthMenu().menu(player, abstractHorse));
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
                        if (!canMountHorse(player, abstractHorse)) return;
                        abstractHorse.addPassenger(player);
                        player.closeInventory();
                    });
        }

    }

    public boolean canMountHorse(Player player, AbstractHorse horse) {
        int age = Keys.readPersistentData(horse, Keys.AGE);
        double thirst = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);
        double hunger = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);

        // check age restriction
        if (age < 4) {
            player.sendMessage(ColorUtils.color("<red>This horse is too young to ride. It must be at least 4 years old!"));
            player.closeInventory();
            return false;
        }

        if (thirst < 30) {
            player.sendMessage(ColorUtils.color("This horse is too thirsty to ride. It needs at least 30% thirst before you can ride it."));
            player.closeInventory();
            return false;
        }

        if (hunger < 30) {
            player.sendMessage(ColorUtils.color("This horse is too hungry to ride. It needs at least 30% hunger before you can ride it."));
            player.closeInventory();
            return false;
        }

        // add more conditions here in the future
        // e.g., cleanliness, health, taming, etc.

        return true;
    }

}
