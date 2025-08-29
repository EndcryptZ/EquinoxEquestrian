package endcrypt.equinox.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.player.data.PlayerData;
import endcrypt.equinox.utils.HeadUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player player, ListOrganizeType listOrganizeType, boolean isTrustedHorses) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> player.getPlayer().openInventory(createMenu(player, listOrganizeType, isTrustedHorses)));
    }

    public void openToOther(Player player, OfflinePlayer target, ListOrganizeType listOrganizeType, boolean isTrustedHorses) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> player.openInventory(createMenu(target, listOrganizeType, isTrustedHorses)));
    }

    private Inventory createMenu(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorses) {
        String guiName = isTrustedHorses ? "Trusted Horses of " + player.getName() : "Horse List of " + player.getName() + " (" + plugin.getDatabaseManager().getDatabaseHorses().getPlayerHorses(player).size() + "/" + plugin.getPermissionManager().getMaxHorsesAllowed(player) + ")" ;

        SGMenu gui = plugin.getSpiGUI().create(guiName, 4, "Horse List");

        SGButton trustedHorsesButton = isTrustedHorses
                ? horseListButton(player, listOrganizeType)
                : trustedHorsesButton(player, listOrganizeType);

        List<EquineLiveHorse> horseIds = isTrustedHorses
                ? plugin.getDatabaseManager().getDatabaseTrustedPlayers().getTrustedHorses(player)
                : plugin.getDatabaseManager().getDatabaseHorses().getPlayerHorses(player);

        List<EquineLiveHorse> sortedHorses;
        switch (listOrganizeType) {
            case AGE -> sortedHorses = horseIds.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(EquineLiveHorse::getAge))
                    .toList();
            case ALPHABETICAL -> sortedHorses = horseIds.stream()
                    .sorted(Comparator.comparing(h -> h.getName() != null ? h.getName() : ""))
                    .toList();
            case GENDER -> {
                Map<Gender, Integer> genderOrder = Map.of(
                        Gender.MARE, 0,
                        Gender.GELDING, 1,
                        Gender.STALLION, 2
                );
                sortedHorses = horseIds.stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(h -> genderOrder.getOrDefault(h.getGender(), 3)))
                        .toList();
            }
            default -> sortedHorses = new ArrayList<>(horseIds);
        }

        int tempoSlot = 0;
        int slot = 0;
        gui.setButton(slot + 31, menuOrganiserButton(player, listOrganizeType, isTrustedHorses));
        gui.setButton(slot + 35, trustedHorsesButton);

        for (EquineLiveHorse equineHorse : sortedHorses) {
            EquineLiveHorse liveHorse = null;
            // Try to get live Bukkit entity
            AbstractHorse entity = (AbstractHorse) Bukkit.getEntity(equineHorse.getUuid());
            if (entity != null) {
                liveHorse = new EquineLiveHorse(entity);
            }

            if (tempoSlot == 0) {
                if (gui.getMaxPage() > 0) {
                    if (slot > (gui.getRowsPerPage() * 9) - 1)
                        gui.setButton(slot + 30, previousPageButton(gui));
                    if (sortedHorses.size() > slot + 27)
                        gui.setButton(slot + 32, nextPageButton(gui));
                }
                gui.setButton(slot + 31, menuOrganiserButton(player, listOrganizeType, isTrustedHorses));
                gui.setButton(slot + 35, trustedHorsesButton);
            }

            // Use live horse if loaded, otherwise use database object
            SGButton horseButton = (entity != null)
                    ? horseButton(player, liveHorse, isTrustedHorses) // overload for live horse
                    : horseButton(player, equineHorse, isTrustedHorses);

            if (tempoSlot == 27) {
                slot += 9;
                tempoSlot = 0;
                gui.setButton(slot, horseButton);
                continue;
            }

            gui.setButton(slot, horseButton);
            tempoSlot++;
            slot++;
        }

        return gui.getInventory();
    }


    private SGButton menuOrganiserButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorses) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&f&l&oMenu Organizer")
                        .lore("&7▸ &bCurrent Organizer: &7" + listOrganizeType.getName())
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    if(player != event.getWhoClicked()) {
                        plugin.getMenuManager().getHorseMenuManager().getListOrganizerMenu().openToOther((Player) event.getWhoClicked(), player, listOrganizeType, isTrustedHorses);
                        return;
                    }
                    plugin.getMenuManager().getHorseMenuManager().getListOrganizerMenu().open(player.getPlayer(), listOrganizeType, isTrustedHorses);


                });
    }

    private SGButton trustedHorsesButton(OfflinePlayer player, ListOrganizeType listOrganizeType) {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fTrusted Horses")
                        .lore("&7View horses that other players",
                                "&7have trusted you",
                                "",
                                "&eClick to view trusted horses")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    if(player != event.getWhoClicked()) {
                        plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, listOrganizeType, true);
                        return;
                    }
                    plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), listOrganizeType, true);

                });
    }


    private SGButton horseListButton(OfflinePlayer player, ListOrganizeType listOrganizeType) {

        return new SGButton(
                new ItemBuilder(Material.WRITABLE_BOOK)
                        .name("&fHorse List")
                        .lore("&7View horses that you owned",
                                "",
                                "&eClick to view your owned horses")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    if(player != event.getWhoClicked()) {
                        plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, listOrganizeType, false);
                        return;
                    }

                    plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), listOrganizeType, false);

                });
    }

    private SGButton horseButton(OfflinePlayer player, EquineLiveHorse equineLiveHorse, boolean isTrustedHorses) {
        boolean isSelected = false;
        if(player.isOnline()) {
            PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player.getPlayer());
            AbstractHorse selectedHorse = playerData.getSelectedHorse();
            isSelected = selectedHorse != null && selectedHorse.getUniqueId().equals(equineLiveHorse.getUuid());
        }
        String displayName = "&f" + equineLiveHorse.getName() + (isSelected ? " &a(Selected)" : "");
        ItemStack head = HeadUtils.getItemHead(equineLiveHorse.getSkullId());


        return new SGButton(
                new ItemBuilder(head)
                        .name(displayName)
                        .build())


                .withListener((InventoryClickEvent event) -> {
                    if(player != event.getWhoClicked()) {
                        plugin.getMenuManager().getHorseMenuManager().getHorseInfoMenu().openToOther((Player) event.getWhoClicked(), player, equineLiveHorse, ListOrganizeType.AGE, isTrustedHorses);
                        return;
                    }

                    plugin.getMenuManager().getHorseMenuManager().getHorseInfoMenu().open(player.getPlayer(), equineLiveHorse, ListOrganizeType.AGE, isTrustedHorses);
                });
    }

    private SGButton previousPageButton(SGMenu gui) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&c&lPrevious Page")
                        .lore(
                                "&aClick to move back to",
                                "&apage " + (gui.getMaxPage() - 1)
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    gui.previousPage(event.getWhoClicked());
                });
    }

    private SGButton nextPageButton(SGMenu gui) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&a&lNext Page")
                        .lore(
                                "&aClick to move forward to",
                                "&apage " + (gui.getMaxPage() + 1)
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    gui.nextPage(event.getWhoClicked());
                });
    }
}
