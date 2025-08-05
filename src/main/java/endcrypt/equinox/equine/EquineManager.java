package endcrypt.equinox.equine;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.aging.EquineAging;
import endcrypt.equinox.equine.breeding.EquineBreeding;
import endcrypt.equinox.equine.crosstie.EquineCrossTie;
import endcrypt.equinox.equine.gaits.EquineGaits;
import endcrypt.equinox.equine.groom.EquineGroomManager;
import endcrypt.equinox.equine.home.EquineHome;
import endcrypt.equinox.equine.invulnerable.EquineInvulnerable;
import endcrypt.equinox.equine.items.EquineItems;
import endcrypt.equinox.equine.leveling.EquineLeveling;
import endcrypt.equinox.equine.lunge.EquineLunge;
import endcrypt.equinox.equine.pregnancy.EquinePregnancy;
import endcrypt.equinox.equine.privacy.EquinePrivacy;
import endcrypt.equinox.equine.selector.EquineSelector;
import endcrypt.equinox.equine.waste.EquineWaste;
import lombok.Getter;

@Getter
public class EquineManager {

    private final EquineHorseBuilder equineHorseBuilder;
    private final EquineGaits equineGaits;
    private final EquineCrossTie equineCrossTie;
    private final EquineLunge equineLunge;
    private final EquineHome equineHome;
    private final EquineGroomManager equineGroomManager;
    private final EquineItems equineItems;
    private final EquineInvulnerable equineInvulnerable;
    private final EquineSelector equineSelector;
    private final EquineTeleport equineTeleport;
    private final EquinePrivacy equinePrivacy;
    private final EquineBreeding equineBreeding;
    private final EquinePregnancy equinePregnancy;
    private final EquineWaste equineWaste;
    private final EquineLeveling equineLeveling;
    private final EquineAging equineAging;

    private final EquinoxEquestrian plugin;
    public EquineManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        equineHorseBuilder = new EquineHorseBuilder(plugin);
        equineGaits = new EquineGaits(plugin);
        equineCrossTie = new EquineCrossTie(plugin);
        equineLunge = new EquineLunge(plugin);
        equineHome = new EquineHome(plugin);
        equineGroomManager = new EquineGroomManager(plugin);
        equineItems = new EquineItems(plugin);
        equineInvulnerable = new EquineInvulnerable(plugin);
        equineSelector = new EquineSelector(plugin);
        equineTeleport = new EquineTeleport(plugin);
        equinePrivacy = new EquinePrivacy();
        equineBreeding = new EquineBreeding(plugin);
        equinePregnancy = new EquinePregnancy(plugin);
        equineWaste = new EquineWaste(plugin);
        equineLeveling = new EquineLeveling(plugin);
        equineAging = new EquineAging(plugin);


    }

}
