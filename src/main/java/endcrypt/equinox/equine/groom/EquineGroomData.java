package endcrypt.equinox.equine.groom;

import endcrypt.equinox.equine.items.Item;
import org.bukkit.entity.AbstractHorse;

public class EquineGroomData {

    private final Item item;
    private final AbstractHorse horse;
    private int groomTimes;

    public EquineGroomData(Item item, AbstractHorse horse, int groomTimes) {
        this.item = item;
        this.horse = horse;
        this.groomTimes = groomTimes;
    }

    public Item getItem() {
        return item;
    }

    public AbstractHorse getHorse() {
        return horse;
    }

    public int getGroomTimes() {
        return groomTimes;
    }

    public void setGroomTimes(int groomTimes) {
        this.groomTimes = groomTimes;
    }
}
