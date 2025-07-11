package endcrypt.equinox.equine.groom;

import endcrypt.equinox.equine.items.Item;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.AbstractHorse;

@Getter
@Setter
public class EquineGroomData {

    private final Item item;
    private final AbstractHorse horse;
    private int groomTimes;

    public EquineGroomData(Item item, AbstractHorse horse, int groomTimes) {
        this.item = item;
        this.horse = horse;
        this.groomTimes = groomTimes;
    }

}
