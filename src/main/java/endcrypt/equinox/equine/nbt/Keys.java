package endcrypt.equinox.equine.nbt;

import de.tr7zw.changeme.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.entity.AbstractHorse;

@Getter
public enum Keys {

    CLAIM_TIME("EQUINE_CLAIM_TIME", System.currentTimeMillis()),
    SKULL_ID("EQUINE_SKULL_ID", "49654"),
    IS_EQUINE("EQUINE_HORSE", "true"),
    DISCIPLINE("EQUINE_DISCIPLINE", ""),
    BREED_PREFIX("EQUINE_BREED_", ""),
    PROMINENT_BREED("EQUINE_PROMINENT_BREED", ""),
    GENDER("EQUINE_GENDER", ""),
    AGE("EQUINE_AGE", 0),
    HEIGHT("EQUINE_HEIGHT", 0.0),
    TRAIT_PREFIX("EQUINE_TRAIT_", ""),
    BIRTH_TIME("EQUINE_BIRTH_TIME", System.currentTimeMillis()),
    OWNER_UUID("EQUINE_OWNER_UUID", ""),
    OWNER_NAME("EQUINE_OWNER_NAME", ""),    
    BASE_SPEED("EQUINE_BASE_SPEED", 0.0),
    BASE_JUMP("EQUINE_BASE_JUMP_POWER", 0.0),
    IS_CROSS_TIED("EQUINE_IS_CROSS_TIED", "false"),
    IS_GROOM_ITEM("EQUINE_GROOM_ITEM", "false"),
    IS_LUNGING("EQUINE_IS_LUNGING", "false"),
    COAT_COLOR("EQUINE_COAT_COLOR", ""),
    COAT_MODIFIER("EQUINE_COAT_MODIFIER", ""),
    LAST_WORLD("EQUINE_LAST_WORLD", ""),
    LAST_LOCATION_X("EQUINE_LAST_LOCATION_X", 0.0),
    LAST_LOCATION_Y("EQUINE_LAST_LOCATION_Y", 0.0),
    LAST_LOCATION_Z("EQUINE_LAST_LOCATION_Z", 0.0),
    IS_PUBLIC("EQUINE_IS_PUBLIC", "false"),

    // Breeding & Pregnancy
    IS_IN_HEAT("EQUINE_IS_IN_HEAT", "false"),
    LAST_IN_HEAT("EQUINE_LAST_IN_HEAT", System.currentTimeMillis()),

    IS_PREGNANT("EQUINE_IS_PREGNANT", "false"),
    PREGNANCY_START_TIME("EQUINE_PREGNANCY_START", System.currentTimeMillis()),
    PREGNANCY_PARTNER("EQUINE_PREGNANCY_PARTNER", ""),
    INSTANT_FOAL("EQUINE_INSTANT_FOAL", "false"),

    IS_BREEDING("EQUINE_IS_BREEDING", "false"),
    BREEDING_START_TIME("EQUINE_BREEDING_START", System.currentTimeMillis()),
    BREEDING_PARTNER("EQUINE_BREEDING_PARTNER", ""),
    INSTANT_BREED("EQUINE_INSTANT_BREED", "false"),

    // Waste
    LAST_PEE("EQUINE_LAST_PEE", System.currentTimeMillis()),
    LAST_POOP("EQUINE_LAST_POOP", System.currentTimeMillis()),

    // Hunger
    HUNGER_PERCENTAGE("EQUINE_HUNGER_PERCENTAGE", 100.0),
    LAST_HUNGER_UPDATE("EQUINE_LAST_HUNGER_UPDATE", System.currentTimeMillis()),

    // Thirst
    THIRST_PERCENTAGE("EQUINE_THIRST_PERCENTAGE", 100.0),
    LAST_THIRST_UPDATE("EQUINE_LAST_THIRST_UPDATE", System.currentTimeMillis());


    public final String key;
    public final Object defaultValue;

    Keys(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
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

}