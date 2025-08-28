package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;

import java.util.ArrayList;

import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.EquineUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Horse;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Setter
@Getter
public class EquineLiveHorse {


    // Attributes
    private String name;
    private Discipline discipline;
    private List<Breed> breeds;
    private Breed prominentBreed;
    private CoatColor coatColor;
    private CoatModifier coatModifier;
    private Gender gender;
    private int age;
    private Height height;
    private List<Trait> traits;

    // Misc
    private UUID uuid;
    private long claimTime;
    private long birthTime;
    private String ownerName;
    private String ownerUUID;
    private double baseSpeed;
    private double baseJumpPower;
    private String skullId;
    private Location lastLocation;
    private boolean isCrossTied;
    private boolean isPublic;

    // In Heat
    private boolean isInHeat;
    private long lastInHeat;

    // Breeding
    private boolean isBreeding;
    private long breedingStartTime;
    private String breedingPartnerUUID;
    private boolean isInstantBreed;

    // Pregnancy
    private boolean isPregnant;
    private long pregnancyStartTime;
    private String pregnancyPartnerUUID;
    private boolean isInstantFoal;

    // Wasting
    private long lastPoop;
    private long lastPee;

    // Hunger
    private double hungerPercentage;
    private long lastHungerUpdate;
    private long lastSeekFood;
    private boolean isInFoodTask;

    // Thirst
    private double thirstPercentage;
    private long lastThirstUpdate;
    private long lastSeekWater;
    private boolean isInWaterTask;

    // Gait Speeds
    private double walkSpeed;
    private double trotSpeed;
    private double canterSpeed;
    private double gallopSpeed;

    private final AbstractHorse horse;
    
    public EquineLiveHorse() {
        this.horse = null;
        this.name = null;
        this.discipline = null;
        this.breeds = null;
        this.prominentBreed = null;
        this.coatColor = null;
        this.coatModifier = null;
        this.gender = null;
        this.age = 0;
        this.height = null;
        this.traits = new ArrayList<>();
        this.uuid = null;
        this.claimTime = 0L;
        this.birthTime = 0L;
        this.ownerName = null;
        this.ownerUUID = null;
        this.baseSpeed = 0.0;
        this.baseJumpPower = 0.0;
        this.skullId = null;
        this.lastLocation = null;
        this.isCrossTied = false;
        this.isPublic = false;
        this.isInHeat = false;
        this.lastInHeat = 0L;
        this.isPregnant = false;
        this.pregnancyStartTime = 0L;
        this.isBreeding = false;
        this.breedingStartTime = 0L;
        this.breedingPartnerUUID = null;
        this.lastPoop = 0L;
        this.lastPee = 0L;
        this.hungerPercentage = 100.0;
        this.lastHungerUpdate = 0L;
        this.lastSeekFood = 0L;
        this.isInFoodTask = false;
        this.thirstPercentage = 100.0;
        this.lastThirstUpdate = 0L;
        this.lastSeekWater = 0L;
        this.isInWaterTask = false;
        this.walkSpeed = 0.0;
        this.trotSpeed = 0.0;
        this.canterSpeed = 0.0;
        this.gallopSpeed = 0.0;
    }


    public EquineLiveHorse(AbstractHorse horse) {
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);

        this.name = horse.getName();
        this.discipline = equineHorse.getDiscipline();
        this.breeds = equineHorse.getBreeds();
        this.prominentBreed = equineHorse.getProminentBreed();
        this.coatColor = equineHorse.getCoatColor();
        this.coatModifier = equineHorse.getCoatModifier();
        this.gender = equineHorse.getGender();
        this.age = equineHorse.getAge();
        this.height = equineHorse.getHeight();
        this.traits = equineHorse.getTraits();

        this.uuid = horse.getUniqueId();
        this.horse = horse;

        // Simple flags
        this.isCrossTied   = Keys.readPersistentData(horse, Keys.IS_CROSS_TIED);
        this.isPublic      = Keys.readPersistentData(horse, Keys.IS_PUBLIC);
        this.isInHeat      = Keys.readPersistentData(horse, Keys.IS_IN_HEAT);
        this.isPregnant    = Keys.readPersistentData(horse, Keys.IS_PREGNANT);
        this.isBreeding    = Keys.readPersistentData(horse, Keys.IS_BREEDING);
        this.isInstantFoal = Keys.readPersistentData(horse, Keys.INSTANT_FOAL);
        this.isInstantBreed= Keys.readPersistentData(horse, Keys.INSTANT_BREED);

        // Core stats
        this.claimTime     = Keys.readPersistentData(horse, Keys.CLAIM_TIME);
        this.birthTime     = Keys.readPersistentData(horse, Keys.BIRTH_TIME);
        this.ownerName     = Keys.readPersistentData(horse, Keys.OWNER_NAME);
        this.ownerUUID     = Keys.readPersistentData(horse, Keys.OWNER_UUID);
        this.baseSpeed     = Keys.readPersistentData(horse, Keys.BASE_SPEED);
        this.baseJumpPower = Keys.readPersistentData(horse, Keys.BASE_JUMP);
        this.skullId       = Keys.readPersistentData(horse, Keys.SKULL_ID);

