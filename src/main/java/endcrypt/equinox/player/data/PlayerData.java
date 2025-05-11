package endcrypt.equinox.player.data;

import org.bukkit.entity.AbstractHorse;

import java.util.List;
import java.util.UUID;

public class PlayerData {

    private AbstractHorse selectedHorse;
    private int tokens;
    private List<UUID> ownedHorses;

    public PlayerData(AbstractHorse selectedHorse, int tokens, List<UUID> ownedHorses) {
        this.selectedHorse = selectedHorse;
        this.tokens = tokens;
        this.ownedHorses = ownedHorses;
    }


    public AbstractHorse getSelectedHorse() {
        return selectedHorse;
    }


    public int getTokens() {
        return tokens;
    }

    public List<UUID> getOwnedHorses() {
        return ownedHorses;
    }

    public void setSelectedHorse(AbstractHorse selectedHorse) {
        this.selectedHorse = selectedHorse;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void addOwnedHorse(UUID uuid) {
        ownedHorses.add(uuid);
    }
}
