package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.horse.enums.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public static String getHorseClaimdate(AbstractHorse horse) {
        long claimEpoch = NBT.getPersistentData(horse, nbt -> nbt.getLong("EQUINE_CLAIM_TIME"));

        Date date = new Date(claimEpoch);

        // Format date to "DD MMM YYYY"
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        return formatter.format(date);
    }

    public static EquineHorse fromAbstractHorse(AbstractHorse horse) {
        return NBT.getPersistentData(horse, nbt -> {
            String name = horse.getCustomName();
            int age = nbt.getInteger("EQUINE_AGE");
            double heightHands = nbt.getDouble("EQUINE_HEIGHT");

            Discipline discipline = Discipline.valueOf(nbt.getString("EQUINE_DISCIPLINE"));
            Breed breed = Breed.valueOf(nbt.getString("EQUINE_BREED"));
            Gender gender = Gender.valueOf(nbt.getString("EQUINE_GENDER"));

            // Extract coat color and modifier from Horse
            CoatColor coatColor = CoatColor.NONE;
            CoatModifier coatModifier = CoatModifier.NONE;

            if (horse instanceof Horse h) {
                coatColor = CoatColor.getCoatColorFromHorseColor(h.getColor());
                coatModifier = CoatModifier.getCoatModifierFromHorseStyle(h.getStyle());
            }

            // Traits
            List<Trait> traits = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                traits.add(Trait.valueOf(nbt.getString("EQUINE_TRAIT_" + i)));
            }

            EquineHorse equineHorse = new EquineHorse(
                    name,
                    discipline,
                    breed,
                    coatColor,
                    coatModifier,
                    gender,
                    age,
                    Height.getByHands(heightHands),
                    traits.toArray(new Trait[0])
            );


            return equineHorse;
        });
    }



}
