package endcrypt.equinox.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadUtils {

    public static ItemStack getItemHead(String id) {
        HeadDatabaseAPI headDatabaseAPI = new HeadDatabaseAPI();
        if(id.isEmpty()) {
            id = "7280";
        }
        return headDatabaseAPI.getItemHead(id);
    }


    public static void placeHeadFromHDB(Location location, String id) {
        HeadDatabaseAPI api = new HeadDatabaseAPI();
        if (id == null || id.isEmpty()) id = "7280";

        ItemStack head = api.getItemHead(id);
        if (head == null || head.getType() != Material.PLAYER_HEAD) return;

        Block block = location.getBlock();
        block.setType(Material.PLAYER_HEAD);

        BlockState state = block.getState();
        if (!(state instanceof Skull skull)) return;

        ItemMeta meta = head.getItemMeta();
        if (meta instanceof SkullMeta skullMeta) {
            PlayerProfile profile = skullMeta.getPlayerProfile();
            if (profile != null) {
                skull.setPlayerProfile(profile);
                skull.update(true, false);
            }
        }
    }
}
