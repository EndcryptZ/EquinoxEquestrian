package endcrypt.equinox.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.attributes.Breed;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.equine.attributes.Trait;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.HeadUtils;
import endcrypt.equinox.utils.TimeUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class    HorseInfoMenu {

    private final EquinoxEquestrian plugin;
    public HorseInfoMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, EquineLiveHorse horse, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
        player.openInventory(createMenu(player, horse, listOrganizeType, isTrustedHorse));

    }

    public void openToOther(Player player, OfflinePlayer target, EquineLiveHorse horse, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
        player.openInventory(createMenu(target, horse, listOrganizeType, isTrustedHorse));
    }

    private Inventory createMenu(OfflinePlayer player, EquineLiveHorse horse, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {

        SGMenu gui = plugin.getSpiGUI().create(horse.getName() + "'s Info", 4, "Horse Info");


        gui.setButton(10, horseHead(horse));
        gui.setButton(12, horseInformation1(horse));
        gui.setButton(13, horseInformation2(horse));
        gui.setButton(14, motionInformation(horse));
        gui.setButton(15, welfareInformation(horse));
        gui.setButton(16, horseInformation3(horse));
        gui.setButton(17, healthInformation(horse));

        gui.setButton(31, backButton(player, listOrganizeType, isTrustedHorse));

        return gui.getInventory();
    }

    private SGButton horseHead(EquineLiveHorse horse) {
        return new SGButton(
                new ItemBuilder(HeadUtils.getItemHead(horse.getSkullId()))
                        .name("&f" + horse.getName())
                        .lore("&7▸ ?")
                        .build()
        )
                .withListener((InventoryClickEvent event ) -> {
                    Player player = (Player) event.getWhoClicked();
                    AbstractHorse abstractHorse = EquineUtils.findHorseByUuidAndLocation(horse.getUuid(), horse.getLastLocation());

                    if(abstractHorse == null) {
                        player.sendMessage(ColorUtils.color("<red>Horse '<horse><red>' not found. The horse has been permanently removed from the database.",
                                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(LegacyComponentSerializer.legacySection().deserialize(horse.getName())))));
                        plugin.getDatabaseManager().getDatabaseHorses().removeHorse(horse.getUuid());
                        player.closeInventory();
                        return;
                    }
                    plugin.getEquineManager().getEquineSelector().selectHorse(player, abstractHorse);
                });
    }

    private SGButton horseInformation1(EquineLiveHorse horse) {
        return new SGButton(
                new ItemBuilder(Material.FILLED_MAP)
                        .name("&fHorse Information")
                        .lore(
                                "&7▸ &bOwner: &7" + horse.getOwnerName(),
                                "&7▸ &bRegistered: &7WIP",
                                "&7▸ &bID: &7WIP",
                                "&7▸ &bClaim Date: &7" + TimeUtils.formatEpochToDate(horse.getClaimTime())
                                )
                        .build()
        );
    }

    private SGButton horseInformation2(EquineLiveHorse equineHorse) {
        List<String> loreList = new ArrayList<>();

        loreList.add("&7▸ &bName: &7" + equineHorse.getName());
        loreList.add("&7▸ &bBarn-Name: &7WIP");

        // Breed section (3rd line starts here)
        loreList.add("&7▸ &bBreeds:");
        List<Breed> breeds = equineHorse.getBreeds();
        Breed prominent = equineHorse.getProminentBreed();

        if (breeds.isEmpty()) {
            loreList.add("    &7▸ &bNone");
        } else {
            if (breeds.size() >= 1 && breeds.get(0) != null) {
                String line = "    &7▸ &bBreed 1: &7" + breeds.get(0).getName();
                if (prominent != null && prominent == breeds.get(0)) {
                    line += " (Prominent)";
                }
                loreList.add(line);
            }

            if (breeds.size() >= 2 && breeds.get(1) != null) {
                String line = "    &7▸ &bBreed 2: &7" + breeds.get(1).getName();
                if (prominent != null && prominent == breeds.get(1)) {
                    line += " (Prominent)";
                }
                loreList.add(line);
            }
        }

        // Remaining static lines
        loreList.add("&7▸ &bAge: &7" + equineHorse.getAge());
        loreList.add("&7▸ &bGender: &7" + equineHorse.getGender().getGenderName());
        loreList.add("&7▸ &bCoat Colour: &7" + equineHorse.getCoatColor().getCoatColorName());
        loreList.add("&7▸ &bCoat Modifier: &7" + equineHorse.getCoatModifier().getCoatModifierName());
        loreList.add("&7▸ &bHeight: &7" + equineHorse.getHeight().getHandsString());

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHorse Information 2")
                        .lore(loreList)
                        .build()
        );
    }



    private SGButton motionInformation(EquineLiveHorse equineHorse){

        String[] baseLore = {
                "&7▸ &bSpeed: ",
                "  &7▸ &bWalk: &7" + EquineUtils.minecraftSpeedToBlocks(equineHorse.getWalkSpeed()) + " mph",
                "  &7▸ &bTrot: &7" + EquineUtils.minecraftSpeedToBlocks(equineHorse.getTrotSpeed()) + " mph",
                "  &7▸ &bCanter: &7" + EquineUtils.minecraftSpeedToBlocks(equineHorse.getCanterSpeed()) + " mph",
                "  &7▸ &bGallop: &7" + EquineUtils.minecraftSpeedToBlocks(equineHorse.getGallopSpeed()) + " mph",
                "&7▸ &bJump: &7" + EquineUtils.minecraftJumpStrengthToBlocks(equineHorse.getBaseJumpPower()) + " block(s)",
                "&7▸ &bStamina: &7WIP",
                "&7▸ &bLevel: &7WIP",
                "&7▸ &bDiscipline: &7" + equineHorse.getDiscipline().getDisciplineName(),
                "&7▸ &bTraits:"
        };

        // Build dynamic trait display
        List<String> loreList = new ArrayList<>(List.of(baseLore));
        List<Trait> traits = equineHorse.getTraits();

        if (traits.isEmpty()) {
            loreList.add("    &7▸ &bNone");
        } else {
            for (int i = 0; i < traits.size(); i++) {
                if( traits.get(i) == null ) continue;
                loreList.add("    &7▸ &bTrait " + (i + 1) + ": &7" + traits.get(i).getTraitName());
            }
        }

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fMotion")
                        .lore(loreList)
                        .build()
        );
    }

    private SGButton welfareInformation(EquineLiveHorse horse) {
        double hunger = Math.max(0, Math.min(100, horse.getHungerPercentage()));
        double thirst = Math.max(0, Math.min(100, horse.getThirstPercentage()));

        // Hunger color
        String hungerColor = getLevelColor(hunger);
        // Thirst color
        String thirstColor = getLevelColor(thirst);

        // Truncate without rounding
        double truncatedHunger = Math.floor(hunger * 100) / 100.0;
        double truncatedThirst = Math.floor(thirst * 100) / 100.0;

        String hungerText = hungerColor + String.format("%.2f%%", truncatedHunger);
        String thirstText = thirstColor + String.format("%.2f%%", truncatedThirst);

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fWelfare")
                        .lore(
                                "&7▸ &bHappiness: &7WIP",
                                "&7▸ &bRelationship: &7WIP",
                                "&7▸ &bNutrition:",
                                "&7▸ &b+ Hunger: " + hungerText,
                                "&7▸ &b+ Thirst: " + thirstText,
                                "&7▸ &b+ Weight: &7WIP",
                                "&7▸ &bCleanliness: &7WIP"
                        )
                        .build()
        );
    }

    private String getLevelColor(double value) {
        if (value >= 60) return "&a"; // Green
        else if (value >= 40) return "&6"; // Orange
        else if (value >= 30) return "&c"; // Light Red
        else return "&4"; // Dark Red
    }


    private SGButton horseInformation3(EquineLiveHorse horse) {
        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHorse Information 3")
                        .lore(
                                "&bShod: &7WIP",
                                "&bRideable: &7WIP",
                                "&bSenses:",
                                "&b  + Vision: &7WIP",
                                "&b  + Hearing: &7WIP",
                                "&bTags:",
                                "&b  + WIP",
                                "&bEarnings: &7WIP"
                        )
                        .build()
        );
    }

    private SGButton healthInformation(EquineLiveHorse horse) {
        List<String> lore = new ArrayList<>();

        if (horse.getGender() == Gender.MARE) {
            String isInHeat = horse.isInHeat() ? "&aYes" : "&cNo";
            String isPregnant = horse.isPregnant() ? "&aYes" : "&cNo";
            lore.add("&bIn-Heat: " + isInHeat);
            lore.add("&bPregnant: " + isPregnant);
        }

        lore.add("&bVaccines:");
        lore.add("&7+ WIP");
        lore.add("&7+ WIP + WIP + WIP");
        lore.add("&bIllnesses:");
        lore.add("&7+ WIP");
        lore.add("&7+ WIP + WIP + WIP");
        lore.add("&bInjury:");
        lore.add("&7+ WIP");
        lore.add("&7+ WIP + WIP + WIP");

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHealth")
                        .lore(lore)
                        .build()
        );
    }

    private SGButton backButton(OfflinePlayer player, ListOrganizeType listOrganizeType, boolean isTrustedHorse) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&c&l&oBack")
                        .lore("&7Click to open your horse list menu")
                        .build()
        ).withListener((InventoryClickEvent event) -> {
            if(player != event.getWhoClicked()) {
                plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().openToOther((Player) event.getWhoClicked(), player, listOrganizeType, isTrustedHorse);
                return;
            }
            plugin.getMenuManager().getHorseMenuManager().getHorseListMenu().open(player.getPlayer(), listOrganizeType, isTrustedHorse);
        });
    }
}
