package endcrypt.equinoxEquestrian.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.EquineHorse;
import endcrypt.equinoxEquestrian.horse.enums.*;
import endcrypt.equinoxEquestrian.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class TraitSelectMenu {


    private final EquinoxEquestrian plugin;
    public TraitSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private Map<Player, Trait[]> playerTraitMap = new HashMap<>();

    // Select Traits Menu
    public Inventory traitMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Traits", 6, "Select Traits");

        playerTraitMap.put(player, equineHorse.getTraits());

        int slot = 0;
        for(Trait trait : Trait.values()) {
            SGButton traitButton = traitButton(player, trait);
            gui.setButton(slot, traitButton);
            slot++;
        }

        SGButton confirmButton = confirmButton(player, equineHorse);
        gui.setButton(49, confirmButton);

        return gui.getInventory();

    }

    private SGButton traitButton(Player player, Trait trait) {
        String traitName = trait.getTraitName();

        if(Arrays.stream(playerTraitMap.get(player)).toList().contains(trait)) {
            traitName = traitName + " &a(Selected)";
        }

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f" + traitName)
                        .lore(
                                "&7" + trait.getTraitType())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();

                    // Get the current traits of the player
                    Trait[] playerTraits = playerTraitMap.get(player);

                    // Check if the player already has 3 traits selected
                    if (playerTraits != null && playerTraits.length == 3) {
                        // If player has 3 traits, don't allow selecting an additional trait
                        if (Arrays.stream(playerTraits).noneMatch(t -> t.equals(trait))) {
                            List<String> defaultLore = new ArrayList<>();
                            defaultLore.add("§7" + trait.getTraitType());

                            ItemUtils.itemMessage(plugin,
                                    event.getCurrentItem(),
                                    "§f" + trait.getTraitName(),
                                    "§cYou cannot select more than 3 traits.",
                                    defaultLore,
                                    null
                            );
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            return;
                        }
                    }

                    // If the trait is already selected, remove it from the player's traits
                    if (Arrays.asList(playerTraits).contains(trait)) {
                        // Remove the trait from the array
                        playerTraitMap.put(player, Arrays.stream(playerTraits)
                                .filter(t -> !t.equals(trait)) // Filter out the selected trait
                                .toArray(Trait[]::new));

                        meta.setDisplayName("§f" + trait.getTraitName()); // Update the display name
                    } else {
                        // If not selected yet, add it to the array and set the name as selected
                        if (playerTraits.length < 3) {
                            playerTraitMap.put(player, Arrays.copyOf(playerTraits, playerTraits.length + 1));  // Resize the array
                            playerTraitMap.get(player)[playerTraits.length] = trait;  // Add the new trait to the last position

                            meta.setDisplayName("§f" + trait.getTraitName() + " §a(Selected)");
                        } else {
                            List<String> defaultLore = new ArrayList<>();
                            defaultLore.add("§7" + trait.getTraitType());

                            ItemUtils.itemMessage(plugin,
                                    event.getCurrentItem(),
                                    "§f" + trait.getTraitName(),
                                    "§cYou cannot select more than 3 traits.",
                                    defaultLore,
                                    null
                            );
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            event.setCancelled(true);  // Prevent further action
                            return;
                        }
                    }

                    // Apply the updated meta to the item
                    event.getCurrentItem().setItemMeta(meta);
                });
    }

    private SGButton confirmButton(Player player, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.EMERALD)
                        .name("&a&lCONFIRM")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    // Check if the player has exactly 3 traits
                    Trait[] playerTraits = playerTraitMap.get(player);
                    if (playerTraits != null && playerTraits.length == 3) {
                        // If the player has 3 traits, proceed with opening the menu
                        equineHorse.setTraits(playerTraits);
                        plugin.getBuildAHorseMenu().openWithParameters(player, equineHorse);
                    } else {
                        // If the player doesn't have 3 traits, play the BLOCK_ANVIL_LAND sound and cancel the event
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§a§lCONFIRM", "§cYou need exactly 3 traits to confirm.", null, null);
                        event.setCancelled(true);  // Prevent further actions (optional)
                    }
                });
    }


}
