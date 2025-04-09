package endcrypt.equinoxEquestrian.horse;

import com.samjakob.spigui.item.ItemBuilder;
import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class EquineItems {

    private EquinoxEquestrian plugin;
    public EquineItems(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public static ItemStack getSoftBrush() {
        ItemStack item = new ItemBuilder(Material.BOWL)
                .name("&fSoft Brush")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_SOFT_BRUSH", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getHardBrush() {
        ItemStack item = new ItemBuilder(Material.STRING)
                .name("&fHard Brush")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_HARD_BRUSH", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }
}
