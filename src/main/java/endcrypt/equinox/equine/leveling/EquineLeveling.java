package endcrypt.equinox.equine.leveling;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.player.data.PlayerData;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EquineLeveling {

    private final EquinoxEquestrian plugin;
    public EquineLeveling(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        new EquineLevelingTask(plugin);
    }

    public void addExp(Player player, double amount, boolean notify) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        double totalExp = playerData.getExp() + amount;
        playerData.setExp(totalExp);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);

        if(notify) {
            Location eyeLocation = player.getEyeLocation();
            Vector direction = eyeLocation.getDirection().normalize();
            Location holoLocation = eyeLocation.add(direction.multiply(1)); // 1 block in front

            plugin.getHologramManager().createFlyoutHolo("<green>+" + amount + " EXP!", holoLocation);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }

        processLevelUp(player);
        syncExpBar(player);
    }

    public void setExp(Player player, double amount) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setExp(amount);
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);

        processLevelUp(player);
        syncExpBar(player);
    }

    public void setLevel(Player player, int level) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        playerData.setLevel(level);
        playerData.setExp(getTotalExpToReachLevel(level));
        plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);
        syncExpBar(player);
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

    public   void syncExpBar(Player player) {
        PlayerData playerData = plugin.getPlayerDataManager().getPlayerData(player);
        double totalExp = playerData.getExp();

        // recalculate level based on total exp
        int calculatedLevel = 1;
        for (int i = 2; i <= 150; i++) {
            double requiredExp = getTotalExpToReachLevel(i);
            if (totalExp < requiredExp) {
                break;
            }
            calculatedLevel = i;
        }

        // update player data level if changed
        if (playerData.getLevel() != calculatedLevel) {
            playerData.setLevel(calculatedLevel);
            plugin.getPlayerDataManager().getPlayerDataMap().put(player, playerData);
        }

        double currentLevelExp = getTotalExpToReachLevel(calculatedLevel);
        double nextLevelExp = getTotalExpToReachLevel(calculatedLevel + 1);

        double progress = 0.0;
        if (nextLevelExp > currentLevelExp) {
            progress = (totalExp - currentLevelExp) / (nextLevelExp - currentLevelExp);
        }

        if (progress < 0.0) progress = 0.0;
        if (progress > 1.0) progress = 1.0;

        player.setLevel(calculatedLevel);
        player.setExp((float) progress);
    }
}
