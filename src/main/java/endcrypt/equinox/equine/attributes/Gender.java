package endcrypt.equinox.equine.attributes;

import java.util.ArrayList;
import java.util.List;

public enum Gender {
    NONE("None", 0),

    STALLION("Stallion", 2000),
    GELDING("Gelding", 1000),
    MARE("Mare", 3000);

    private final String genderName;
    private final int price;

    Gender(String genderName, int price){
        this.genderName = genderName;
        this.price = price;
    }

    public String getGenderName() {
        return genderName;
    }

    public int getPrice() {
        return price;
    }

    public static List<String> getGenderNames() {
        List<String> names = new ArrayList<>();
        for (Gender gender : Gender.values()) {
            names.add(gender.getGenderName());
        }
        return names;
    }
}
