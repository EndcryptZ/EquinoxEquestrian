package endcrypt.equinox.equine.leveling;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EquineLevelingListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineLevelingListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExperienceItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if (!EquineUtils.isExperienceItem(itemStack)) {
            return;
        }

        if(EquineUtils.isExperienceProcessed(itemStack)) {
            return;
        }

        NBT.modify(itemStack, nbtItem -> {
            nbtItem.setBoolean(Keys.IS_EXPERIENCE_PROCESSED.getKey(), true);
        });

        int exp;
        if (Math.random() < 0.8) { // 80% chance
            exp = new Random().nextInt(2) + 1; // 1-2
        } else {
            exp = new Random().nextInt(3) + 3; // 3-5
        }

        plugin.getEquineManager().getEquineLeveling().addExp(player, exp, true);
    }

}
