package endcrypt.equinox.menu.build.select;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.Trait;
import endcrypt.equinox.utils.ItemUtils;
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

        SGMenu gui = plugin.getSpiGUI().create("Select Traits", 6, "Trait Menu");

        if(Arrays.asList(equineHorse.getTraits()).contains(Trait.NONE)) {
            equineHorse.setTraits(new Trait[0]);
        }

        playerTraitMap.put(player, equineHorse.getTraits());

        int slot = 0;
        for(Trait trait : Trait.values()) {
            if(trait == Trait.NONE) continue;
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

        boolean isTraitSelected = Arrays.asList(playerTraitMap.get(player)).contains(trait);
        Material material = isTraitSelected ? Material.MAP : Material.PAPER;

        if (isTraitSelected) {
            traitName += " &a(Selected)";
        }

        return new SGButton(
                new ItemBuilder(material)
                        .name("&f" + traitName)
                        .lore("&7" + trait.getTraitType())
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    ItemMeta meta = event.getCurrentItem().getItemMeta();
                    Trait[] playerTraits = playerTraitMap.get(player);

                    if (isTraitSelected) {
                        // remove trait
                        playerTraitMap.put(player, Arrays.stream(playerTraits)
                                .filter(t -> !t.equals(trait))
                                .toArray(Trait[]::new));

                        event.getCurrentItem().setType(Material.PAPER);
                        meta.setDisplayName("§f" + trait.getTraitName());
                    } else {
                        if (playerTraits.length < 3) {
                            // add trait
                            Trait[] newTraits = Arrays.copyOf(playerTraits, playerTraits.length + 1);
                            newTraits[playerTraits.length] = trait;
                            playerTraitMap.put(player, newTraits);

                            event.getCurrentItem().setType(Material.MAP);
                            meta.setDisplayName("§f" + trait.getTraitName() + " §a(Selected)");
                        } else {
                            ItemUtils.itemMessage(plugin,
                                    event.getCurrentItem(),
                                    "§f" + trait.getTraitName(),
                                    "§cYou can select up to 3 traits only.",
                                    Collections.singletonList("§7" + trait.getTraitType()),
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
                    Trait[] playerTraits = playerTraitMap.get(player);
                    if (playerTraits != null && playerTraits.length >= 1) {
                        equineHorse.setTraits(playerTraits);
                        plugin.getBuildMenuManager().getBuildMenu().openWithParameters(player, equineHorse);
                    } else {
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§a§lCONFIRM", "§cYou need to select at least 1 trait to confirm.", null, null);
                        event.setCancelled(true);
                    }
                });
    }


}