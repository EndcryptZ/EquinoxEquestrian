package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.attributes.CoatModifier;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class CoatModifierForm {

    private EquinoxEquestrian plugin;
    public CoatModifierForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), coatModifierForm(player, "", equineHorse));
    }

    private CustomForm coatModifierForm(Player player, String errorType, EquineHorse equineHorse) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Coat Modifier");

        if (hasError) {
            form.label("§cYou must select a Coat Modifier");
        }

        form.dropdown("Coat Modifier", CoatModifier.getCoatModifierNames(), equineHorse.getCoatColor().ordinal())
                .validResultHandler(result -> {
                    // if we showed a label, dropdown is at index 1; otherwise at 0
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    CoatModifier chosen = CoatModifier.values()[choiceIndex];
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, CoatModifier coatModifier) {

        equineHorse.setCoatModifier(coatModifier);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
