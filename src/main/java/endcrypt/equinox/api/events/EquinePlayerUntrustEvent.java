package endcrypt.equinox.api.events;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class EquinePlayerUntrustEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final AbstractHorse horse;
    private final Player player;
    private final OfflinePlayer trustedPlayer;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerUntrustEvent(AbstractHorse horse,
                                    Player player,
                                    OfflinePlayer trustedPlayer) {
        this.horse = horse;
        this.player = player;
        this.trustedPlayer = trustedPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}

