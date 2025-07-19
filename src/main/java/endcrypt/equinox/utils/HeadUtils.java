package endcrypt.equinox.utils;

import com.mojang.authlib.GameProfile;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;

public class HeadUtils {

    public static ItemStack getItemHead(String id) {
        HeadDatabaseAPI headDatabaseAPI = new HeadDatabaseAPI();
        if(id.isEmpty()) {
            id = "7280";
        }
        return headDatabaseAPI.getItemHead(id);
    }


    public static void copyHeadTextureToBlock(Block block, ItemStack headItem) {
        // Ensure block is correct type
        block.setType(Material.PLAYER_HEAD);
        if (!(block.getState() instanceof Skull)) return;
        if (!(headItem.getItemMeta() instanceof SkullMeta)) return;

        SkullMeta skullMeta = (SkullMeta) headItem.getItemMeta();
        Skull skull = (Skull) block.getState();

        try {
            // Get GameProfile from SkullMeta
            Field metaProfileField = skullMeta.getClass().getDeclaredField("profile");
            metaProfileField.setAccessible(true);
            GameProfile profile = (GameProfile) metaProfileField.get(skullMeta);

            // Set it into the Skull block
            Field blockProfileField = skull.getClass().getDeclaredField("profile");
            blockProfileField.setAccessible(true);
            blockProfileField.set(skull, profile);

            skull.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
