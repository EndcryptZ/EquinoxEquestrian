package endcrypt.equinox.equine.pregnancy;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorseBuilder;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;

import java.util.ArrayList;
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
        for (AbstractHorse horse : new ArrayList<>(plugin.getEquineManager().getEquinePregnancy().getPregnantHorses())) {
            if(horse == null) continue;
            checkHorsePregnancy(new EquineLiveHorse(horse));
        }
    }

    private void checkHorsePregnancy(EquineLiveHorse mare) {
        long calculatedEndPregnancy = mare.getPregnancyStartTime() + TimeUtils.daysToMillis(3);
        if(mare.isInstantFoal()) {
            giveBirth(mare);
            return;
        }
        if(calculatedEndPregnancy < System.currentTimeMillis()) {
            giveBirth(mare);
        }

    }

    private void giveBirth(EquineLiveHorse mare) {
        EquineHorseBuilder equineHorseBuilder = new EquineHorseBuilder(plugin);

        equineHorseBuilder.spawnFoal(mare, getPregnancyPartner(mare), mare.getHorse().getLocation(), mare.getOwnerUUID());
        mare.setPregnant(false);
        mare.setPregnancyStartTime(0L);
        mare.setInstantFoal(false);
        mare.update();
        plugin.getEquineManager().getEquinePregnancy().remove(mare.getHorse());
    }


    private EquineLiveHorse getPregnancyPartner(EquineLiveHorse mare) {
        return plugin.getDatabaseManager().getDatabaseHorses().getHorse(UUID.fromString(mare.getPregnancyPartnerUUID()));
    }

}
