package endcrypt.equinoxEquestrian.horse.enums;

import endcrypt.equinoxEquestrian.horse.EquineItems;
import org.bukkit.inventory.ItemStack;

public enum Item {



    HARD_BRUSH(EquineItems.getHardBrush(), "Hard Brush", "EQUINE_HARD_BRUSH"),
    SOFT_BRUSH(EquineItems.getSoftBrush(), "Soft Brush", "EQUINE_SOFT_BRUSH"),
    HOOF_PICK(EquineItems.getHoofPick(), "Hoof Pick", "EQUINE_HOOF_PICK"),
    FLY_SPRAY(EquineItems.getFlySpray(), "Fly Spray", "EQUINE_FLY_SPRAY"),
    WASH_HORSE(EquineItems.getWashHorse(), "Wash Horse", "EQUINE_WASH_HORSE"),
    CONDITIONER(EquineItems.getConditioner(), "Conditioner", "EQUINE_CONDITIONER"),
    SHAMPOO(EquineItems.getShampoo(), "Shampoo", "EQUINE_SHAMPOO"),
    HOOF_OIL(EquineItems.getHoofOil(), "Hoof Oil", "EQUINE_HOOF_OIL"),
    SUN_CREAM(EquineItems.getSunCream(), "Sun Cream", "EQUINE_SUN_CREAM")
    ;

    private final ItemStack item;
    private final String name;
    private final String nbt;

    Item(ItemStack item, String name, String nbt) {
        this.item = item;
        this.name = name;
        this.nbt = nbt;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public String getNbt() {
        return nbt;
    }
}
