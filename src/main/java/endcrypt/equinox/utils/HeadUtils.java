package endcrypt.equinox.utils;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;

public class HeadUtils {

    public static ItemStack getItemHead(String id) {
        HeadDatabaseAPI headDatabaseAPI = new HeadDatabaseAPI();
        return headDatabaseAPI.getItemHead(id);
    }
}
