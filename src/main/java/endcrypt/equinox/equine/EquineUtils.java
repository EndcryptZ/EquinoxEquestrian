package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;
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
        return "true".equalsIgnoreCase(NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_HORSE")));
    }

    public static double getBaseSpeed(AbstractHorse horse) {
        return horse == null ? 0 : NBT.getPersistentData(horse, nbt -> nbt.getDouble("EQUINE_BASE_SPEED"));
    }

    public static double getBaseJumpPower(AbstractHorse horse) {
        return horse == null ? 0 : NBT.getPersistentData(horse, nbt -> nbt.getDouble("EQUINE_BASE_JUMP_POWER"));
    }

    public static boolean isCrossTied(AbstractHorse horse) {
        if (horse == null) return false;
        return "true".equalsIgnoreCase(NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_IS_CROSS_TIED")));
    }

    public static boolean isLunging(AbstractHorse horse) {
        if (horse == null) return false;
        return "true".equalsIgnoreCase(NBT.getPersistentData(horse, nbt -> nbt.getString("EQUINE_IS_LUNGING")));
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
        return "true".equalsIgnoreCase(NBT.get(item, nbt -> (String) nbt.getString("EQUINE_GROOM_ITEM")));
    }

    public static String getHorseClaimDate(AbstractHorse horse) {
        if (horse == null) return "Unknown";

        long claimEpoch = NBT.getPersistentData(horse, nbt -> nbt.getLong("EQUINE_CLAIM_TIME"));
        return new SimpleDateFormat("dd MMM yyyy").format(new Date(claimEpoch));
    }

    public static EquineHorse fromAbstractHorse(AbstractHorse horse) {
        if (horse == null) return null;

        return NBT.getPersistentData(horse, nbt -> {
            String name = horse.getName();
            int age = nbt.getInteger("EQUINE_AGE");
            double heightHands = nbt.getDouble("EQUINE_HEIGHT");

            Discipline discipline = Discipline.getDisciplineByName(nbt.getString("EQUINE_DISCIPLINE"));

            // Breeds handling
            Breed breed1 = Breed.getBreedByName(nbt.getString("EQUINE_BREED_1"));
            Breed breed2 = Breed.getBreedByName(nbt.getString("EQUINE_BREED_2"));

            Breed[] breeds;
            if (breed2 == null || breed2 == Breed.NONE) {
                breeds = new Breed[] { breed1 };
            } else {
                breeds = new Breed[] { breed1, breed2 };
            }

            // Prominent breed
            String prominentBreedName = nbt.getString("EQUINE_PROMINENT_BREED");
            Breed prominentBreed = (prominentBreedName != null && !prominentBreedName.isEmpty())
                    ? Breed.getBreedByName(prominentBreedName)
                    : null;

            Gender gender = Gender.getGenderByName(nbt.getString("EQUINE_GENDER"));

            CoatColor coatColor = CoatColor.NONE;
            CoatModifier coatModifier = CoatModifier.NONE;
            if (horse instanceof Horse h) {
                coatColor = CoatColor.getCoatColorFromHorseColor(h.getColor());
                coatModifier = CoatModifier.getCoatModifierFromHorseStyle(h.getStyle());
            }

            // Traits handling (dynamic, allows 0-3)
            List<Trait> traitList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String traitName = nbt.getString("EQUINE_TRAIT_" + i);
                if (traitName != null && !traitName.isEmpty() && !traitName.equalsIgnoreCase("NONE")) {
                    traitList.add(Trait.getTraitByName(traitName));
                }
            }
            Trait[] traits = traitList.toArray(new Trait[0]);

            EquineHorse equineHorse = new EquineHorse(
                    name,
                    discipline,
                    breeds,
                    coatColor,
                    coatModifier,
                    gender,
                    age,
                    Height.getByHands(heightHands),
                    traits
            );

            equineHorse.setProminentBreed(prominentBreed);
            equineHorse.setUuid(horse.getUniqueId());

            return equineHorse;
        });
    }

}