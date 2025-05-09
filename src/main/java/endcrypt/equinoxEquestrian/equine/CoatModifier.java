package endcrypt.equinoxEquestrian.equine;

import org.bukkit.entity.Horse;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getCoatModifierNames() {
        List<String> names = new ArrayList<>();
        for (CoatModifier coatModifier : CoatModifier.values()) {
            names.add(coatModifier.getCoatModifierName());
        }
        return names;
    }

    public static CoatModifier getCoatModifierFromHorseStyle(Horse.Style style) {
        for (CoatModifier cm : CoatModifier.values()) {
            if (cm.getHorseCoatModifier() == style) {
                return cm;
            }
        }
        return CoatModifier.NONE;
    }
}
