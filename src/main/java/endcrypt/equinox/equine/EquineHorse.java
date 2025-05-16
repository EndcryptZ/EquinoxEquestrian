package endcrypt.equinox.equine;

import endcrypt.equinox.equine.attributes.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class EquineHorse {

        private String name;
        private Discipline discipline;
        private Breed[] breeds;
        private Breed prominentBreed;
        private CoatColor coatColor;
        private CoatModifier coatModifier;
        private Gender gender;
        private int age;
        private Height height;
        private Trait[] traits;

        private UUID uuid;

        public EquineHorse(String name, Discipline discipline, Breed[] breeds, CoatColor coatColor, CoatModifier coatModifier, Gender gender, int age, Height height, Trait[] traits) {
            this.name = name;
            this.discipline = discipline;
            this.breeds = breeds;
            this.coatColor = coatColor;
            this.coatModifier = coatModifier;
            this.gender = gender;
            this.age = age;
            this.height = height;
            this.traits = traits;
        }
    }
