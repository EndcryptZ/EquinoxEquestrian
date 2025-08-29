package endcrypt.equinox.utils;

import java.util.UUID;

public class UniqueIdUtil {

    public static boolean isValidUUID(String str) {
        if (str == null) return false;
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
