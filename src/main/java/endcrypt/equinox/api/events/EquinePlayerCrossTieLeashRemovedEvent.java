package endcrypt.equinox.api.events;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EquinePlayerCrossTieLeashRemovedEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final AbstractHorse horse;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerCrossTieLeashRemovedEvent(AbstractHorse horse) {
        this.horse = horse;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
