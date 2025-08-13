package endcrypt.equinox.api.events;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EquinePlayerLungeHorseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final AbstractHorse horse;

    private boolean cancelled;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerLungeHorseEvent(Player player, AbstractHorse horse) {
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

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
