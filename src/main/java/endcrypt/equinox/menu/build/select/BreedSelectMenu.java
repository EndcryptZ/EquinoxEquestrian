package endcrypt.equinox.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.Breed;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.Trait;
import endcrypt.equinox.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BreedSelectMenu implements Listener {


    private final EquinoxEquestrian plugin;
    public BreedSelectMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private Map<Player, Breed[]> playerBreedMap = new HashMap<>();

    // Select Discipline Menu
    public Inventory breedMenu(Player player, EquineHorse equineHorse) {

        SGMenu gui = plugin.getSpiGUI().create("Select Breed", 5, "Breed Menu");

        if(Arrays.asList(equineHorse.getBreeds()).contains(Breed.NONE)) {
            equineHorse.setBreeds(new Breed[0]);
        }

        playerBreedMap.put(player, equineHorse.getBreeds());


        int slot = 0;
        for(Breed breed : Breed.values()) {
            if (breed == Breed.NONE) continue;
            SGButton breedButton = breedButton(player, breed);
            gui.setButton(slot, breedButton);
            slot++;
        }

        gui.setAutomaticPaginationEnabled(true);

        return gui.getInventory();

    }

    private SGButton breedButton(Player player, Breed breed) {
        String breedName = breed.getName();

        boolean isBreedSelected = Arrays.asList(playerBreedMap.get(player)).contains(breed);
        Material material = isBreedSelected ? Material.MAP : Material.PAPER;

        return new SGButton(
                new ItemBuilder(material)
                        .name(breed.getName())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
                    Breed[] breeds = playerBreedMap.get(player);

                    if (isBreedSelected) {
                        // remove breed
                        playerBreedMap.put(player, Arrays.stream(breeds)
                                .filter(b -> !b.equals(breed))
                                .toArray(Breed[]::new));

                        event.getCurrentItem().setType(Material.PAPER);
                        itemMeta.setDisplayName(breed.getName());
                    } else {
                        if (breeds.length < 2) {
                            // add breed
                            Breed[] newBreeds = Arrays.copyOf(breeds, breeds.length + 1);
                            newBreeds[breeds.length] = breed;
                            playerBreedMap.put(player, newBreeds);

                            event.getCurrentItem().setType(Material.MAP);
                            itemMeta.setDisplayName("§f" + breed.getName() + " §a(Selected)");
                        } else {
                            ItemUtils.itemMessage(plugin,
                                    event.getCurrentItem(),
                                    breed.getName(),
                                    "§cYou can only select 2 breeds",
                                    null,
                                    null
                            );
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                            event.setCancelled(true);
                            return;
                        }

                        event.getCurrentItem().setItemMeta(itemMeta);
                    }
                });
    }

    private SGButton confirmButton(Player player, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.EMERALD)
                        .name("&a&lCONFIRM")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Breed[] playerBreeds = playerBreedMap.get(player);
                    if (playerBreeds != null && playerBreeds.length >= 1) {
                        equineHorse.setBreeds(playerBreeds);
                        plugin.getBuildMenuManager().getBuildMenu().openWithParameters(player, equineHorse);
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§a§lCONFIRM", "§cYou need to select at least 1 breed to confirm.", null, null);
                        event.setCancelled(true);
                    }
                });
    }
}
