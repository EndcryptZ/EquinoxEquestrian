package endcrypt.equinox.equine.waste;

import com.samjakob.spigui.item.ItemBuilder;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.utils.HeadUtils;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.inventory.ItemStack;

public class EquineWasteTask {

    private final EquinoxEquestrian plugin;
    public EquineWasteTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            checkPoop();
            checkPee();
        }, 20L, 100L);
    }

    private void checkPoop() {
        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if(!EquineUtils.isLivingEquineHorse(horse)) continue;
                EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
                if (liveHorse.getLastPoop() + TimeUtils.hoursToMillis(2) < System.currentTimeMillis()) {
                    handlePoop(liveHorse);
                }
            }
        }
    }

    private void checkPee() {
        for (World world : plugin.getServer().getWorlds()) {
            for (AbstractHorse horse : world.getEntitiesByClass(AbstractHorse.class)) {
                if(!EquineUtils.isLivingEquineHorse(horse)) continue;
                EquineLiveHorse liveHorse = new EquineLiveHorse(horse);
                if (liveHorse.getLastPee() + TimeUtils.hoursToMillis(2) < System.currentTimeMillis()) {
                    handlePee(liveHorse);
                }
            }
        }
    }

    private void handlePoop(EquineLiveHorse liveHorse) {
        AbstractHorse horse = liveHorse.getHorse();
        liveHorse.setLastPoop(System.currentTimeMillis());
        liveHorse.update();
        horse.getWorld().dropItem(horse.getLocation(), poopItem());
    }

    private void handlePee(EquineLiveHorse liveHorse) {
        AbstractHorse horse = liveHorse.getHorse();
        liveHorse.setLastPee(System.currentTimeMillis());
        liveHorse.update();
        horse.getWorld().dropItem(horse.getLocation(), peeItem());
    }

    private ItemStack poopItem() {
        return new ItemBuilder(HeadUtils.getItemHead("1682"))
                .name("&6Poop")
                .build();
    }

    private ItemStack peeItem() {
        return new ItemBuilder(Material.YELLOW_CARPET)
                .name("&6Pee")
                .build();
    }
}
