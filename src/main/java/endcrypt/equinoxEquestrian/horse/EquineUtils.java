package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EquineUtils {

    public static boolean isLivingEquineHorse(AbstractHorse horse) {
        String isEquine = NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_HORSE"));
        return isEquine.equalsIgnoreCase("true");
    }

    public static double getBaseSpeed(AbstractHorse horse) {
        return NBT.getPersistentData(horse, nbt -> nbt.getDouble("EQUINE_BASE_SPEED"));
    }

    public static double getBaseJumpPower(AbstractHorse horse) {
        return NBT.getPersistentData(horse, nbt -> nbt.getDouble("EQUINE_BASE_JUMP_POWER"));
    }

    public static boolean isCrossTied(AbstractHorse horse) {
        String isCrossTied = NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_IS_CROSS_TIED"));
        return isCrossTied.equalsIgnoreCase("true");
    }


    public static boolean isLunging(AbstractHorse horse) {
        String isLunging = NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_IS_LUNGING"));
        return isLunging.equalsIgnoreCase("true");
    }


    public static List<Entity> getLeashedEntities(Player player) {
        List<Entity> leashedEntities = new ArrayList<>();

        // Iterate through all entities in the world
        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
            // Check if the entity is leashed and its leash holder is the player
            if(!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }
            if (livingEntity.isLeashed() && livingEntity.getLeashHolder() == player) {
                leashedEntities.add(livingEntity);
            }
        }

        return leashedEntities;
    }


    public static boolean isGroomItem(ItemStack item) {
        String isGroomItem = NBT.get(item, nbt -> (String) nbt.getString("EQUINE_GROOM_ITEM"));
        return isGroomItem.equalsIgnoreCase("true");
    }
}