        // Heat + pregnancy
        this.lastInHeat            = Keys.readPersistentData(horse, Keys.LAST_IN_HEAT);
        this.pregnancyStartTime    = Keys.readPersistentData(horse, Keys.PREGNANCY_START_TIME);
        this.pregnancyPartnerUUID  = Keys.readPersistentData(horse, Keys.PREGNANCY_PARTNER);
        this.breedingStartTime     = Keys.readPersistentData(horse, Keys.BREEDING_START_TIME);
        this.breedingPartnerUUID   = Keys.readPersistentData(horse, Keys.BREEDING_PARTNER);

        // Body functions
        this.lastPoop  = Keys.readPersistentData(horse, Keys.LAST_POOP);
        this.lastPee   = Keys.readPersistentData(horse, Keys.LAST_PEE);

        // Hunger + thirst
        this.hungerPercentage  = Keys.readPersistentData(horse, Keys.HUNGER_PERCENTAGE);
        this.lastHungerUpdate  = Keys.readPersistentData(horse, Keys.LAST_HUNGER_UPDATE);
        this.lastSeekFood      = Keys.readPersistentData(horse, Keys.LAST_SEEK_FOOD);
        this.isInFoodTask      = Keys.readPersistentData(horse, Keys.IS_IN_FOOD_TASK);

