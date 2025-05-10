package endcrypt.equinox.token;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.Player;

public class TokenManager {

    private final EquinoxEquestrian plugin;
    public TokenManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public int getTokens(Player player) {
        return plugin.getPlayerDataManager().getPlayerData(player).getTokens();
    }

    public void setTokens(Player player, int amount) {
        plugin.getPlayerDataManager().getPlayerData(player).setTokens(amount);
    }

    public void giveTokens(Player player, int amount) {
        plugin.getPlayerDataManager().getPlayerData(player).setTokens(plugin.getPlayerDataManager().getPlayerData(player).getTokens() + amount);
    }

    public void takeTokens(Player player, int amount) {
        int finalAmount = plugin.getPlayerDataManager().getPlayerData(player).getTokens() - amount;
        if(finalAmount < 0) {
            finalAmount = 0;
        }
        plugin.getPlayerDataManager().getPlayerData(player).setTokens(finalAmount);

    }
}
