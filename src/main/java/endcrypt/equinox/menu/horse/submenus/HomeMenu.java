package endcrypt.equinox.menu.horse.submenus;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.*;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeMenu {

    private final EquinoxEquestrian plugin;
    public HomeMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    // Home Menu
    public Inventory menu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create("Home", 1, "Home");


        gui.setButton(3, stallButton(player, horse));
        gui.setButton(5, pastureButton(player, horse));


        return gui.getInventory();

    }


    private SGButton stallButton(Player player, AbstractHorse horse) {

        List<String> lore;

        // Check if the horse has a stall location saved in the NBT data
        if (NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_STALL")).equalsIgnoreCase("true")) {
            double x = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_X"));
            double y = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Y"));
            double z = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Z"));
            String world = NBT.getPersistentData(horse, nbt -> nbt.getString("STALL_WORLD"));
            lore = new ArrayList<>(Arrays.asList(
                    "&aClick to teleport!"
            ));
        } else {
            lore = new ArrayList<>(Arrays.asList(
                    "&cNo Location Found!"
            ));
        }

        return new SGButton(
                new ItemBuilder(Material.SMOOTH_SANDSTONE)
                        .name("&fStall")
                        .lore(lore)
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    event.setCancelled(true);  // Prevent moving the button

                    // Check if the horse already has a stall location
                    if (NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_STALL")).equalsIgnoreCase("true")) {
                        // Teleport player to the stall location
                        World stallWorld = Bukkit.getWorld((String) NBT.getPersistentData(horse, nbt -> nbt.getString("STALL_WORLD")));
                        if (stallWorld != null) {
                            double stallX = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_X"));
                            double stallY = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Y"));
                            double stallZ = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Z"));
                            Location stallLocation = new Location(stallWorld, stallX, stallY, stallZ);

                            player.teleport(stallLocation);
                            horse.teleport(stallLocation);
                            player.sendMessage(ChatColor.GREEN + "You have been teleported to the horse's stall location!");
                        } else {
                            player.sendMessage(ChatColor.RED + "World not found for the stall location!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "No stall location found for this horse!");
                    }
                });
    }

    private SGButton pastureButton(Player player, AbstractHorse horse) {

        List<String> lore;

        // Check if the horse has a stall location saved in the NBT data
        if (NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_PASTURE")).equalsIgnoreCase("true")) {
            double x = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_X"));
            double y = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Y"));
            double z = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Z"));
            String world = NBT.getPersistentData(horse, nbt -> nbt.getString("PASTURE_WORLD"));
            lore = new ArrayList<>(Arrays.asList(
                    "&aClick to teleport!"
            ));
        } else {
            lore = new ArrayList<>(Arrays.asList(
                    "&cNo Location Found!"
            ));
        }

        return new SGButton(
                new ItemBuilder(Material.GRASS_BLOCK)
                        .name("&fPasture")
                        .lore(lore)
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    event.setCancelled(true);  // Prevent moving the button

                    // Check if the horse already has a stall location
                    if (NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_PASTURE")).equalsIgnoreCase("true")) {
                        // Teleport player to the stall location
                        World pastureWorld = Bukkit.getWorld((String) NBT.getPersistentData(horse, nbt -> nbt.getString("PASTURE_WORLD")));
                        if (pastureWorld != null) {
                            double pastureX = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_X"));
                            double pastureY = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Y"));
                            double pastureZ = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Z"));
                            Location pastureLocation = new Location(pastureWorld, pastureX, pastureY, pastureZ);

                            player.teleport(pastureLocation);
                            horse.teleport(pastureLocation);
                            player.sendMessage(ChatColor.GREEN + "You have been teleported to the horse's pasture location!");
                        } else {
                            player.sendMessage(ChatColor.RED + "World not found for the pasture location!");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "No pasture location found for this horse!");
                    }
                });
    }
}
