package endcrypt.equinox.equine.gaits;

import lombok.Getter;

@Getter
public enum Gaits {
    WALK("Walk", 1),
    TROT("Trot", 2),
    CANTER("Canter", 3),
    GALLOP("Gallop", 4);

    private final String name;
    private final double speedMultiplier;

    Gaits(String name, double speedMultiplier) {
        this.name = name;
        this.speedMultiplier = speedMultiplier;
    }

    // Get the next height (or null if none exists)
    public static Gaits getNextGait(Gaits gait) {
        Gaits[] gaits = Gaits.values();
        for (int i = 0; i < gaits.length - 1; i++) {
            if (gaits[i] == gait) {
                return gaits[i + 1]; // Return the previous gait
            }
        }
        return null; // No next height (if it's the last one)
    }


    // Get the previous gait (or null if none exists)
    public static Gaits getPreviousGait(Gaits gait) {
        Gaits[] gaits = Gaits.values();
        for (int i = 1; i < gaits.length; i++) {
            if (gaits[i] == gait) {
                return gaits[i - 1]; // Return the previous gait
            }
        }
        return null; // No previous gait (if it's the first one)
    }
}
