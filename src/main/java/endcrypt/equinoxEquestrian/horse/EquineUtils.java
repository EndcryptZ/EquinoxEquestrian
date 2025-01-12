package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.entity.AbstractHorse;

public class EquineUtils {

    public static boolean isLivingEquineHorse(AbstractHorse entity) {
        String isEquine = NBT.getPersistentData(entity, nbt -> nbt.getString("EQUINE_HORSE"));
        if (isEquine.equalsIgnoreCase("true")) {
            return true;
        }

        return false;
    }

    public static double getBaseSpeed(AbstractHorse entity) {
        return NBT.getPersistentData(entity, nbt -> (double) nbt.getDouble("EQUINE_BASE_SPEED"));
    }
}
