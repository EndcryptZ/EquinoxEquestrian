package endcrypt.equinoxEquestrian.api.events;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EquineCrossTieLeashEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private AbstractHorse horse;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquineCrossTieLeashEvent(AbstractHorse horse) {
        this.horse = horse;
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
}
