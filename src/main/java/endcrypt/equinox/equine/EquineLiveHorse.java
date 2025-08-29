package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;

import java.util.ArrayList;

import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.utils.UniqueIdUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
        this.isCrossTied = Keys.readBoolean(horse, Keys.IS_CROSS_TIED);
        this.isPublic = Keys.readBoolean(horse, Keys.IS_PUBLIC);
        this.isInHeat = Keys.readBoolean(horse, Keys.IS_IN_HEAT);
        this.isPregnant = Keys.readBoolean(horse, Keys.IS_PREGNANT);
        this.isBreeding    = Keys.readBoolean(horse, Keys.IS_BREEDING);
        this.isInstantFoal = Keys.readBoolean(horse, Keys.INSTANT_FOAL);
        this.isInstantBreed= Keys.readBoolean(horse, Keys.INSTANT_BREED);

        // Core stats
        this.claimTime     = Keys.readLong(horse, Keys.CLAIM_TIME);
        this.birthTime     = Keys.readLong(horse, Keys.BIRTH_TIME);
        this.ownerName     = Keys.readString(horse, Keys.OWNER_NAME);
        this.ownerUUID     = Keys.readString(horse, Keys.OWNER_UUID);
        this.baseSpeed     = Keys.readDouble(horse, Keys.BASE_SPEED);
        this.baseJumpPower = Keys.readDouble(horse, Keys.BASE_JUMP);
        this.skullId       = Keys.readString(horse, Keys.SKULL_ID);

        // Heat + pregnancy
        this.lastInHeat            = Keys.readLong(horse, Keys.LAST_IN_HEAT);
        this.pregnancyStartTime    = Keys.readLong(horse, Keys.PREGNANCY_START_TIME);
        this.pregnancyPartnerUUID  = Keys.readString(horse, Keys.PREGNANCY_PARTNER);
        this.breedingStartTime     = Keys.readLong(horse, Keys.BREEDING_START_TIME);
        this.breedingPartnerUUID   = Keys.readString(horse, Keys.BREEDING_PARTNER);

        // Body functions
        this.lastPoop  = Keys.readLong(horse, Keys.LAST_POOP);
        this.lastPee   = Keys.readLong(horse, Keys.LAST_PEE);

        // Hunger + thirst
        this.hungerPercentage  = Keys.readDouble(horse, Keys.HUNGER_PERCENTAGE);
        this.lastHungerUpdate  = Keys.readLong(horse, Keys.LAST_HUNGER_UPDATE);
        this.lastSeekFood      = Keys.readLong(horse, Keys.LAST_SEEK_FOOD);
        this.isInFoodTask      = Keys.readBoolean(horse, Keys.IS_IN_FOOD_TASK);

        this.thirstPercentage  = Keys.readDouble(horse, Keys.THIRST_PERCENTAGE);
        this.lastThirstUpdate  = Keys.readLong(horse, Keys.LAST_THIRST_UPDATE);
        this.lastSeekWater     = Keys.readLong(horse, Keys.LAST_SEEK_WATER);
        this.isInWaterTask     = Keys.readBoolean(horse, Keys.IS_IN_WATER_TASK);

        // Movement speeds
        this.walkSpeed   = Keys.readDouble(horse, Keys.WALK_SPEED);
        this.trotSpeed   = Keys.readDouble(horse, Keys.TROT_SPEED);
        this.canterSpeed = Keys.readDouble(horse, Keys.CANTER_SPEED);
        this.gallopSpeed = Keys.readDouble(horse, Keys.GALLOP_SPEED);

        // Last location
        World lastWorld = Bukkit.getWorld(Keys.readString(horse, Keys.LAST_WORLD));
        double lastX    = Keys.readDouble(horse, Keys.LAST_LOCATION_X);
        double lastY    = Keys.readDouble(horse, Keys.LAST_LOCATION_Y);
        double lastZ    = Keys.readDouble(horse, Keys.LAST_LOCATION_Z);

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

        UUID possibleOwnerUUID;
        if (UniqueIdUtil.isValidUUID(this.ownerUUID)) {
            possibleOwnerUUID = UUID.fromString(this.ownerUUID);
        } else {
            possibleOwnerUUID = horse.getOwnerUniqueId();
        }

        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setLong(Keys.CLAIM_TIME.getKey(), this.claimTime);
            nbt.setLong(Keys.BIRTH_TIME.getKey(), this.birthTime);
            nbt.setString(Keys.OWNER_NAME.getKey(), Bukkit.getOfflinePlayer(Objects.requireNonNull(possibleOwnerUUID)).getName());
            nbt.setString(Keys.OWNER_UUID.getKey(), this.ownerUUID);
            nbt.setDouble(Keys.BASE_SPEED.getKey(), this.baseSpeed);
            nbt.setDouble(Keys.BASE_JUMP.getKey(), this.baseJumpPower);
            nbt.setString(Keys.SKULL_ID.getKey(), this.skullId);
            nbt.setString(Keys.LAST_WORLD.getKey(), this.horse.getWorld().getName());
            nbt.setDouble(Keys.LAST_LOCATION_X.getKey(), this.horse.getLocation().getX());
            nbt.setDouble(Keys.LAST_LOCATION_Y.getKey(), this.horse.getLocation().getY());
            nbt.setDouble(Keys.LAST_LOCATION_Z.getKey(), this.horse.getLocation().getZ());
            nbt.setBoolean(Keys.IS_PUBLIC.getKey(), this.isPublic);
            nbt.setBoolean(Keys.IS_IN_HEAT.getKey(), this.isInHeat);
            nbt.setLong(Keys.LAST_IN_HEAT.getKey(), this.lastInHeat);
            nbt.setBoolean(Keys.IS_PREGNANT.getKey(), this.isPregnant);
            nbt.setLong(Keys.PREGNANCY_START_TIME.getKey(), this.pregnancyStartTime);
            nbt.setString(Keys.PREGNANCY_PARTNER.getKey(), this.pregnancyPartnerUUID);
            nbt.setBoolean(Keys.IS_BREEDING.getKey(), this.isBreeding);
            nbt.setLong(Keys.BREEDING_START_TIME.getKey(), this.breedingStartTime);
            nbt.setString(Keys.BREEDING_PARTNER.getKey(), this.breedingPartnerUUID);
            nbt.setBoolean(Keys.INSTANT_FOAL.getKey(), this.isInstantFoal);
            nbt.setBoolean(Keys.INSTANT_BREED.getKey(), this.isInstantBreed);
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
