package endcrypt.equinox.equine.leveling;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerLungeHorseEvent;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class EquineLevelingListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineLevelingListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExpPickup(PlayerPickupExperienceEvent event) {
        event.getExperienceOrb().setExperience(0);
    }


    // Riding EXP Rewards
    @EventHandler
    public void onPlayerMountHorse(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getMount() instanceof AbstractHorse horse)) return;
        if (!EquineUtils.isLivingEquineHorse(horse)) return;

        new BukkitRunnable() {
            final int secondsRequired = 59;
            int secondsMoving = 0;
            Location lastLocation = horse.getLocation();

            @Override
            public void run() {
                if (!horse.getPassengers().contains(player)) {
                    cancel();
                    return;
                }

                // Do not progress
                if (lastLocation.distanceSquared(horse.getLocation()) < 0.0001) return;

                if (secondsMoving == secondsRequired) {
                    Random random = new Random();
                    int expToGive = random.nextInt(6) + 1;
                    secondsMoving = 0;
                    plugin.getEquineManager().getEquineLeveling().addExp(player, expToGive, true);
                    player.sendMessage(ColorUtils.color(
                            "<prefix><green>Yeehaw! You wrangled <exp> EXP riding your trusty <horse>!",
                            Placeholder.parsed("prefix", plugin.getPrefix().endsWith(" ") ? plugin.getPrefix() : plugin.getPrefix() + " "),
                            Placeholder.parsed("exp", String.valueOf(expToGive)),
                            Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))
                    ));
                    return;
                }

                secondsMoving++;
                lastLocation = horse.getLocation();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onLunge(EquinePlayerLungeHorseEvent event) {
        AbstractHorse horse = event.getHorse();
        Player player = event.getPlayer();
        player.sendMessage(ColorUtils.color(
                "<prefix><green>Your horse <horse> has started lunging! <yellow>Move around while it's lunging to earn EXP!",
                Placeholder.parsed("prefix", plugin.getPrefix().endsWith(" ") ? plugin.getPrefix() : plugin.getPrefix() + " "),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))
        ));

        new BukkitRunnable() {
            final int secondsRequired = 59;
            int secondsLunging = 0;
            Location lastLocation = player.getLocation();

            @Override
            public void run() {
                if (!EquineUtils.isLunging(horse) || !player.isOnline()) {
                    cancel();
                    return;
                }

                // Do not progress
                if (lastLocation.distanceSquared(player.getLocation()) < 0.0001) return;

                if (secondsLunging == secondsRequired) {
                    Random random = new Random();
                    int expToGive = random.nextInt(6) + 1;
                    secondsLunging = 0;
                    plugin.getEquineManager().getEquineLeveling().addExp(player, expToGive, true);
                    player.sendMessage(ColorUtils.color(
                            "<prefix><green>Woohoo! You earned <exp> EXP lunging your awesome <horse>!",
                            Placeholder.parsed("prefix", plugin.getPrefix().endsWith(" ") ? plugin.getPrefix() : plugin.getPrefix() + " "),
                            Placeholder.parsed("exp", String.valueOf(expToGive)),
                            Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))
                    ));
                    return;
                }

                secondsLunging++;
                lastLocation = player.getLocation();
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

}
