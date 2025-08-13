package endcrypt.equinox.api.events;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EquinePlayerCrossTieLeashEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final AbstractHorse horse;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerCrossTieLeashEvent(Player player, AbstractHorse horse) {
        this.player = player;
        this.horse = horse;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
