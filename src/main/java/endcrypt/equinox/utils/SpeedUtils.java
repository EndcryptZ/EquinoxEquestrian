package endcrypt.equinox.utils;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.Discipline;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.entity.AbstractHorse;

import java.util.EnumSet;
import java.util.Random;

public class SpeedUtils {

    private static final EquinoxEquestrian plugin = EquinoxEquestrian.instance;

    public static double blocksToMnecraftSpeed(double blocksPerSecond) {
        return blocksPerSecond * 0.02777777777777778;
    }
    public static double minecraftSpeedToBlocks(double minecraftSpeed) {
        return EquineUtils.roundToTwoDecimals(minecraftSpeed / 0.02777777777777778);
    }

    public static void applySpeedsToHorse(AbstractHorse horse) {
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);
        double walkSpeed = generateSpeed(0.4, 1.2, 80, 1.2, 2.7, 55);
        double trotSpeed = generateSpeed(3.4, 4.4, 80, 4.4, 5.7, 55);
        double canterSpeed = generateSpeed(7.3, 8.6, 80, 8.6, 9.5, 55);
        double gallopSpeed = generateSpeed(11.4, 12.8, 80, 12.8, 13.7, 55);

        // override speeds if racehorse
        if (EnumSet.of(Discipline.FLAT_RACING_SHORT, Discipline.FLAT_RACING_LONG)
                .contains(equineHorse.getDiscipline())) {
            canterSpeed = RandomUtils.randomInRange(7.6, 10.1);  // 13–15 mph
            gallopSpeed = RandomUtils.randomInRange(12.5, 15.0); // 25–45 mph
        }

        // apply converted speeds to NBT
        Keys.writeDouble(horse, Keys.WALK_SPEED, blocksToMnecraftSpeed(walkSpeed));
        Keys.writeDouble(horse, Keys.TROT_SPEED, blocksToMnecraftSpeed(trotSpeed));
        Keys.writeDouble(horse, Keys.CANTER_SPEED, blocksToMnecraftSpeed(canterSpeed));
        Keys.writeDouble(horse, Keys.GALLOP_SPEED, blocksToMnecraftSpeed(gallopSpeed));
    }

    private static double generateSpeed(double min1, double max1, int chance1,
                                        double min2, double max2, int chance2) {
        Random random = new Random();
        int roll = random.nextInt(100) + 1;
        double value;

        if (roll <= chance1) {
            value = RandomUtils.randomInRange(min1, max1);
        } else if (roll <= chance1 + chance2) {
            value = RandomUtils.randomInRange(min2, max2);
        } else {
            value = RandomUtils.randomInRange(min1, max1);
        }
        return Math.round(value * 100.0) / 100.0;
    }
}
