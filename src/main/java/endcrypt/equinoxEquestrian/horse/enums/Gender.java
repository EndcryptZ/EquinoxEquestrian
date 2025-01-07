package endcrypt.equinoxEquestrian.horse.enums;

public enum Gender {
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
}
