package endcrypt.equinox.utils;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.equine.bypass.EquineBypass;
import endcrypt.equinox.equine.nbt.Keys;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

import static endcrypt.equinox.EquinoxEquestrian.instance;

public class EquineUtils {

    public static boolean isLivingEquineHorse(AbstractHorse horse) {
        if (horse == null) return false;
        return "true".equalsIgnoreCase(Keys.readPersistentData(horse, Keys.IS_EQUINE));
    }

    public static double getBaseSpeed(AbstractHorse horse) {
        return Keys.readPersistentData(horse, Keys.BASE_SPEED);
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

    public static double blocksToMinecraftJumpStrength(double blocks) {
        return blocks * 0.18181818181818182; // 1 / 5.5
    }

    // Reverse of blocksToMinecraftSpeed
    public static double minecraftSpeedToBlocks(double minecraftSpeed) {
        return roundToTwoDecimals(minecraftSpeed / 0.02777777777777778);
    }

    // Reverse of blocksToMinecraftJumpStrength
    public static double minecraftJumpStrengthToBlocks(double jumpStrength) {
        return roundToTwoDecimals(jumpStrength / 0.18181818181818182);
    }

    // Utility to round to 2 decimal places
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public static AbstractHorse findHorseByUuidAndLocation(UUID uuid, Location location) {
        Entity firstEntity = Bukkit.getEntity(uuid);
        if(firstEntity != null) {
            return (AbstractHorse) firstEntity;
        }

        if(location == null) return null;

        World world = location.getWorld();
        if (world == null) return null;

        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;

        // Load chunk if not loaded
        if (!world.isChunkLoaded(chunkX, chunkZ)) {
            world.loadChunk(chunkX, chunkZ, true); // force load
        }

        Chunk chunk = world.getChunkAt(chunkX, chunkZ);
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof AbstractHorse horse && entity.getUniqueId().equals(uuid)) {
                return horse;
            }
        }

        return null;
    }

    public static boolean hasPermissionToHorse(Player player, AbstractHorse horse) {
        if (player == null || horse == null) return false;
        if (instance.getDatabaseManager().getDatabaseTrustedPlayers().isTrustedToHorse(horse, player)) return true;
        if(Keys.readPersistentData(horse, Keys.IS_PUBLIC)) return true;
        if(EquineBypass.hasBypass(player)) return true;
        if(horse.getOwner() == player) return true;

        return false;
    }

    public static boolean hasNoSelectedHorse(Player player) {
        AbstractHorse horse = instance.getPlayerDataManager().getPlayerData(player).getSelectedHorse();
        if (horse == null) {
            player.sendMessage(ColorUtils.color("<prefix><red>You have not selected a horse!",
                    Placeholder.parsed("prefix", instance.getPrefix())));
            return true;
        }

        return false;

    }

    public static boolean isPlayerHorseSlotsMax(Player player) {
        if (instance.getPermissionManager().getMaxHorsesAllowed(player) <= instance.getDatabaseManager().getDatabaseHorses().getPlayerHorses(player).size()) {
            return true;
        }

        return false;
    }

    public static void applySpeedsToHorse(AbstractHorse horse) {
        EquineHorse equineHorse = fromAbstractHorse(horse);
        double walkSpeed = generateSpeed(0.4, 1.2, 80, 1.2, 2.7, 55);
        double trotSpeed = generateSpeed(3.4, 4.4, 80, 4.4, 5.7, 55);
        double canterSpeed = generateSpeed(7.3, 8.6, 80, 8.6, 9.5, 55);
        double gallopSpeed = generateSpeed(11.4, 12.8, 80, 12.8, 13.7, 55);

        // override speeds if racehorse
        if (EnumSet.of(Discipline.FLAT_RACING_SHORT, Discipline.FLAT_RACING_LONG)
                .contains(equineHorse.getDiscipline())) {
            canterSpeed = randomInRange(7.6, 10.1);  // 13–15 mph
            gallopSpeed = randomInRange(12.5, 15.0); // 25–45 mph
        }

        // apply converted speeds to NBT
        Keys.writePersistentData(horse, Keys.WALK_SPEED, EquineUtils.blocksToMnecraftSpeed(walkSpeed));
        Keys.writePersistentData(horse, Keys.TROT_SPEED, EquineUtils.blocksToMnecraftSpeed(trotSpeed));
        Keys.writePersistentData(horse, Keys.CANTER_SPEED, EquineUtils.blocksToMnecraftSpeed(canterSpeed));
        Keys.writePersistentData(horse, Keys.GALLOP_SPEED, EquineUtils.blocksToMnecraftSpeed(gallopSpeed));
    }

    private static double generateSpeed(double min1, double max1, int chance1,
                                        double min2, double max2, int chance2) {
        Random random = new Random();
        int roll = random.nextInt(100) + 1;
        double value;

        if (roll <= chance1) {
            value = randomInRange(min1, max1);
        } else if (roll <= chance1 + chance2) {
            value = randomInRange(min2, max2);
        } else {
            value = randomInRange(min1, max1);
        }
        return Math.round(value * 100.0) / 100.0;
    }

    private static double randomInRange(double min, double max) {
        Random random = new Random();

        double val = min + (max - min) * random.nextDouble();
        return Math.round(val * 100.0) / 100.0;
    }

}