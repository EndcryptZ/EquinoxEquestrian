package endcrypt.equinoxEquestrian.equine.enums;

import org.bukkit.entity.Horse;

public enum CoatModifier {
    NONE("None", null),
    SNIP("Snip", Horse.Style.WHITE),
    PAINT("Paint", Horse.Style.WHITEFIELD),
    HEART("Heart", Horse.Style.WHITE_DOTS),
    BLAZE("Blaze", Horse.Style.BLACK_DOTS);

    private final String coatModifierName;
    private final Horse.Style horseCoatModifier;

    CoatModifier(String coatModifierName, Horse.Style horseCoatModifier){
        this.coatModifierName = coatModifierName;
        this.horseCoatModifier = horseCoatModifier;
    }

    public String getCoatModifierName() {
        return coatModifierName;
    }

    public Horse.Style getHorseCoatModifier() {
        return horseCoatModifier;
    }
}
