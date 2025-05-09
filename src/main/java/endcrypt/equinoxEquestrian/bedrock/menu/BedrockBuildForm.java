package endcrypt.equinoxEquestrian.bedrock.menu;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.bedrock.menu.subforms.*;
import endcrypt.equinoxEquestrian.equine.*;
import endcrypt.equinoxEquestrian.player.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BedrockBuildForm {

    private final EquinoxEquestrian plugin;
    private final NameForm nameForm;
    private final DisciplineForm disciplineForm;
    private final BreedForm breedForm;
    private final CoatColorForm coatColorForm;
    private final CoatModifierForm coatModifierForm;
    private final GenderForm genderForm;
    private final AgeForm ageForm;
    private final HeightForm heightForm;
    private final TraitForm traitForm;

    public BedrockBuildForm(EquinoxEquestrian plugin) {

        this.plugin = plugin;
        nameForm = new NameForm(plugin);
        disciplineForm = new DisciplineForm(plugin);
        breedForm = new BreedForm(plugin);
        coatColorForm = new CoatColorForm(plugin);
        coatModifierForm = new CoatModifierForm(plugin);
        genderForm = new GenderForm(plugin);
        ageForm = new AgeForm(plugin);
        heightForm = new HeightForm(plugin);
        traitForm = new TraitForm(plugin);
    }

    public void openDefault(Player player) {

        openDefaultForm(player);
    }

    public void openDefaultForm(Player player) {

        String name = "";
        Discipline discipline = Discipline.NONE;
        Breed breed = Breed.NONE;
        CoatColor coatColor = CoatColor.NONE;
        CoatModifier coatModifier = CoatModifier.NONE;
        Gender gender = Gender.NONE;
        Height height = null;
        int age = 4;

        for (Height loopedHeight : Height.values()) {
            if (loopedHeight.getHands() == breed.getMinimumHands()) {
                height = loopedHeight;
                break;
            }
        }

        Trait[] traits = {Trait.NONE, Trait.NONE, Trait.NONE};

        EquineHorse equineHorse = new EquineHorse(name, discipline, breed, coatColor, coatModifier, gender, age, height, traits);

        plugin.getFloodgateApi().sendForm(player.getUniqueId(), mainForm(player, "", equineHorse));
    }

    public void openMainFormWithParameters(Player player, EquineHorse equineHorse) {

        plugin.getFloodgateApi().sendForm(player.getUniqueId(), mainForm(player, "", equineHorse));
    }

    private CustomForm mainForm(Player player, String errorType, EquineHorse equineHorse) {

        CustomForm.Builder form = CustomForm.builder().title("Build a Horse");
        boolean hasErrorLabel = !errorType.isEmpty();

        if (hasErrorLabel) {
            form.label(errorType);
        }

        form.label("§6Name: §f" + equineHorse.getName() +
                        "\n§6Discipline: §f" + equineHorse.getDiscipline().getDisciplineName() +
                        "\n§6Breed: §f" + equineHorse.getBreed().getName() +
                        "\n§6Coat Color: §f" + equineHorse.getCoatColor().getCoatColorName() +
                        "\n§6Coat Modifier: §f" + equineHorse.getCoatModifier().getCoatModifierName() +
                        "\n§6Gender: §f" + equineHorse.getGender().getGenderName() +
                        "\n§6Age: §f" + equineHorse.getAge() +
                        "\n§6Height: §f" + equineHorse.getHeight().getHandsString() +
                        "\n§6Trait 1: §f" + equineHorse.getTraits()[0].getTraitName() +
                        "\n§6Trait 2: §f" + equineHorse.getTraits()[1].getTraitName() +
                        "\n§6Trait 3: §f" + equineHorse.getTraits()[2].getTraitName() +
                        "\n\n§6Your Tokens: §e" + plugin.getPlayerDataManager().getPlayerData(player).getTokens() +
                        "\n§6Your Balance: §a$" + plugin.getEcon().getBalance(player) +
                        "\n§eCost: §a$" + calculateCost(equineHorse))
                .dropdown("Select to Edit", "Name", "Discipline", "Breed", "Coat Color", "Coat Modifier", "Gender", "Age", "Height", "Trait 1", "Trait 2", "Trait 3")
                .toggle("Create Horse (Submitting while this is on will create the horse)", false)
                .validResultHandler(result -> {
                    int dropdownIndex = hasErrorLabel ? 2 : 1;
                    int toggleIndex = hasErrorLabel ? 3 : 2;
                    initializeSubmission(player, result.asDropdown(dropdownIndex), result.asToggle(toggleIndex), equineHorse);
                });

        return form.build();
    }


    private void initializeSubmission(Player player, int toEdit, boolean isCreatingHorse, EquineHorse equineHorse) {
        if (isCreatingHorse) {
            List<String> missingAttributes = getStrings(equineHorse);

            if (!missingAttributes.isEmpty()) {
                String message = "§cMissing: " + String.join(", ", missingAttributes);
                plugin.getFloodgateApi().sendForm(player.getUniqueId(), mainForm(player, message, equineHorse));
                return;
            }

            double price = calculateCost(equineHorse);

            double balance = plugin.getEcon().getBalance(player);
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);

            if (data.getTokens() < 1) {
                if (balance < price) {
                    String message = String.format("§cInsufficient funds\n§7Your balance: §a$§f%.2f\n§7Cost: §a$§f%.2f", balance, price);
                    plugin.getFloodgateApi().sendForm(player.getUniqueId(), mainForm(player, message, equineHorse));
                    return;
                }
                plugin.getEcon().withdrawPlayer(player, price);
            } else {
                data.setTokens(data.getTokens() - 1);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7You have used a token to create this horse."));
            }

            new EquineHorseBuilder(plugin).spawnHorse(player, equineHorse);
            return;
        }

        switch (toEdit) {
            case 0 -> nameForm.openForm(player, equineHorse);
            case 1 -> disciplineForm.openForm(player, equineHorse);
            case 2 -> breedForm.openForm(player, equineHorse);
            case 3 -> coatColorForm.openForm(player, equineHorse);
            case 4 -> coatModifierForm.openForm(player, equineHorse);
            case 5 -> genderForm.openForm(player, equineHorse);
            case 6 -> ageForm.openForm(player, equineHorse);
            case 7 -> heightForm.openForm(player, equineHorse);
            case 8, 9, 10 -> traitForm.openForm(player, equineHorse, toEdit - 8);
        }
    }

    private static @NotNull List<String> getStrings(EquineHorse equineHorse) {
        List<String> missingAttributes = new ArrayList<>();

        if (equineHorse.getName().isEmpty()) missingAttributes.add("Name");
        if (equineHorse.getDiscipline() == Discipline.NONE) missingAttributes.add("Discipline");
        if (equineHorse.getBreed() == Breed.NONE) missingAttributes.add("Breed");
        if (equineHorse.getCoatColor() == CoatColor.NONE) missingAttributes.add("Coat Color");
        if (equineHorse.getGender() == Gender.NONE) missingAttributes.add("Gender");
        if (Arrays.asList(equineHorse.getTraits()).contains(Trait.NONE)) missingAttributes.add("Trait(s)");
        return missingAttributes;
    }


    private double calculateCost(EquineHorse equineHorse) {
        int namePrice = 1000;
        int disciplinePrice = equineHorse.getDiscipline().getPrice();
        int coatColorPrice = 1000;
        int coatStylePrice = 1000;
        int genderPrice = equineHorse.getGender().getPrice();
        int agePrice = 1000;
        int heightPrice = 1000;
        int traitsPrice = equineHorse.getTraits()[0].getPrice() + equineHorse.getTraits()[1].getPrice() + equineHorse.getTraits()[2].getPrice();

        return namePrice + disciplinePrice + coatColorPrice + coatStylePrice + genderPrice + agePrice + heightPrice + traitsPrice;
    }
}
