package endcrypt.equinox.equine.pregnancy;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.util.UUID;

public class EquinePregnancyTask {

    private final EquinoxEquestrian plugin;
    public EquinePregnancyTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }


    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::checkPregnancy, 20L, 20L);
    }


    private void checkPregnancy() {
        for (AbstractHorse horse : plugin.getEquineManager().getEquinePregnancy().getPregnantHorses()) {
            checkHorsePregnancy(new EquineLiveHorse(horse));
        }
    }

    private void checkHorsePregnancy(EquineLiveHorse mare) {
        long calculatedEndPregnancy = mare.getPregnancyStartTime() + TimeUtils.daysToMillis(3);
        if(calculatedEndPregnancy < System.currentTimeMillis()) {
            EquineHorseBuilder equineHorseBuilder = new EquineHorseBuilder(plugin);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(mare.getHorse().getUniqueId());

            equineHorseBuilder.spawnFoal(mare, getPregnancyPartner(mare), mare.getHorse().getLocation(), owner);
            mare.setPregnant(false);
            mare.setPregnancyStartTime(0L);
            mare.update();
            plugin.getEquineManager().getEquinePregnancy().remove(mare.getHorse());
        }
    }


    private EquineLiveHorse getPregnancyPartner(EquineLiveHorse mare) {
        return plugin.getDatabaseManager().getDatabaseHorses().getHorse(UUID.fromString(mare.getBreedingPartnerUUID()));
    }

}
