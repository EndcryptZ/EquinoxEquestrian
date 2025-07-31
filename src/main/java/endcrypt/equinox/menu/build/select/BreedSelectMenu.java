package endcrypt.equinox.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.Breed;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BreedSelectMenu implements Listener {


    private final EquinoxEquestrian plugin;
    public BreedSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, List<Breed>> playerBreedMap = new HashMap<>();

    // Select Discipline Menu
    public Inventory breedMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Breed", 5, "Breed Menu");

        if(equineHorse.getBreeds().contains(Breed.NONE)) {
            equineHorse.setBreeds(new ArrayList<>());
        }

        playerBreedMap.put(player, equineHorse.getBreeds());


        int tempoSlot = 0;
        int slot = 0;
        for (Breed breed : Breed.values()) {
            if (breed == Breed.NONE) continue;
            if(tempoSlot == 0) {
                gui.setButton(slot + 40, confirmButton(player, equineHorse));
            }
            SGButton breedButton = breedButton(player, breed);
            if(tempoSlot == 36) {
                slot += 9;
                tempoSlot = 0;
                gui.setButton(slot, breedButton);
                continue;
            }


            gui.setButton(slot, breedButton);
            tempoSlot++;
            slot++;
        }

        gui.setAutomaticPaginationEnabled(true);

        return gui.getInventory();

    }

    private SGButton breedButton(Player player, Breed breed) {
        String breedName = breed.getName();

        boolean isBreedSelected = playerBreedMap.get(player).contains(breed);
        Material material = isBreedSelected ? Material.MAP : Material.PAPER;

        if (isBreedSelected) {
            breedName += " &a(Selected)";
        }

        return new SGButton(
                new ItemBuilder(material)
                        .name("&f" + breedName)
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    boolean isBreedCurrentlySelected = playerBreedMap.get(player).contains(breed);
                    List<Breed> playerBreeds = playerBreedMap.get(player);


                    if (isBreedCurrentlySelected) {
                        // remove trait
                        playerBreeds.remove(breed);

                        event.getCurrentItem().setType(Material.PAPER);
                        meta.setDisplayName("§f" + breed.getName());
                    } else {
                        if (playerBreeds.size() < 2) {
                            // add trait
                            playerBreeds.add(breed);

                            event.getCurrentItem().setType(Material.MAP);
                            meta.setDisplayName("§f" + breed.getName() + " §a(Selected)");
                        } else {
                            ItemUtils.itemMessage(plugin,
                                    event.getCurrentItem(),
                                    "§f" + breed.getName(),
                                    "§cYou can select up to 2 breeds only.",
                                    null,
                                    null
                            );
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1f, 1f);
                            event.setCancelled(true);
                            return;
                        }
                    }

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
                    List<Breed> playerBreeds = playerBreedMap.get(player);
                    if (playerBreeds != null && !playerBreeds.isEmpty()) {
                        equineHorse.setBreeds(playerBreeds);
                        plugin.getMenuManager().getBuildMenuManager().getBuildMenu().openWithParameters(player, equineHorse);
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§a§lCONFIRM", "§cYou need to select at least 1 breed to confirm.", null, null);
                        event.setCancelled(true);
                    }
                });
    }
}
