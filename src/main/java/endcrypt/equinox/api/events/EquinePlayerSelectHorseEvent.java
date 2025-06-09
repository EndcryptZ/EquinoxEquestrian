package endcrypt.equinox.api.events;

import lombok.Getter;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class EquinePlayerSelectHorseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final AbstractHorse horse;

    private boolean cancelled;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerSelectHorseEvent(Player player, AbstractHorse horse) {
        this.player = player;
        this.horse = horse;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


}
