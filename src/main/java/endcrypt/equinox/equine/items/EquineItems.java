package endcrypt.equinox.equine.items;

import com.samjakob.spigui.item.ItemBuilder;
import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EquineItems {

    private EquinoxEquestrian plugin;
    public EquineItems(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public static ItemStack getSoftBrush() {
        ItemStack item = new ItemBuilder(Material.BOWL)
                .name("&fSoft Brush")
                .lore(
                        "",
                        "&fDurability: 100")
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
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_HARD_BRUSH", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getHoofPick() {
        ItemStack item = new ItemBuilder(Material.CLAY_BALL)
                .name("&fHoof Pick")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_HOOF_PICK", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getFlySpray() {
        ItemStack item = new ItemBuilder(Material.POTION)
                .name("&fFly Spray")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_FLY_SPRAY", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getWashHorse() {
        ItemStack item = new ItemBuilder(Material.WATER_BUCKET)
                .name("&fWash")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_WASH_HORSE", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getConditioner() {
        ItemStack item = new ItemBuilder(Material.DRAGON_BREATH)
                .name("&fConditioner")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_CONDITIONER", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getShampoo() {
        ItemStack item = new ItemBuilder(Material.HONEY_BOTTLE)
                .name("&fShampoo")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_SHAMPOO", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getHoofOil() {
        ItemStack item = new ItemBuilder(Material.GLASS_BOTTLE)
                .name("&fHoof Oil")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_HOOF_OIL", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

    public static ItemStack getSunCream() {
        ItemStack item = new ItemBuilder(Material.AMETHYST_SHARD)
                .name("&fSun Cream")
                .lore(
                        "",
                        "&fDurability: 100")
                .build();

        NBT.modify(item, NBT -> {
            NBT.setString("EQUINE_GROOM_ITEM", "true");
            NBT.setString("EQUINE_SUN_CREAM", "true");
            NBT.setInteger("EQUINE_ITEM_DURABILITY", 100);
        });

        return item;
    }

}
