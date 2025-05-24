package endcrypt.equinox.equine.nbt;

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
    IS_CROSS_TIED("EQUINE_IS_CROSS_TIED", "false");

    public final String key;
    public final Object defaultValue;

    Keys(String key, Object defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}