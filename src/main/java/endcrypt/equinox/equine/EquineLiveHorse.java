package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;

import java.util.ArrayList;

import endcrypt.equinox.equine.nbt.Keys;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;

import java.util.List;
import java.util.UUID;

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
    private boolean isPublic;
    private boolean isInHeat;
    private boolean isPregnant;

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
        this.isPublic = false;
        this.isInHeat = false;
        this.isPregnant = false;
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
        this.isPublic = EquineUtils.isHorsePublic(horse);
        this.isInHeat = EquineUtils.isHorseInHeat(horse);
        this.isPregnant = EquineUtils.isHorsePregnant(horse);

        NBT.getPersistentData(horse, nbt -> this.claimTime = nbt.getLong(Keys.CLAIM_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.birthTime = nbt.getLong(Keys.BIRTH_TIME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.ownerName = nbt.getString(Keys.OWNER_NAME.getKey()));
        NBT.getPersistentData(horse, nbt -> this.ownerUUID = nbt.getString(Keys.OWNER_UUID.getKey()));
        NBT.getPersistentData(horse, nbt -> this.baseSpeed = nbt.getDouble(Keys.BASE_SPEED.getKey()));
        NBT.getPersistentData(horse, nbt -> this.baseJumpPower = nbt.getDouble(Keys.BASE_JUMP.getKey()));
        NBT.getPersistentData(horse, nbt -> this.skullId = nbt.getString(Keys.SKULL_ID.getKey()));

        World lastWorld = Bukkit.getWorld((String) NBT.getPersistentData(horse, nbt -> nbt.getString(Keys.LAST_WORLD.getKey())));
        double lastX = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_X.getKey()));
        double lastY = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_Y.getKey()));
        double lastZ = NBT.getPersistentData(horse, nbt -> nbt.getDouble(Keys.LAST_LOCATION_Z.getKey()));

        this.lastLocation = new Location(lastWorld, lastX, lastY, lastZ);
    }

    public void update() {

        updateDefault();

        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setLong(Keys.CLAIM_TIME.getKey(), this.claimTime);
            nbt.setLong(Keys.BIRTH_TIME.getKey(), this.birthTime);
            nbt.setString(Keys.OWNER_NAME.getKey(), this.ownerName);
            nbt.setString(Keys.OWNER_UUID.getKey(), horse.getOwnerUniqueId().toString());
            nbt.setDouble(Keys.BASE_SPEED.getKey(), this.baseSpeed);
            nbt.setDouble(Keys.BASE_JUMP.getKey(), this.baseJumpPower);
            nbt.setString(Keys.SKULL_ID.getKey(), this.skullId);
            nbt.setString(Keys.LAST_WORLD.getKey(), horse.getWorld().getName());
            nbt.setDouble(Keys.LAST_LOCATION_X.getKey(), horse.getLocation().getX());
            nbt.setDouble(Keys.LAST_LOCATION_Y.getKey(), horse.getLocation().getY());
            nbt.setDouble(Keys.LAST_LOCATION_Z.getKey(), horse.getLocation().getZ());
            nbt.setString(Keys.IS_PUBLIC.getKey(), String.valueOf(isPublic));
            nbt.setString(Keys.IS_IN_HEAT.getKey(), String.valueOf(isInHeat));
            nbt.setString(Keys.IS_PREGNANT.getKey(), String.valueOf(isPregnant));
        });
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
