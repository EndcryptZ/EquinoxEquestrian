package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.Breed;
import endcrypt.equinoxEquestrian.equine.Height;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class BreedForm {

    private EquinoxEquestrian plugin;

    public BreedForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), breedForm(player, "", equineHorse));
    }

    private CustomForm breedForm(Player player, String errorType, EquineHorse equineHorse) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Breed");

        if (hasError) {
            form.label("§cYou must select a Breed");
        }

        form.dropdown("Breed", Breed.getBreedNames(), equineHorse.getBreed().ordinal())
                .validResultHandler(result -> {
                    // if we showed a label, dropdown is at index 1; otherwise at 0
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    Breed chosen = Breed.values()[choiceIndex];
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Breed breed) {

        if (breed == Breed.NONE) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), breedForm(player, "None Error", equineHorse));
            return;
        }

        equineHorse.setBreed(breed);

        double minHands = breed.getMinimumHands();
        double maxHands = breed.getMaximumHands();

        Height currentHeight = equineHorse.getHeight();
        double currentHands = currentHeight.getHands();

        if (currentHands < minHands) {
            equineHorse.setHeight(Height.getByHands(minHands));
        } else if (currentHands > maxHands) {
            equineHorse.setHeight(Height.getByHands(maxHands));
        }

        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
