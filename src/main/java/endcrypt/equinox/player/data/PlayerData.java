package endcrypt.equinox.player.data;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static endcrypt.equinox.EquinoxEquestrian.instance;

@Setter
@Getter
public class PlayerData {

    private AbstractHorse selectedHorse;
    private int tokens;
    private List<UUID> ownedHorses; // List of UUIDs of horses owned by the player

    public PlayerData(AbstractHorse selectedHorse, int tokens) {
        this.selectedHorse = selectedHorse;
        this.tokens = tokens;
    }

    @SneakyThrows
    public List<UUID> getOwnedHorses(Player player) {
        return instance.getDatabaseManager().getPlayerHorses(player);
    }
}
