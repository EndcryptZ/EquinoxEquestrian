package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EquineUtils {

    public static boolean isLivingEquineHorse(AbstractHorse horse) {
        if (horse == null) return false;
        return Keys.readPersistentData(horse, Keys.IS_EQUINE);
    }

    public static double getBaseSpeed(AbstractHorse horse) {
        return Keys.readPersistentData(horse, Keys.BASE_SPEED);
    }

    public static double getBaseJumpPower(AbstractHorse horse) {
        return Keys.readPersistentData(horse, Keys.BASE_JUMP);
    }

    public static boolean isCrossTied(AbstractHorse horse) {
        if (horse == null) return false;
        return Keys.readPersistentData(horse, Keys.IS_CROSS_TIED);
    }

    public static boolean isLunging(AbstractHorse horse) {
        if (horse == null) return false;
        return Keys.readPersistentData(horse, Keys.IS_LUNGING);
    }

    public static List<Entity> getLeashedEntities(Player player) {
        if (player == null) return new ArrayList<>();

        return player.getNearbyEntities(20, 20, 20).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .filter(livingEntity -> livingEntity.isLeashed() && livingEntity.getLeashHolder() == player)
                .collect(Collectors.toList());
    }

    public static boolean isGroomItem(ItemStack item) {
        if (item == null) return false;
        return "true".equalsIgnoreCase(NBT.get(item, nbt -> (String) nbt.getString(Keys.IS_GROOM_ITEM.getKey())));
    }

    public static String getHorseClaimDate(EquineLiveHorse horse) {
        if (horse == null) return "Unknown";

        long claimEpoch = horse.getClaimTime();
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(claimEpoch));
    }

    public static EquineHorse fromAbstractHorse(AbstractHorse horse) {
        if (horse == null) return null;

        return NBT.getPersistentData(horse, nbt -> {
            // Basic attributes
            String name = horse.getName();
            int age = nbt.getInteger(Keys.AGE.getKey());
            double heightHands = nbt.getDouble(Keys.HEIGHT.getKey());
            Discipline discipline = Discipline.getDisciplineByName(nbt.getString(Keys.DISCIPLINE.getKey()));
            Gender gender = Gender.getGenderByName(nbt.getString(Keys.GENDER.getKey()));

            // Handle breeds
            List<Breed> breeds = new ArrayList<>();
            Breed breed1 = Breed.getBreedByName(nbt.getString(Keys.BREED_PREFIX.getKey() + "0"));
            Breed breed2 = Breed.getBreedByName(nbt.getString(Keys.BREED_PREFIX.getKey() + "1"));

            breeds.add(breed1);
            if (breed2 != null && breed2 != Breed.NONE) {
                breeds.add(breed2);
            }

            // Get prominent breed
            String prominentBreedName = nbt.getString(Keys.PROMINENT_BREED.getKey());
            Breed prominentBreed = (prominentBreedName != null && !prominentBreedName.isEmpty())
                    ? Breed.getBreedByName(prominentBreedName)
                    : null;

            // Handle coat properties
            CoatColor coatColor = CoatColor.NONE;
            CoatModifier coatModifier = CoatModifier.NONE;
            if (horse instanceof Horse h) {
                coatColor = CoatColor.getCoatColorFromHorseColor(h.getColor());
                coatModifier = CoatModifier.getCoatModifierFromHorseStyle(h.getStyle());
            }

            // Handle traits
            List<Trait> traitList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String traitName = nbt.getString(Keys.TRAIT_PREFIX.getKey() + i);
                if (traitName != null && !traitName.isEmpty() && !traitName.equalsIgnoreCase("NONE")) {
                    traitList.add(Trait.getTraitByName(traitName));
                }
            }

            // Create and return horse
            EquineHorse equineHorse = new EquineHorse(
                    name,
                    discipline,
                    breeds,
                    coatColor,
                    coatModifier,
                    gender,
                    age,
                    Height.getByHands(heightHands),
                    traitList.isEmpty() ? null : traitList
            );

            equineHorse.setProminentBreed(prominentBreed);
            equineHorse.setUuid(horse.getUniqueId());

            return equineHorse;
        });
    }

    public static double blocksToMnecraftSpeed(double blocksPerSecond) {
        return blocksPerSecond * 0.02777777777777778;
    }

}