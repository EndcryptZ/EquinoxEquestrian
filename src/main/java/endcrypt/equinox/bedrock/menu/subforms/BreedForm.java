package endcrypt.equinox.bedrock.menu.subforms;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.Breed;
import endcrypt.equinox.equine.attributes.Height;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class BreedForm {

    private final EquinoxEquestrian plugin;

    public BreedForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse, int index) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), breedForm(player, "", equineHorse, index));
    }

    private CustomForm breedForm(Player player, String errorType, EquineHorse equineHorse, int index) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Breed " + (index + 1));

        if (hasError) {
            form.label("§cYou must select a valid Breed");
        }

        List<Breed> currentBreeds = equineHorse.getBreeds();
        Breed otherBreed = currentBreeds.get(index == 0 ? 1 : 0);

        List<String> breedNames = new ArrayList<>();
        List<Breed> breeds = new ArrayList<>();
        int selectedIndex = 0;

        for (Breed breed : Breed.values()) {
            if (index == 1 && breed == Breed.NONE) {
                // Allow NONE only for Breed 2 if you want
                breedNames.add(breed.getName());
                breeds.add(breed);
                if (currentBreeds.get(index) == breed) {
                    selectedIndex = breedNames.size() - 1;
                }
                continue;
            }

            if (breed != Breed.NONE && breed != otherBreed) {
                breedNames.add(breed.getName());
                breeds.add(breed);
                if (currentBreeds.get(index) == breed) {
                    selectedIndex = breedNames.size() - 1;
                }
            }
        }

        form.dropdown("Breed", breedNames, selectedIndex)
                .validResultHandler(result -> {
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    Breed chosen = breeds.get(choiceIndex);
                    initializeSubmission(player, equineHorse, chosen, index);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Breed breed, int index) {

        if (breed == Breed.NONE && index == 0) {
            // Breed 1 should never be NONE
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), breedForm(player, "None Error", equineHorse, index));
            return;
        }

        List<Breed> breeds = equineHorse.getBreeds();
        breeds.set(index, breed);
        equineHorse.setBreeds(breeds);

        // Adjust height only if we change Breed 1
        if (index == 0) {
            double minHands = breed.getMinimumHands();
            double maxHands = breed.getMaximumHands();

            Height currentHeight = equineHorse.getHeight();
            double currentHands = currentHeight.getHands();

            if (currentHands < minHands) {
                equineHorse.setHeight(Height.getByHands(minHands));
            } else if (currentHands > maxHands) {
                equineHorse.setHeight(Height.getByHands(maxHands));
            }
        }

        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
