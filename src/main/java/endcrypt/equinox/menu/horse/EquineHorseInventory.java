package endcrypt.equinox.menu.horse;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EquineHorseInventory implements InventoryHolder {

    private final Inventory inventory;
    private final AbstractHorse horse;

    // Constructor to create a custom inventory for the horse
    public EquineHorseInventory(AbstractHorse horse) {
        this.horse = horse;
        // Create a chest inventory (27 slots) with the horse's name as the title
        this.inventory = Bukkit.createInventory(this, 27, horse.getCustomName() + "'s Inventory");
    }

    // Return the associated inventory
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    // Get the associated horse
    public AbstractHorse getHorse() {
        return this.horse;
    }
}
