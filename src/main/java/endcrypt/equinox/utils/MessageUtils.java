package endcrypt.equinox.utils;

import endcrypt.equinox.equine.EquineLiveHorse;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.AbstractHorse;
import org.jetbrains.annotations.NotNull;

import static endcrypt.equinox.EquinoxEquestrian.instance;

public class MessageUtils {

    public static @NotNull Component cantInteractWithHorse(AbstractHorse horse) {
        EquineLiveHorse equineLiveHorse = new EquineLiveHorse(horse);
        return ColorUtils.color("<prefix><red>You cannot interact with <owner> horse!",
                Placeholder.parsed("prefix", instance.getPrefix()),
                Placeholder.parsed("owner", equineLiveHorse.getOwnerName()));
    }
}
