package endcrypt.equinox.utils;

public class TimeUtils {

    public static long daysToMillis(int days) {
        return (long) days * 24 * 60 * 60 * 1000;
    }

    public static long secondsToMillis(int seconds) {
        return (long) seconds * 1000;
    }

    public static long minutesToMillis(int minutes) {
        return (long) minutes * 60 * 1000;
    }
}
