package endcrypt.equinoxEquestrian.horse.enums;

import endcrypt.equinoxEquestrian.horse.EquineItems;
import org.bukkit.inventory.ItemStack;

public enum Item {



    SOFT_BRUSH(EquineItems.getSoftBrush()),
    HARD_BRUSH(EquineItems.getHardBrush())
    ;

    private final ItemStack item;

    Item(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }
}
