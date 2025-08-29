package endcrypt.equinox.equine.nbt;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.attributes.Gender;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

@Getter
public enum Keys {

    CLAIM_TIME("EQUINE_CLAIM_TIME", System.currentTimeMillis(), Long.class),
    SKULL_ID("EQUINE_SKULL_ID", "49654", String.class),
    IS_EQUINE("EQUINE_HORSE", "true", Boolean.class),
    DISCIPLINE("EQUINE_DISCIPLINE", "", String.class),
    BREED_PREFIX("EQUINE_BREED_", "", String.class),
    PROMINENT_BREED("EQUINE_PROMINENT_BREED", "", String.class),
    GENDER("EQUINE_GENDER", "", String.class),
    AGE("EQUINE_AGE", 0, Integer.class),
    HEIGHT("EQUINE_HEIGHT", 0.0, Double.class),
    TRAIT_PREFIX("EQUINE_TRAIT_", "", String.class),
    BIRTH_TIME("EQUINE_BIRTH_TIME", System.currentTimeMillis(), Long.class),
    OWNER_UUID("EQUINE_OWNER_UUID", "", String.class),
    OWNER_NAME("EQUINE_OWNER_NAME", "", String.class),
    BASE_SPEED("EQUINE_BASE_SPEED", 0.0, Double.class),
    BASE_JUMP("EQUINE_BASE_JUMP_POWER", 0.0, Double.class),
    IS_CROSS_TIED("EQUINE_IS_CROSS_TIED", "false", Boolean.class),
    IS_GROOM_ITEM("EQUINE_GROOM_ITEM", "false", Boolean.class),
    IS_LUNGING("EQUINE_IS_LUNGING", "false", Boolean.class),
    COAT_COLOR("EQUINE_COAT_COLOR", "", String.class),
    COAT_MODIFIER("EQUINE_COAT_MODIFIER", "", String.class),
    LAST_WORLD("EQUINE_LAST_WORLD", "", String.class),
    LAST_LOCATION_X("EQUINE_LAST_LOCATION_X", 0.0, Double.class),
    LAST_LOCATION_Y("EQUINE_LAST_LOCATION_Y", 0.0, Double.class),
    LAST_LOCATION_Z("EQUINE_LAST_LOCATION_Z", 0.0, Double.class),
    IS_PUBLIC("EQUINE_IS_PUBLIC", "false", Boolean.class),

    // Breeding & Pregnancy
    IS_IN_HEAT("EQUINE_IS_IN_HEAT", "false", Boolean.class),
    LAST_IN_HEAT("EQUINE_LAST_IN_HEAT", System.currentTimeMillis(), Long.class),

    IS_PREGNANT("EQUINE_IS_PREGNANT", "false", Boolean.class),
    PREGNANCY_START_TIME("EQUINE_PREGNANCY_START", System.currentTimeMillis(), Long.class),
    PREGNANCY_PARTNER("EQUINE_PREGNANCY_PARTNER", "", String.class),
    INSTANT_FOAL("EQUINE_INSTANT_FOAL", "false", Boolean.class),

    IS_BREEDING("EQUINE_IS_BREEDING", "false", Boolean.class),
    BREEDING_START_TIME("EQUINE_BREEDING_START", System.currentTimeMillis(), Long.class),
    BREEDING_PARTNER("EQUINE_BREEDING_PARTNER", "", String.class),
    INSTANT_BREED("EQUINE_INSTANT_BREED", "false", Boolean.class),

    // Waste
    LAST_PEE("EQUINE_LAST_PEE", System.currentTimeMillis(), Long.class),
    LAST_POOP("EQUINE_LAST_POOP", System.currentTimeMillis(), Long.class),

    // Hunger
    HUNGER_PERCENTAGE("EQUINE_HUNGER_PERCENTAGE", 100.0, Double.class),
    LAST_HUNGER_UPDATE("EQUINE_LAST_HUNGER_UPDATE", System.currentTimeMillis(), Long.class),
    LAST_SEEK_FOOD("EQUINE_LAST_SEEK_FOOD", System.currentTimeMillis(), Long.class),
    IS_IN_FOOD_TASK("EQUINE_IS_IN_FOOD_TASK", false, Boolean.class),

    // Thirst
    THIRST_PERCENTAGE("EQUINE_THIRST_PERCENTAGE", 100.0, Double.class),
    LAST_THIRST_UPDATE("EQUINE_LAST_THIRST_UPDATE", System.currentTimeMillis(), Long.class),
    LAST_SEEK_WATER("EQUINE_LAST_SEEK_WATER", System.currentTimeMillis(), Long.class),
    IS_IN_WATER_TASK("EQUINE_IS_IN_WATER_TASK", false, Boolean.class),

    // Speed
    WALK_SPEED("EQUINE_WALK_SPEED", 0.0, Double.class),
    TROT_SPEED("EQUINE_TROT_SPEED", 0.0, Double.class),
    CANTER_SPEED("EQUINE_CANTER_SPEED", 0.0, Double.class),
    GALLOP_SPEED("EQUINE_GALLOP_SPEED", 0.0, Double.class);

    public final String key;
    public final Object defaultValue;
    public final Class<?> type;

    Keys(String key, Object defaultValue, Class<?> type) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public static String readString(AbstractHorse horse, Keys key) {
        return NBT.getPersistentData(horse, nbt -> nbt.getString(key.getKey()));
    }

    public static void writeString(AbstractHorse horse, Keys key, String value) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setString(key.getKey(), value);
        });
    }

    public static int readInt(AbstractHorse horse, Keys key) {
        return NBT.getPersistentData(horse, nbt -> nbt.getInteger(key.getKey()));
    }

    public static void writeInt(AbstractHorse horse, Keys key, int value) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setInteger(key.getKey(), value);
        });
    }

    public static double readDouble(AbstractHorse horse, Keys key) {
        return NBT.getPersistentData(horse, nbt -> nbt.getDouble(key.getKey()));
    }

    public static void writeDouble(AbstractHorse horse, Keys key, double value) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setDouble(key.getKey(), value);
        });
    }

    public static long readLong(AbstractHorse horse, Keys key) {
        return NBT.getPersistentData(horse, nbt -> nbt.getLong(key.getKey()));
    }

    public static void writeLong(AbstractHorse horse, Keys key, long value) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setLong(key.getKey(), value);
        });
    }

    public static boolean readBoolean(AbstractHorse horse, Keys key) {
        return NBT.getPersistentData(horse, nbt -> nbt.getBoolean(key.getKey()));
    }

    public static void writeBoolean(AbstractHorse horse, Keys key, boolean value) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setBoolean(key.getKey(), value);
        });
    }

    public static Gender readGender(AbstractHorse horse) {
        return Gender.getGenderByName(NBT.getPersistentData(horse, nbt -> nbt.getString(GENDER.getKey())));
    }

    public static boolean hasPersistentData(AbstractHorse horse, Keys nbtKey) {
        return NBT.getPersistentData(horse, nbt -> nbt.hasTag(nbtKey.getKey()));
    }

}