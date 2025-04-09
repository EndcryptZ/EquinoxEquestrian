package endcrypt.equinoxEquestrian.horse.enums;

public enum Discipline {
    NONE("None", 0),

    ALL_ROUND("All Round", 1000),
    BARREL_RACING("Barrel Racing", 1000),
    CLASSICAL_DRESSAGE("Classical Dressage", 1000),
    DRESSAGE("Dressage", 1000),
    ENDURANCE("Endurance", 1000),
    EQUITATION("Equitation", 1000),
    EVENTING("Eventing", 1000),
    MOUNTED_ARCHERY("Mounted Archery", 1000),
    IN_HAND("In-Hand", 1000),
    HUNTER("Hunter", 1000),
    FLAT_RACING_LONG("Flat Racing (Long)", 1000),
    FLAT_RACING_SHORT("Flat Racing (Short)", 1000),
    POLE_BENDING("Pole Bending", 1000),
    SHOW_JUMPING("Show Jumping", 3000),  // Special price for Show Jumping
    STEEPLECHASE("Steeplechase", 1000),
    WESTERN_PLEASURE("Western Pleasure", 1000),
    TRAIL_RIDING("Trail Riding", 1000),
    CROSS_COUNTRY("Cross Country", 1000);

    private final String disciplineName;
    private final int price;

    Discipline(String disciplineName, int price) {
        this.disciplineName = disciplineName;
        this.price = price;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public int getPrice() {
        return price;
    }
}