        this.thirstPercentage  = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);
        this.lastThirstUpdate  = Keys.readPersistentData(horse, Keys.LAST_THIRST_UPDATE);
        this.lastSeekWater     = Keys.readPersistentData(horse, Keys.LAST_SEEK_WATER);
        this.isInWaterTask     = Keys.readPersistentData(horse, Keys.IS_IN_WATER_TASK);

        // Movement speeds
        this.walkSpeed   = Keys.readPersistentData(horse, Keys.WALK_SPEED);
        this.trotSpeed   = Keys.readPersistentData(horse, Keys.TROT_SPEED);
        this.canterSpeed = Keys.readPersistentData(horse, Keys.CANTER_SPEED);
        this.gallopSpeed = Keys.readPersistentData(horse, Keys.GALLOP_SPEED);

        // Last location
        World lastWorld = Bukkit.getWorld((Key) Keys.readPersistentData(horse, Keys.LAST_WORLD));
        double lastX    = Keys.readPersistentData(horse, Keys.LAST_LOCATION_X);
        double lastY    = Keys.readPersistentData(horse, Keys.LAST_LOCATION_Y);
        double lastZ    = Keys.readPersistentData(horse, Keys.LAST_LOCATION_Z);

        this.lastLocation = new Location(lastWorld, lastX, lastY, lastZ);
    }
    public void breed(EquineLiveHorse horse) {
        this.isBreeding = true;
        this.breedingPartnerUUID = horse.getUuid().toString();
        this.breedingStartTime = System.currentTimeMillis();
        this.update();
    }

    public void unbreed() {
        this.isBreeding = false;
        this.breedingPartnerUUID = null;
        this.breedingStartTime = 0L;
        this.update();
    }

    public void pregnant(EquineLiveHorse horse) {
        this.isPregnant = true;
        this.pregnancyPartnerUUID = horse.getUuid().toString();
        this.pregnancyStartTime = System.currentTimeMillis();
        this.isInHeat = false;
        this.update();
    }

    public int getSpeedDivision() {
        double mphHorseSpeed = EquineUtils.minecraftSpeedToBlocks(gallopSpeed);

        if (mphHorseSpeed >= 45 && mphHorseSpeed <= 50) return 1;
        if (mphHorseSpeed >= 40 && mphHorseSpeed < 45) return 2;
        if (mphHorseSpeed >= 35 && mphHorseSpeed < 40) return 3;
        if (mphHorseSpeed >= 30 && mphHorseSpeed < 35) return 4;
        if (mphHorseSpeed >= 25 && mphHorseSpeed < 30) return 5;
        if (mphHorseSpeed >= 20 && mphHorseSpeed < 25) return 6;
        if (mphHorseSpeed >= 15 && mphHorseSpeed < 20) return 7;
        if (mphHorseSpeed >= 10 && mphHorseSpeed < 15) return 8;

        return 0; // Out of range
    }


    public void update() {

        updateDefault();

        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setLong(Keys.CLAIM_TIME.getKey(), this.claimTime);
            nbt.setLong(Keys.BIRTH_TIME.getKey(), this.birthTime);
            nbt.setString(Keys.OWNER_NAME.getKey(), Bukkit.getOfflinePlayer(UUID.fromString(this.ownerUUID)).getName());
            nbt.setString(Keys.OWNER_UUID.getKey(), this.ownerUUID);
            nbt.setDouble(Keys.BASE_SPEED.getKey(), this.baseSpeed);
            nbt.setDouble(Keys.BASE_JUMP.getKey(), this.baseJumpPower);
            nbt.setString(Keys.SKULL_ID.getKey(), this.skullId);
            nbt.setString(Keys.LAST_WORLD.getKey(), this.horse.getWorld().getName());
            nbt.setDouble(Keys.LAST_LOCATION_X.getKey(), this.horse.getLocation().getX());
            nbt.setDouble(Keys.LAST_LOCATION_Y.getKey(), this.horse.getLocation().getY());
            nbt.setDouble(Keys.LAST_LOCATION_Z.getKey(), this.horse.getLocation().getZ());
            nbt.setString(Keys.IS_PUBLIC.getKey(), String.valueOf(this.isPublic));
            nbt.setString(Keys.IS_IN_HEAT.getKey(), String.valueOf(this.isInHeat));
            nbt.setLong(Keys.LAST_IN_HEAT.getKey(), this.lastInHeat);
            nbt.setString(Keys.IS_PREGNANT.getKey(), String.valueOf(this.isPregnant));
            nbt.setLong(Keys.PREGNANCY_START_TIME.getKey(), this.pregnancyStartTime);
            nbt.setString(Keys.PREGNANCY_PARTNER.getKey(), this.pregnancyPartnerUUID);
            nbt.setString(Keys.IS_BREEDING.getKey(), String.valueOf(this.isBreeding));
            nbt.setLong(Keys.BREEDING_START_TIME.getKey(), this.breedingStartTime);
            nbt.setString(Keys.BREEDING_PARTNER.getKey(), this.breedingPartnerUUID);
            nbt.setString(Keys.INSTANT_FOAL.getKey(), String.valueOf(this.isInstantFoal));
            nbt.setString(Keys.INSTANT_BREED.getKey(), String.valueOf(this.isInstantBreed));
            nbt.setInteger(Keys.AGE.getKey(), this.age);
            nbt.setLong(Keys.LAST_POOP.getKey(), this.lastPoop);
            nbt.setLong(Keys.LAST_PEE.getKey(), this.lastPee);
            nbt.setDouble(Keys.HEIGHT.getKey(), this.height.getHands());
            nbt.setDouble(Keys.HUNGER_PERCENTAGE.getKey(), this.hungerPercentage);
            nbt.setLong(Keys.LAST_HUNGER_UPDATE.getKey(), this.lastHungerUpdate);
            nbt.setLong(Keys.LAST_SEEK_FOOD.getKey(), this.lastSeekFood);
            nbt.setBoolean(Keys.IS_IN_FOOD_TASK.getKey(), this.isInFoodTask);
            nbt.setDouble(Keys.THIRST_PERCENTAGE.getKey(), this.thirstPercentage);
            nbt.setLong(Keys.LAST_THIRST_UPDATE.getKey(), this.lastThirstUpdate);
            nbt.setLong(Keys.LAST_SEEK_WATER.getKey(), this.lastSeekWater);
            nbt.setBoolean(Keys.IS_IN_WATER_TASK.getKey(), this.isInWaterTask);
            nbt.setDouble(Keys.WALK_SPEED.getKey(), this.walkSpeed);
            nbt.setDouble(Keys.TROT_SPEED.getKey(), this.trotSpeed);
            nbt.setDouble(Keys.CANTER_SPEED.getKey(), this.canterSpeed);
            nbt.setDouble(Keys.GALLOP_SPEED.getKey(), this.gallopSpeed);

            // Traits Updater
            for (int i = 0; i <= 3; i++) {
                if (i < this.traits.size()) {
                    nbt.setString(Keys.TRAIT_PREFIX.getKey() + i, this.traits.get(i).name());
                } else {
                    nbt.setString(Keys.TRAIT_PREFIX.getKey() + i, "");
                }
            }
        });

        Horse horse1 = (Horse) horse;
        horse1.setColor(coatColor.getHorseColor());     
        horse1.setStyle(coatModifier.getHorseCoatModifier());
        Objects.requireNonNull(horse.getAttribute(Attribute.SCALE)).setBaseValue(height.getSize());
        horse.setAge(this.age);
    }

    public void updateDefault() {
        NBT.modifyPersistentData(horse, nbt -> {
            for (Keys key : Keys.values()) {
                String tag = key.getKey();
                Object value = key.getDefaultValue();

                if (!nbt.hasTag(tag)) {
                    if (value instanceof String str) {
                        nbt.setString(tag, str);
                    } else if (value instanceof UUID uuid) {
                        nbt.setUUID(tag, uuid);
                    } else if (value instanceof Long lng) {
                        nbt.setLong(tag, lng);
                    } else if (value instanceof Integer i) {
                        nbt.setInteger(tag, i);
                    } else if (value instanceof Double d) {
                        nbt.setDouble(tag, d);
                    }

                    // Debug
                    // Bukkit.broadcast(ColorUtils.color(tag + " was missing and has been set to default."));
                }
            }
        });
    }
}
