package endcrypt.equinox.menu.horse.internal;

import lombok.Getter;

@Getter
public enum ListOrganizeType {

    DISCIPLINE("Discipline"),
    ALPHABETICAL("Alphabetical"),
    AGE("Age"),
    LEVEL("Level"),
    GENDER("Gender");

    private final String name;

    ListOrganizeType(String name) {
        this.name = name;
    }
}
