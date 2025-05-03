package endcrypt.equinoxEquestrian.api.events;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EquinePlayerSelectHorseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private Player player;
    private AbstractHorse horse;

    private boolean cancelled;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerSelectHorseEvent(Player player, AbstractHorse horse) {
        this.player = player;
        this.horse = horse;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractHorse getHorse() {
        return horse;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
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
