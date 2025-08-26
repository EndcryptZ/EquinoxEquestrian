package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.EquineUtils;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

import java.util.ArrayList;

import java.util.*;
import java.util.stream.IntStream;

public class EquineHorseBuilder {
    private final EquinoxEquestrian plugin;

    public EquineHorseBuilder(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void spawnFoal(EquineLiveHorse mare, EquineLiveHorse stallion, Location location, String owner) {
        EquineHorse foal = combineGenetics(mare, stallion);
        spawnHorse(owner, location, foal, true);
    }

    private EquineHorse combineGenetics(EquineLiveHorse mare, EquineLiveHorse stallion) {
        Random random = new Random();

        List<Breed> foalBreeds = new ArrayList<>();
        foalBreeds.add(random.nextBoolean() ? mare.getBreeds().getFirst() : stallion.getBreeds().getFirst());

        CoatColor foalColor = random.nextBoolean() ? mare.getCoatColor() : stallion.getCoatColor();
        CoatModifier foalModifier = random.nextBoolean() ? mare.getCoatModifier() : stallion.getCoatModifier();
        Gender foalGender = random.nextBoolean() ? Gender.MARE : Gender.STALLION;
        Height foalHeight = Height.getRandomHeight(foalBreeds.getFirst());

        List<Trait> foalTraits = new ArrayList<>();
        int traitCount = Math.min(3, Math.max(mare.getTraits().size(), stallion.getTraits().size()));
        for (int i = 0; i < traitCount; i++) {
            foalTraits.add(random.nextBoolean() && i < mare.getTraits().size()
                    ? mare.getTraits().get(i)
                    : stallion.getTraits().get(Math.min(i, stallion.getTraits().size() - 1)));
        }

        return new EquineHorse(
                mare.getName() + "'s Foal",
                random.nextBoolean() ? mare.getDiscipline() : stallion.getDiscipline(),
                foalBreeds,
                foalColor,
                foalModifier,
                foalGender,
                0,
                foalHeight,
                foalTraits
        );
    }

    private static final long MILLIS_PER_YEAR = 30L * 24 * 60 * 60 * 1000;

    // Method to spawn the horse at a player's location
    public void spawnHorse(String ownerUUID, Location loc, EquineHorse equineHorse, boolean isBaby) {
        World world = loc.getWorld();

        // Spawn the horse
        Horse horse = (Horse) world.spawnEntity(loc, EntityType.HORSE);
        horse.setPersistent(true);

        // Set the horse's custom name
        horse.customName(LegacyComponentSerializer.legacyAmpersand().deserialize(equineHorse.getName()));
        horse.setCustomNameVisible(true);

        // Optional: Set horse as tamed
        horse.setTamed(true);
        horse.setOwner(Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)));
        horse.setAge(equineHorse.getAge());
        if(isBaby) {
            horse.setBaby();
        }

        horse.setColor(equineHorse.getCoatColor().getHorseColor());

        if(equineHorse.getCoatModifier() != CoatModifier.NONE) {
            horse.setStyle(equineHorse.getCoatModifier().getHorseCoatModifier());
        }

        Objects.requireNonNull(horse.getAttribute(Attribute.SCALE)).setBaseValue(equineHorse.getHeight().getSize());

