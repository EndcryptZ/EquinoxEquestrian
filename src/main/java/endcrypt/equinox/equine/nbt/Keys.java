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

    IS_BREEDING("EQUINE_IS_BREEDING", "false"),
    BREEDING_START_TIME("EQUINE_BREEDING_START", System.currentTimeMillis()),
    BREEDING_PARTNER("EQUINE_BREEDING_PARTNER", "");

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

}