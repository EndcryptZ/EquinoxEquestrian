package endcrypt.equinox.equine.nbt;

import de.tr7zw.changeme.nbtapi.NBT;
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

    @SuppressWarnings("unchecked")
    public static <T> T readPersistentData(AbstractHorse horse, Keys nbtKey) {
        return NBT.getPersistentData(horse, nbt -> {
            Object value;
            if (nbtKey.getDefaultValue() instanceof String) {
                value = nbt.getString(nbtKey.getKey());
            } else if (nbtKey.getDefaultValue() instanceof Integer) {
                value = nbt.getInteger(nbtKey.getKey());
            } else if (nbtKey.getDefaultValue() instanceof Double) {
                value = nbt.getDouble(nbtKey.getKey());
            } else if (nbtKey.getDefaultValue() instanceof Long) {
                value = nbt.getLong(nbtKey.getKey());
            } else if (nbtKey.getDefaultValue() instanceof Boolean) {
                value = nbt.getBoolean(nbtKey.getKey());
            } else {
                value = null;
            }

            return value != null ? (T) value : (T) nbtKey.getDefaultValue();
        });
    }

    public static <T> void writePersistentData(AbstractHorse horse, Keys nbtKey, T value) {
        NBT.modifyPersistentData(horse, nbt -> {
            if (value instanceof String) {
                nbt.setString(nbtKey.getKey(), (String) value);
            } else if (value instanceof Integer) {
                nbt.setInteger(nbtKey.getKey(), (Integer) value);
            } else if (value instanceof Double) {
                nbt.setDouble(nbtKey.getKey(), (Double) value);
            } else if (value instanceof Long) {
                nbt.setLong(nbtKey.getKey(), (Long) value);
            } else if (value instanceof Boolean) {
                nbt.setBoolean(nbtKey.getKey(), (Boolean) value);
            }
        });
    }

    public static boolean hasPersistentData(AbstractHorse horse, Keys nbtKey) {
        return NBT.getPersistentData(horse, nbt -> nbt.hasTag(nbtKey.getKey()));
    }

}