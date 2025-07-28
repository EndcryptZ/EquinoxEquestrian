package endcrypt.equinox.permissions;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class PermissionManager {

    private final EquinoxEquestrian plugin;
    public PermissionManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        PluginManager pm = plugin.getServer().getPluginManager();

        for (PermissionsEnum permissionEnum : PermissionsEnum.values()) {
            String basePermission = permissionEnum.getPermission();

            // If it's the PLACE_LIMIT permission, register numbered permission nodes (bounty.place.1 to bounty.place.100)
            if (permissionEnum == PermissionsEnum.PERMISSION_HORSE_LIMIT) {
                for (int i = 1; i <= 100; i++) {
                    String numberedPermission = basePermission + "." + i;
                    if (pm.getPermission(numberedPermission) == null) {
                        pm.addPermission(new Permission(numberedPermission));
                    }
                }
            }

            // Register the base permission if not already registered
            if (pm.getPermission(basePermission) == null) {
                pm.addPermission(new Permission(basePermission));
            }
        }
    }

    public int getMaxHorsesAllowed(Player player) {
        String basePermission = PermissionsEnum.PERMISSION_HORSE_LIMIT.getPermission();
        // Check from highest to lowest to ensure highest rank takes priority
        for (int i = 100; i >= 1; i--) {
            String numberedPermission = basePermission + "." + i;
            if (player.hasPermission(numberedPermission)) {
                return i;
            }
        }
        return 0; // default if none is set
    }
}
