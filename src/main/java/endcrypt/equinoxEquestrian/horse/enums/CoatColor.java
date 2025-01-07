package endcrypt.equinoxEquestrian.horse.enums;

import org.bukkit.entity.Horse;

public enum CoatColor {
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
}