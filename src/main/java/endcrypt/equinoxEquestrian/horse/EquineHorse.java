package endcrypt.equinoxEquestrian.horse;

import endcrypt.equinoxEquestrian.horse.enums.*;

public class EquineHorse {

    private String name;
    private Discipline discipline;
    private Breed breed;
    private CoatColor coatColor;
    private CoatModifier coatModifier;
    private Gender gender;
    private int age;
    private Height height;
    private Trait[] traits;

    public EquineHorse(String name, Discipline discipline, Breed breed, CoatColor coatColor, CoatModifier coatModifier, Gender gender, int age, Height height, Trait[] traits) {
        this.name = name;
        this.discipline = discipline;
        this.breed = breed;
        this.coatColor = coatColor;
        this.coatModifier = coatModifier;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.traits = traits;
    }

    public String getName() {
        return name;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public Breed getBreed() {
        return breed;
    }

    public CoatColor getCoatColor() {
        return coatColor;
    }

    public CoatModifier getCoatModifier() {
        return coatModifier;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Height getHeight() {
        return height;
    }

    public Trait[] getTraits() {
        return traits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public void setCoatColor(CoatColor coatColor) {
        this.coatColor = coatColor;
    }

    public void setCoatModifier(CoatModifier coatModifier) {
        this.coatModifier = coatModifier;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public void setTraits(Trait[] traits) {
        this.traits = traits;
    }
}
