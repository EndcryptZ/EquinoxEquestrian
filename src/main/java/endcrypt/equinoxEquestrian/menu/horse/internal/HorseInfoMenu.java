package endcrypt.equinoxEquestrian.menu.horse.internal;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.EquineUtils;
import endcrypt.equinoxEquestrian.utils.HeadUtils;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HorseInfoMenu {

    private final EquinoxEquestrian plugin;
    public HorseInfoMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, AbstractHorse horse, ListOrganizeType listOrganizeType) {
        player.openInventory(createMenu(player, horse, listOrganizeType));

    }

    private Inventory createMenu(Player player, AbstractHorse horse, ListOrganizeType listOrganizeType) {

        SGMenu gui = plugin.getSpiGUI().create(horse.getName() + "'s Info", 4, "Horse Info");


        gui.setButton(10, horseHead(horse));
        gui.setButton(12, horseInformation1(horse));
        gui.setButton(13, horseInformation2(horse));
        gui.setButton(14, motionInformation(horse));
        gui.setButton(15, welfareInformation(horse));
        gui.setButton(16, horseInformation3(horse));
        gui.setButton(17, healthInformation(horse));

        gui.setButton(31, backButton(player, listOrganizeType));

        return gui.getInventory();
    }

    private SGButton horseHead(AbstractHorse horse) {
        return new SGButton(
                new ItemBuilder(HeadUtils.getItemHead("3919"))
                        .name("&f" + horse.getName())
                        .lore("&7▸ ?")
                        .build()
        );
    }

    private SGButton horseInformation1(AbstractHorse horse) {
        return new SGButton(
                new ItemBuilder(Material.FILLED_MAP)
                        .name("&fHorse Information")
                        .lore(
                                "&7▸ &bOwner: &7" + horse.getOwner().getName(),
                                "&7▸ &bRegistered: &7WIP",
                                "&7▸ &bID: &7WIP",
                                "&7▸ &bClaim Date: &7" + EquineUtils.getHorseClaimDate(horse)
                                )
                        .build()
        );
    }

    private SGButton horseInformation2(AbstractHorse horse) {
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHorse Information 2")
                        .lore(
                                "&7▸ &bName: &7" + horse.getName(),
                                "&7▸ &bBarn-Name: &7WIP",
                                "&7▸ &bBreed: &7" + equineHorse.getBreed().getName(),
                                "&7▸ &bAge: &7" + equineHorse.getAge(),
                                "&7▸ &bGender: &7" + equineHorse.getGender().getGenderName(),
                                "&7▸ &bCoat Colour: &7" + equineHorse.getCoatColor().getCoatColorName(),
                                "&7▸ &bCoat Modifier: &7" + equineHorse.getCoatModifier().getCoatModifierName(),
                                "&7▸ &bHeight: &7" + equineHorse.getHeight().getHandsString()


                        )
                        .build()
        );
    }

    private SGButton motionInformation(AbstractHorse horse){
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fMotion")
                        .lore(
                                "&7▸ &bSpeed: &7WIP",
                                "&7▸ &bJump: &7WIP",
                                "&7▸ &bStamina: &7WIP",
                                "&7▸ &bLevel: &7WIP",
                                "&7▸ &bDiscipline: &7" + equineHorse.getDiscipline().getDisciplineName(),
                                "&7▸ &bTraits:",
                                "    &7▸ &bTrait 1: &7" + equineHorse.getTraits()[0].getTraitName(),
                                "    &7▸ &bTrait 2: &7" + equineHorse.getTraits()[1].getTraitName(),
                                "    &7▸ &bTrait 3: &7" + equineHorse.getTraits()[2].getTraitName()


                        )
                        .build()
        );
    }

    private SGButton welfareInformation(AbstractHorse horse) {
        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fWelfare")
                        .lore(
                                "&7▸ &bHappiness: &7WIP",
                                "&7▸ &bRelationship: &7WIP",
                                "&7▸ &bNutrition:",
                                "&7▸ &b+ Hunger: &7WIP",
                                "&7▸ &b+ Thirst: &7WIP",
                                "&7▸ &b+ Weight: &7WIP",
                                "&7▸ &bCleanliness: &7WIP"
                        )
                        .build()
        );
    }

    private SGButton horseInformation3(AbstractHorse horse) {
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

    private SGButton healthInformation(AbstractHorse horse) {
        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHealth")
                        .lore(
                                "&bVaccines:",
                                "&7+ WIP",
                                "&7+ WIP + WIP + WIP",
                                "&bIllnesses:",
                                "&7+ WIP",
                                "&7+ WIP + WIP + WIP",
                                "&bInjury:",
                                "&7+ WIP",
                                "&7+ WIP + WIP + WIP"
                        )
                        .build()
        );
    }

    private SGButton backButton(Player player, ListOrganizeType listOrganizeType) {
        return new SGButton(
                new ItemBuilder(Material.RED_CANDLE)
                        .name("&c&l&oBack")
                        .lore("&7Click to open your horse list menu")
                        .build()
        ).withListener(event -> plugin.getHorseMenuManager().getHorseListMenu().open(player, listOrganizeType));
    }
}
