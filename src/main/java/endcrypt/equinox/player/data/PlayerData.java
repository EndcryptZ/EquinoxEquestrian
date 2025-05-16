package endcrypt.equinox.player.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.AbstractHorse;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class PlayerData {

    private AbstractHorse selectedHorse;
    private int tokens;
    private final List<UUID> ownedHorses;

    public PlayerData(AbstractHorse selectedHorse, int tokens, List<UUID> ownedHorses) {
        this.selectedHorse = selectedHorse;
        this.tokens = tokens;
        this.ownedHorses = ownedHorses;
    }

    public void addOwnedHorse(UUID uuid) {
        ownedHorses.add(uuid);
    }
}
