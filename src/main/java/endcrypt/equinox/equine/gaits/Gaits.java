package endcrypt.equinox.equine.gaits;

import endcrypt.equinox.equine.nbt.Keys;
import lombok.Getter;

@Getter
public enum Gaits {
    WALK("Walk", Keys.WALK_SPEED),
    TROT("Trot", Keys.TROT_SPEED),
    CANTER("Canter", Keys.CANTER_SPEED),
    GALLOP("Gallop", Keys.GALLOP_SPEED);

    private final String name;
    private final Keys gaitSpeedKey;

    Gaits(String name, Keys gaitSpeedKey) {
        this.name = name;
        this.gaitSpeedKey = gaitSpeedKey;
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
