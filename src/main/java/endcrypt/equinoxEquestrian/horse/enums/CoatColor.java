package endcrypt.equinoxEquestrian.horse.enums;

import org.bukkit.entity.Horse;

import java.util.ArrayList;
import java.util.List;

public enum CoatColor {
    NONE("None", null),
    WHITE("White", Horse.Color.WHITE),       // Minecraft equivalent: White horse
    PALOMINO("Palomino", Horse.Color.CREAMY),   // Closest to the creamy horse in Minecraft
    CHESTNUT("Chestnut", Horse.Color.CHESTNUT), // Minecraft has a Chestnut color variant
    BUCKSKIN("Buckskin", Horse.Color.BROWN),   // Closest match: Creamy, no specific Buckskin color in Minecraft
    BLACK("Black", Horse.Color.BLACK),       // Minecraft equivalent: Black horse
    GRAY("Gray", Horse.Color.GRAY),         // Minecraft equivalent: Gray horse
    BAY("Bay", Horse.Color.DARK_BROWN);         // Minecraft closest equivalent: Brown horse (could also be close to Dark Brown or Bay in real life)

    private final String coatColorName;
    private final Horse.Color horseColor;

     CoatColor(String coatColorName, Horse.Color horseColor) {
         this.coatColorName = coatColorName;
         this.horseColor = horseColor;
    }

    public String getCoatColorName() {
        return coatColorName;
    }

    public Horse.Color getHorseColor() {
        return horseColor;
    }

    public static List<String> getCoatColorNames() {
        List<String> names = new ArrayList<>();
        for (CoatColor coatColor : CoatColor.values()) {
            names.add(coatColor.getCoatColorName());
        }
        return names;
    }

    public static CoatColor getCoatColorFromHorseColor(Horse.Color color) {
        for (CoatColor cc : CoatColor.values()) {
            if (cc.getHorseColor() == color) {
                return cc;
            }
        }
        return CoatColor.NONE;
    }
}