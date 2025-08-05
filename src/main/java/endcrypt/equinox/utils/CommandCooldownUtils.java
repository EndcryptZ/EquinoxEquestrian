package endcrypt.equinox.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandCooldownUtils {

    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    /**
     * Adds a cooldown for a specific command and player.
     *
     * @param player The player to add the cooldown for.
     * @param command The command string identifier.
     * @param cooldownMillis Cooldown duration in milliseconds.
     */
    public static void addCooldown(Player player, String command, long cooldownMillis) {
        UUID uuid = player.getUniqueId();
        cooldowns.putIfAbsent(uuid, new HashMap<>());
        cooldowns.get(uuid).put(command, System.currentTimeMillis() + cooldownMillis);
    }

    /**
     * Checks if the given command is on cooldown for the player.
     *
     * @param player The player to check.
     * @param command The command to check.
     * @return True if the command is still on cooldown, false otherwise.
     */
    public static boolean isOnCooldown(Player player, String command) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) return false;

        Map<String, Long> playerCooldowns = cooldowns.get(uuid);
        if (!playerCooldowns.containsKey(command)) return false;

        long expireTime = playerCooldowns.get(command);
        if (System.currentTimeMillis() >= expireTime) {
            // Cooldown expired
            playerCooldowns.remove(command);
            if (playerCooldowns.isEmpty()) cooldowns.remove(uuid);
            return false;
        }

        return true;
    }

    /**
     * Gets the remaining cooldown time in milliseconds.
     *
     * @param player The player to check.
     * @param command The command to check.
     * @return Remaining time in milliseconds. Returns 0 if not on cooldown.
     */
    public static long getRemaining(Player player, String command) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) return 0;

        Long expireTime = cooldowns.get(uuid).get(command);
        if (expireTime == null || System.currentTimeMillis() >= expireTime) return 0;

        return expireTime - System.currentTimeMillis();
    }
}
