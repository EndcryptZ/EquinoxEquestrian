package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.player.data.PlayerData;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class EquineLeveling {

    private final EquinoxEquestrian plugin;
    public EquineLeveling(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void addExp(Player player, double amount, boolean notify) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        double totalExp = playerData.getExp() + amount;
        playerData.setExp(totalExp);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);

        if(notify) {
            Location location = player.getLocation().add(0, 1, 0);
            player.sendMessage(ColorUtils.color("<prefix><green>You gained <exp> exp!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("exp", String.valueOf(amount))));
            // DHAPI.createHologram("Test", location);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        processLevelUp(player);
    }

    public void setExp(Player player, double amount) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setExp(amount);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);

        processLevelUp(player);
    }

    public void setLevel(Player player, int level) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setLevel(level);
        playerData.setExp(getTotalExpToReachLevel(level));
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);
    }

    public void processLevelUp(Player player) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        double totalExp = playerData.getExp();

        int newLevel = 1;
        for (int i = 2; i <= 150; i++) {
            double requiredExp = getTotalExpToReachLevel(i);
            if (totalExp < requiredExp) {
                break;
            }
            newLevel = i;
        }

        int currentLevel = playerData.getLevel();
        if (newLevel > currentLevel) {
            playerData.setLevel(newLevel);
            plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);

            player.sendMessage(ColorUtils.color("<prefix><green>You reached level <level>!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("level", String.valueOf(newLevel))));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }
    }

    /**
     * Calculates the total EXP required to reach the given level.
     * EXP does NOT reset between levels.
     */
    public double getTotalExpToReachLevel(int level) {
        if (level <= 1) return 0.0;

        double base = 120.0;
        double tierMultiplier = 1.20;
        double levelMultiplier = 1.08;

        double totalExp = 0.0;

        for (int currentLevel = 2; currentLevel <= level; currentLevel++) {
            int tier = (currentLevel - 1) / 10;
            double tierBase = base * Math.pow(tierMultiplier, tier);
            double expForLevel = tierBase * Math.pow(levelMultiplier, (currentLevel - 1) % 10);
            totalExp += expForLevel;
        }

        return totalExp;
    }


}
