package endcrypt.equinoxEquestrian.menu.horse;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.EquineHorse;
import endcrypt.equinoxEquestrian.horse.EquineUtils;
import endcrypt.equinoxEquestrian.utils.HeadUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HorseInfoMenu {

    private final EquinoxEquestrian plugin;
    public HorseInfoMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();
        if(horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou have not selected a horse!"));
            return;
        }
        player.openInventory(createMenu(player, horse));

    }

    private Inventory createMenu(Player player, AbstractHorse horse) {

        SGMenu gui = plugin.getSpiGUI().create(horse.getName() + "'s Info", 4, "Horse Info");


        gui.setButton(10, horseHead(horse));
        gui.setButton(12, horseInformation1(horse));
        gui.setButton(13, horseInformation2(horse));
        gui.setButton(14, horseInformation3(horse));

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
                                "&7▸ &bClaimdate: &7" + EquineUtils.getHorseClaimdate(horse)
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

    private SGButton horseInformation3 (AbstractHorse horse){
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("&fHorse Information 3")
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
}
