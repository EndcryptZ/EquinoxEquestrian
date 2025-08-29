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
import java.util.concurrent.CompletableFuture;

public class HorseListMenu {

    private final EquinoxEquestrian plugin;
    public HorseListMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }


    public void open(Player viewer,
                     OfflinePlayer target,
                     ListOrganizeType listOrganizeType,
                     boolean isTrustedHorses) {

        // Run DB fetch async
        CompletableFuture
                .supplyAsync(() -> isTrustedHorses
                        ? plugin.getDatabaseManager().getDatabaseTrustedPlayers().getTrustedHorses(target)
                        : plugin.getDatabaseManager().getDatabaseHorses().getPlayerHorses(target))
                // Once horses are fetched, build the menu async-aware
                .thenCompose(horses -> createMenuAsync(target, listOrganizeType, isTrustedHorses, horses))
                .thenAccept(menu -> Bukkit.getScheduler().runTask(plugin, () -> {
                    viewer.openInventory(menu);
                }));
    }

    public CompletableFuture<Inventory> createMenuAsync(OfflinePlayer target,
                                                        ListOrganizeType listOrganizeType,
                                                        boolean isTrustedHorses,
                                                        List<EquineLiveHorse> horses) {

        return plugin.getPermissionManager().getMaxHorsesAllowedAsync(target).thenApplyAsync(maxHorses -> {
            String playerName = target.getName() != null ? target.getName() : "Unknown";

            String guiName = isTrustedHorses
                    ? "Trusted Horses of " + playerName
                    : "Horse List of " + playerName + " (" + horses.size() + "/" + maxHorses + ")";

            // Sort horses according to listOrganizeType
            List<EquineLiveHorse> sorted = switch (listOrganizeType) {
                case AGE -> horses.stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(EquineLiveHorse::getAge))
                        .toList();
                case ALPHABETICAL -> horses.stream()
                        .sorted(Comparator.comparing(h -> h.getName() != null ? h.getName() : ""))
                        .toList();
                case GENDER -> {
                    Map<Gender, Integer> order = Map.of(
                            Gender.MARE, 0,
                            Gender.GELDING, 1,
                            Gender.STALLION, 2
                    );
                    yield horses.stream()
                            .filter(Objects::nonNull)
                            .sorted(Comparator.comparingInt(h -> order.getOrDefault(h.getGender(), 3)))
                            .toList();
                }
                default -> new ArrayList<>(horses);
            };

            // GUI must be created on main thread
            CompletableFuture<Inventory> result = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(plugin, () -> {
                SGMenu gui = plugin.getSpiGUI().create(guiName, 4, "Horse List");

                gui.setButton(31, menuOrganiserButton(target, listOrganizeType, isTrustedHorses));
                gui.setButton(35, isTrustedHorses
                        ? horseListButton(target, listOrganizeType)
                        : trustedHorsesButton(target, listOrganizeType));

                int slot = 0;
                for (EquineLiveHorse horse : sorted) {
                    gui.setButton(slot++, horseButton(target, horse, isTrustedHorses));
                }

                result.complete(gui.getInventory());
            });
            return result.join();
        });
    }

    private Inventory createMenu(OfflinePlayer target,
                                 ListOrganizeType listOrganizeType,
                                 boolean isTrustedHorses,
                                 List<EquineLiveHorse> horses) {

        String playerName = target.getName() != null ? target.getName() : "Unknown";

        String guiName = isTrustedHorses
                ? "Trusted Horses of " + playerName
                : "Horse List of " + playerName + " ("
                + horses.size() + "/"
                + plugin.getPermissionManager().getMaxHorsesAllowed(target) + ")";

        SGMenu gui = plugin.getSpiGUI().create(guiName, 4, "Horse List");

        // Sort horses according to listOrganizeType
        List<EquineLiveHorse> sorted = switch (listOrganizeType) {
            case AGE -> horses.stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(EquineLiveHorse::getAge))
                    .toList();
            case ALPHABETICAL -> horses.stream()
                    .sorted(Comparator.comparing(h -> h.getName() != null ? h.getName() : ""))
                    .toList();
            case GENDER -> {
                Map<Gender, Integer> order = Map.of(Gender.MARE, 0, Gender.GELDING, 1, Gender.STALLION, 2);
                yield horses.stream()
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingInt(h -> order.getOrDefault(h.getGender(), 3)))
                        .toList();
            }
            default -> new ArrayList<>(horses);
        };

        // Fill GUI
        gui.setButton(31, menuOrganiserButton(target, listOrganizeType, isTrustedHorses));
        gui.setButton(35, isTrustedHorses ? horseListButton(target, listOrganizeType)
                : trustedHorsesButton(target, listOrganizeType));

        int slot = 0;
        for (EquineLiveHorse horse : sorted) {
            gui.setButton(slot++, horseButton(target, horse, isTrustedHorses));
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
                        plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open((Player) event.getWhoClicked(), player, listOrganizeType, true);
                        return;
                    }
                    plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), player, listOrganizeType, true);

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
                        plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open((Player) event.getWhoClicked(), player, listOrganizeType, false);
                        return;
                    }

                    plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), player, listOrganizeType, false);

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
