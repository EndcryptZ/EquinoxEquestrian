package endcrypt.equinox.equine.nbt;

public enum Keys {

    EQUINE_CLAIM_TIME("EQUINE_CLAIM_TIME", System.currentTimeMillis()),
    EQUINE_SKULL_ID("EQUINE_SKULL_ID", "49654");

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
