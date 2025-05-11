package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;

import java.util.UUID;

public class EquineLiveHorse {


    // Attributes
    private String name;
    private Discipline discipline;
    private Breed breed;
    private CoatColor coatColor;
    private CoatModifier coatModifier;
    private Gender gender;
    private int age;
    private Height height;
    private Trait[] traits;

    // Others
    private UUID uuid;
    private long claimTime;
    private long birthTime;
    private String ownerName;
    private UUID ownerUUID;
    private double baseSpeed;
    private double baseJumpPower;
    private String skullId;


    public EquineLiveHorse(AbstractHorse horse) {
        EquineHorse equineHorse = EquineUtils.fromAbstractHorse(horse);

        this.name = horse.getName();
        this.discipline = equineHorse.getDiscipline();
        this.breed = equineHorse.getBreed();
        this.coatColor = equineHorse.getCoatColor();
        this.coatModifier = equineHorse.getCoatModifier();
        this.gender = equineHorse.getGender();
        this.age = equineHorse.getAge();
        this.height = equineHorse.getHeight();
        this.traits = equineHorse.getTraits();

        this.uuid = horse.getUniqueId();

        NBT.getPersistentData(horse, nbt -> this.claimTime = nbt.getLong("EQUINE_CLAIM_TIME"));
        NBT.getPersistentData(horse, nbt -> this.birthTime = nbt.getLong("EQUINE_BIRTH_TIME"));
        NBT.getPersistentData(horse, nbt -> this.ownerName = nbt.getString("EQUINE_OWNER_NAME"));
        NBT.getPersistentData(horse, nbt -> this.ownerUUID = nbt.getUUID("EQUINE_OWNER_UUID"));
        NBT.getPersistentData(horse, nbt -> this.baseSpeed = nbt.getDouble("EQUINE_BASE_SPEED"));
        NBT.getPersistentData(horse, nbt -> this.baseJumpPower = nbt.getDouble("EQUINE_BASE_JUMP_POWER"));
        NBT.getPersistentData(horse, nbt -> this.skullId = nbt.getString("EQUINE_SKULL_ID"));
    }

    public void update(AbstractHorse horse) {

        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setLong("EQUINE_CLAIM_TIME", this.claimTime);
            nbt.setLong("EQUINE_BIRTH_TIME", this.birthTime);
            nbt.setString("EQUINE_OWNER_NAME", this.ownerName);
            nbt.setUUID("EQUINE_OWNER_UUID", this.ownerUUID);
            nbt.setDouble("EQUINE_BASE_SPEED", this.baseSpeed);
            nbt.setDouble("EQUINE_BASE_JUMP_POWER", this.baseJumpPower);
            nbt.setString("EQUINE_SKULL_ID", this.skullId);
        });
    }

    public void updateDefault(AbstractHorse horse) {
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
                    Bukkit.broadcast(ColorUtils.color(tag + " was missing and has been set to default."));
                }
            }
        });
    }

    public String getName() {
        return name;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Breed getBreed() {
        return breed;
    }

    public CoatColor getCoatColor() {
        return coatColor;
    }

    public CoatModifier getCoatModifier() {
        return coatModifier;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Height getHeight() {
        return height;
    }

    public Trait[] getTraits() {
        return traits;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getClaimTime() {
        return claimTime;
    }

    public double getBaseJumpPower() {
        return baseJumpPower;
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public String getSkullId() {
        return skullId;
    }

    public void setSkullId(String skullId) {
        this.skullId = skullId;
    }
}
