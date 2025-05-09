package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.Trait;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class TraitForm {


    private EquinoxEquestrian plugin;
    public TraitForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse, int index) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), traitForm(player, "", equineHorse, index));
    }

    private CustomForm traitForm(Player player, String errorType, EquineHorse equineHorse, int index) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Trait " + (index + 1));

        if (hasError) {
            form.label("§cYou must select a Trait");
        }

        List<String> traitNames = new ArrayList<>();
        List<Trait> traits = new ArrayList<>();
        int selectedIndex = 0;

        Trait[] currentTraits = equineHorse.getTraits();

        for (Trait trait : Trait.values()) {
            if (trait == Trait.NONE) {
                traitNames.add(trait.getTraitName());
                traits.add(trait);
                if (currentTraits[index] == trait) {
                    selectedIndex = traitNames.size() - 1;
                }
                continue;
            }

            boolean alreadyUsed = false;
            for (int i = 0; i < currentTraits.length; i++) {
                if (i != index && currentTraits[i] == trait) {
                    alreadyUsed = true;
                    break;
                }
            }

            if (!alreadyUsed) {
                traitNames.add(trait.getTraitName());
                traits.add(trait);
                if (currentTraits[index] == trait) {
                    selectedIndex = traitNames.size() - 1;
                }
            }
        }

        form.dropdown("Trait", traitNames, selectedIndex)
                .validResultHandler(result -> {
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    Trait chosen = traits.get(choiceIndex);
                    initializeSubmission(player, equineHorse, chosen, index);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Trait trait, int index) {

        if(trait == Trait.NONE) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), traitForm(player, "None Error", equineHorse, index));
            return;
        }

        Trait[] traits = equineHorse.getTraits();
        traits[index] = trait;
        equineHorse.setTraits(traits);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
