package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;

import java.util.ArrayList;

import endcrypt.equinox.equine.nbt.Keys;
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

    // Others
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

    private boolean isInHeat;
    private long lastInHeat;

    private boolean isBreeding;
    private long breedingStartTime;
    private String breedingPartnerUUID;
    private boolean isInstantBreed;

    private boolean isPregnant;
    private long pregnancyStartTime;
    private String pregnancyPartnerUUID;
    private boolean isInstantFoal;

    private long lastPoop;
    private long lastPee;

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
        this.isCrossTied = EquineUtils.isCrossTied(horse);
        this.isPublic = EquineUtils.isHorsePublic(horse);
        this.isInHeat = EquineUtils.isHorseInHeat(horse);
        this.isPregnant = EquineUtils.isHorsePregnant(horse);
        this.isBreeding = EquineUtils.isBreeding(horse);
        this.isInstantFoal = EquineUtils.isInstantFoal(horse);
        this.isInstantBreed = EquineUtils.isInstantBreed(horse);

        NBT.getPersistentData(horse, nbt -> this.claimTime = nbt.getLong(Keys.CLAIM_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.birthTime = nbt.getLong(Keys.BIRTH_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.ownerName = nbt.getString(Keys.OWNER_NAME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.ownerUUID = nbt.getString(Keys.OWNER_UUID.getKey()));
        NBT.getPersistentData(horse, nbt -> this.baseSpeed = nbt.getDouble(Keys.BASE_SPEED.getKey()));
        NBT.getPersistentData(horse, nbt -> this.baseJumpPower = nbt.getDouble(Keys.BASE_JUMP.getKey()));
        NBT.getPersistentData(horse, nbt -> this.skullId = nbt.getString(Keys.SKULL_ID.getKey()));

        NBT.getPersistentData(horse, nbt -> this.lastInHeat = nbt.getLong(Keys.LAST_IN_HEAT.getKey()));

        NBT.getPersistentData(horse, nbt -> this.pregnancyStartTime = nbt.getLong(Keys.PREGNANCY_START_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.pregnancyPartnerUUID = nbt.getString(Keys.PREGNANCY_PARTNER.getKey()));

        NBT.getPersistentData(horse, nbt -> this.breedingStartTime = nbt.getLong(Keys.BREEDING_START_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.breedingPartnerUUID = nbt.getString(Keys.BREEDING_PARTNER.getKey()));

        NBT.getPersistentData(horse, nbt -> this.lastPoop = nbt.getLong(Keys.LAST_POOP.getKey()));
        NBT.getPersistentData(horse, nbt -> this.lastPee = nbt.getLong(Keys.LAST_PEE.getKey()));

        World lastWorld = Bukkit.getWorld((String) NBT.getPersistentData(horse, nbt -> nbt.getString(Keys.LAST_WORLD.getKey())));
        double lastX = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_X.getKey()));
        double lastY = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_Y.getKey()));
        double lastZ = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_Z.getKey()));

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
        Objects.requireNonNull(horse1.getAttribute(Attribute.SCALE)).setBaseValue(height.getSize());
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
