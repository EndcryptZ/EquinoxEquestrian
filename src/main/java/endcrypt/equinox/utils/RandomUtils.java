package endcrypt.equinox.utils;

import java.util.Random;

public class RandomUtils {

    public static double randomInRange(double min, double max) {
        Random random = new Random();

        double val = min + (max - min) * random.nextDouble();
        return Math.round(val * 100.0) / 100.0;
    }
}
