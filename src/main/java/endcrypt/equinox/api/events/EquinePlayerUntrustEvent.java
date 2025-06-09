package endcrypt.equinox.api.events;

import lombok.Getter;
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
    private final Player trustedPlayer;

    // Constructor: Include the horse and the side of the leash (left or right)
    public EquinePlayerUntrustEvent(AbstractHorse horse,
                                    Player player,
                                    Player trustedPlayer) {
        this.horse = horse;
        this.player = player;
        this.trustedPlayer = trustedPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}

