package endcrypt.equinox.equine.breeding;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.EquineUtils;
import endcrypt.equinox.equine.attributes.Gender;
import endcrypt.equinox.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class EquineBreedingTask {

    private final EquinoxEquestrian plugin;
    public EquineBreedingTask(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        start();
    }

    private void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkBreed();
            checkBreeding();
        }, 20L, 20L);
    }

    private void checkBreeding() {
        for (AbstractHorse horse : plugin.getEquineManager().getEquineBreeding().getBreedingHorses()) {
            horse.getWorld().spawnParticle(Particle.HEART, horse.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.05);

            EquineLiveHorse liveHorse = new EquineLiveHorse(horse);

            // Fail breeding if the partner is dead or too far away
            if(!isPartnerValid(liveHorse)) {
                liveHorse.unbreed();
                plugin.getEquineManager().getEquineBreeding().remove(horse);
                continue;
            }

            // Successful breeding
            if(liveHorse.getBreedingStartTime() + TimeUtils.minutesToMillis(10) < System.currentTimeMillis()) {
                if (liveHorse.getGender() == Gender.MARE) {
                    liveHorse.pregnant(getBreedingPartner(liveHorse));
                }
                liveHorse.unbreed();
                plugin.getEquineManager().getEquineBreeding().remove(horse);
            }

        }
    }

    private void checkBreed() {
        for (AbstractHorse abstractMare : plugin.getEquineManager().getEquineCrossTie().getCrosstiedHorses()) {
            if(EquineUtils.getHorseGender(abstractMare) != Gender.MARE) continue;
            EquineLiveHorse mare = new EquineLiveHorse(abstractMare);
            if(!canMareBreed(mare)) continue;

            AbstractHorse abstractStallion = getNearestStallion(abstractMare.getLocation());
            if(abstractStallion == null) continue;
            EquineLiveHorse stallion = new EquineLiveHorse(abstractStallion);
            if(!canStallionBreed(stallion)) continue;

            mare.breed(stallion);
            stallion.breed(mare);

            plugin.getEquineManager().getEquineBreeding().add(abstractMare);
            plugin.getEquineManager().getEquineBreeding().add(abstractStallion);

        }
    }



    private AbstractHorse getNearestStallion(Location location) {
        double minDistance = Double.MAX_VALUE;
        AbstractHorse nearestStallion = null;

        for (Entity entity : location.getNearbyEntities(5, 5, 5)) {
            if (!(entity instanceof AbstractHorse horse)) continue;
            if (EquineUtils.getHorseGender(horse) != Gender.STALLION) continue;

            double distance = location.distance(horse.getLocation());
            if (distance < minDistance) {
                minDistance = distance;
                nearestStallion = horse;
            }
        }

        return nearestStallion;
    }

    private boolean canMareBreed(EquineLiveHorse horse) {
        if(!horse.isInHeat()) return false;
        if(horse.isBreeding()) return false;
        if(horse.getAge() < 6) return false;
        if(horse.getAge() > 12) return false;

        return true;
    }

    private boolean canStallionBreed(EquineLiveHorse horse) {
        if(horse.isBreeding()) return false;
        if(horse.getAge() < 6) return false;
        if(horse.getAge() > 12) return false;

        return true;
    }

    private boolean isPartnerValid(EquineLiveHorse horse) {
        AbstractHorse partner = (AbstractHorse) Bukkit.getEntity(UUID.fromString(horse.getBreedingPartnerUUID()));
        if (partner == null) return false;
        if (partner.isDead()) return false;
        if (partner.getLocation().distance(horse.getHorse().getLocation()) > 5) return false;
        if (!EquineUtils.isBreeding(partner)) return false;
        return true;
    }

    private EquineLiveHorse getBreedingPartner(EquineLiveHorse horse) {
        AbstractHorse partner = (AbstractHorse) Bukkit.getEntity(UUID.fromString(horse.getBreedingPartnerUUID()));
        if (partner == null) return null;
        return new EquineLiveHorse(partner);
    }

}