        NBT.modifyPersistentData(horse, nbt -> {
            long currentTime = System.currentTimeMillis();

            // Set base horse properties
            nbt.setString(Keys.IS_EQUINE.getKey(), (String) Keys.IS_EQUINE.getDefaultValue());
            nbt.setString(Keys.DISCIPLINE.getKey(), equineHorse.getDiscipline().name());

            // Set breed properties 
            IntStream.range(0, equineHorse.getBreeds().size())
                    .forEach(i -> nbt.setString(Keys.BREED_PREFIX.getKey() + i, equineHorse.getBreeds().get(i).name()));

            // Set prominent breed for multi-breed horses
            if (equineHorse.getBreeds().size() > 1) {
                equineHorse.setProminentBreed(equineHorse.getBreeds().get(new Random().nextInt(equineHorse.getBreeds().size())));
                nbt.setString(Keys.PROMINENT_BREED.getKey(), equineHorse.getProminentBreed().name());
            }

            // Set appearance properties
            nbt.setString(Keys.GENDER.getKey(), equineHorse.getGender().name());
            nbt.setInteger(Keys.AGE.getKey(), equineHorse.getAge());
            nbt.setDouble(Keys.HEIGHT.getKey(), equineHorse.getHeight().getHands());
            nbt.setString(Keys.COAT_COLOR.getKey(), equineHorse.getCoatColor().name());
            nbt.setString(Keys.COAT_MODIFIER.getKey(), equineHorse.getCoatModifier().name());

            // Set traits
            IntStream.range(0, equineHorse.getTraits().size())
                    .forEach(i -> nbt.setString(Keys.TRAIT_PREFIX.getKey() + i, equineHorse.getTraits().get(i).name()));

            // Set metadata
            nbt.setLong(Keys.CLAIM_TIME.getKey(), currentTime);
            nbt.setLong(Keys.BIRTH_TIME.getKey(), currentTime - (MILLIS_PER_YEAR * equineHorse.getAge()));
            nbt.setString(Keys.OWNER_UUID.getKey(), ownerUUID);
            nbt.setString(Keys.OWNER_NAME.getKey(), Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).getName());

            // Set base stats
            nbt.setDouble(Keys.BASE_SPEED.getKey(), Objects.requireNonNull(horse.getAttribute(Attribute.MOVEMENT_SPEED)).getBaseValue());
            nbt.setString(Keys.SKULL_ID.getKey(), randomSkullId());

            // Set state flags
            nbt.setString(Keys.IS_CROSS_TIED.getKey(), (String) Keys.IS_CROSS_TIED.getDefaultValue());

            // Last Location
            nbt.setString(Keys.LAST_WORLD.getKey(), horse.getWorld().getName());
            nbt.setDouble(Keys.LAST_LOCATION_X.getKey(), horse.getLocation().getX());
            nbt.setDouble(Keys.LAST_LOCATION_Y.getKey(), horse.getLocation().getY());
            nbt.setDouble(Keys.LAST_LOCATION_Z.getKey(), horse.getLocation().getZ());

            // No Instant Waste
            nbt.setLong(Keys.LAST_PEE.getKey(), System.currentTimeMillis());
            nbt.setLong(Keys.LAST_POOP.getKey(), System.currentTimeMillis());

            // Set Hunger & Thirst
            nbt.setDouble(Keys.HUNGER_PERCENTAGE.getKey(), 100.0);
            nbt.setLong(Keys.LAST_HUNGER_UPDATE.getKey(), System.currentTimeMillis());
            nbt.setLong(Keys.LAST_SEEK_FOOD.getKey(), System.currentTimeMillis());
            nbt.setDouble(Keys.THIRST_PERCENTAGE.getKey(), 100.0);
            nbt.setLong(Keys.LAST_THIRST_UPDATE.getKey(), System.currentTimeMillis());


            // SPEED
            // normal horses
            double walkSpeed = generateSpeed(0.4, 1.2, 1.2, 2.7);
            double trotSpeed = generateSpeed(3.4, 4.4, 4.4, 5.7);
            double canterSpeed = generateSpeed(7.3, 8.6, 8.6, 9.5);
            double gallopSpeed = generateSpeed(11.4, 12.8, 12.8, 13.7);

            // racehorses (overrides for racing disciplines)
            if (EnumSet.of(Discipline.FLAT_RACING_SHORT, Discipline.FLAT_RACING_LONG)
                    .contains(equineHorse.getDiscipline())) {
                canterSpeed = randomInRange(7.6, 10.1); // 13–15 mph
                gallopSpeed = randomInRange(12.5, 15.0); // 25–45 mph
            }

            // set NBT speeds
            nbt.setDouble(Keys.WALK_SPEED.getKey(), EquineUtils.blocksToMnecraftSpeed(walkSpeed));
            nbt.setDouble(Keys.TROT_SPEED.getKey(), EquineUtils.blocksToMnecraftSpeed(trotSpeed));
            nbt.setDouble(Keys.CANTER_SPEED.getKey(), EquineUtils.blocksToMnecraftSpeed(canterSpeed));
            nbt.setDouble(Keys.GALLOP_SPEED.getKey(), EquineUtils.blocksToMnecraftSpeed(gallopSpeed));

            // Set Jump
            double jumpStrength = generateJumpStrength();

            // Show Jumpers (overrides for jumper discipline)
            if (equineHorse.getDiscipline() == Discipline.SHOW_JUMPING) {
                jumpStrength = 4.0;
            }

            nbt.setDouble(Keys.BASE_JUMP.getKey(), EquineUtils.blocksToMinecraftJumpStrength(jumpStrength));
            Objects.requireNonNull(horse.getAttribute(Attribute.JUMP_STRENGTH)).setBaseValue(EquineUtils.blocksToMinecraftJumpStrength(jumpStrength));

        });

        plugin.getDatabaseManager().getDatabaseHorses().addHorse(horse);


    }

    // Assigns a horse a random skull for menus
    private String randomSkullId() {
        List<String> idList = Arrays.asList("49654", "7280", "49652", "49651", "1154", "3920", "3919", "2912", "7649");
        Random random = new Random();
        int randomID = random.nextInt(idList.size());
        return idList.get(randomID);
    }

    public EquineHorse randomHorse(String name) {
        Random random = new Random();

        Discipline discipline = Discipline.random();
        List<Breed> breeds = Breed.random(random.nextInt(2) + 1);
        CoatColor coatColor = CoatColor.random();
        CoatModifier coatModifier = CoatModifier.random();
        Gender gender = Gender.random();
        int age = random.nextInt(10) + 1;
        Height height = Height.getRandomHeight(breeds.getFirst());
        List<Trait> traits = Trait.random(random.nextInt(3) + 1);

        return new EquineHorse(name, discipline, breeds, coatColor, coatModifier, gender, age, height, traits);
    }

    private double generateSpeed(double min1, double max1,
                                 double min2, double max2) {
        Random random = new Random();
        int roll = random.nextInt(100) + 1;
        double value;

        if (roll <= 80) {
            value = randomInRange(min1, max1);
        } else {
            value = randomInRange(min2, max2);
        }

        return Math.round(value * 100.0) / 100.0;
    }


    public double generateJumpStrength() {
        Random random = new Random();

        // weights
        double[] values = {2.7, 3.2, 3.6, 4.0};
        int[] weights = {80, 65, 45, 25}; // chance weights

        // total weight
        int totalWeight = 0;
        for (int w : weights) {
            totalWeight += w;
        }

        // roll between 0 and totalWeight
        int roll = random.nextInt(totalWeight) + 1;

        // pick based on cumulative weight
        int cumulative = 0;
        for (int i = 0; i < values.length; i++) {
            cumulative += weights[i];
            if (roll <= cumulative) {
                return values[i];
            }
        }

        return -1; // should never happen
    }

    private static double randomInRange(double min, double max) {
        Random random = new Random();
        double val = min + (max - min) * random.nextDouble();
        return Math.round(val * 100.0) / 100.0;
    }
}
