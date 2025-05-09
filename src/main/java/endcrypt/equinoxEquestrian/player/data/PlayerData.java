package endcrypt.equinoxEquestrian.player.data;

import org.bukkit.entity.AbstractHorse;

public class PlayerData {

    private AbstractHorse selectedHorse;
    private int tokens;

    public PlayerData(AbstractHorse selectedHorse, int tokens) {
        this.selectedHorse = selectedHorse;
        this.tokens = tokens;
    }

    public AbstractHorse getSelectedHorse() {
        return selectedHorse;
    }

    public void setSelectedHorse(AbstractHorse selectedHorse) {
        this.selectedHorse = selectedHorse;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
}
