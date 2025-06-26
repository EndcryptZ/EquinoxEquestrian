package endcrypt.equinox.equine.selector;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerSelectHorseEvent;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.MessageUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class EquineSelector {

    private final EquinoxEquestrian plugin;
    public EquineSelector(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }



    public void selectHorse(Player player, AbstractHorse horse) {
        EquinePlayerSelectHorseEvent equinePlayerSelectHorseEvent = new EquinePlayerSelectHorseEvent(player, horse);
        plugin.getServer().getPluginManager().callEvent(equinePlayerSelectHorseEvent);

        if (equinePlayerSelectHorseEvent.isCancelled()) {
            return;
        }

        if(!EquineUtils.hasPermissionToHorse(player, horse)) {
            player.sendMessage(MessageUtils.cantInteractWithHorse(horse));
            return;
        }

        if(plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse() != null) {

        }

        player.sendMessage(ColorUtils.color("<prefix><green>You have selected <horse>!",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", horse.getName())));
        plugin.getPlayerDataManager().getPlayerData(player).setSelectedHorse(horse);
    }


}
