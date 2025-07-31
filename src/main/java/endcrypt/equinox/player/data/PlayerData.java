package endcrypt.equinox.player.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.AbstractHorse;

@Setter
@Getter
public class PlayerData {

    private AbstractHorse selectedHorse;
    private int tokens;
    private int level;
    private double exp;
    private int manure;

    public PlayerData(AbstractHorse selectedHorse, int tokens) {
        this.selectedHorse = selectedHorse;
        this.tokens = tokens;
        this.level = 1;
        this.exp = 0;
    }

}
