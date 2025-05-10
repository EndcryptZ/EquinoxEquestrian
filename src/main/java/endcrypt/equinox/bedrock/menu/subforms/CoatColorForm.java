package endcrypt.equinox.bedrock.menu.subforms;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.CoatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class CoatColorForm {

    private EquinoxEquestrian plugin;
    public CoatColorForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), coatColorForm(player, "", equineHorse));
    }

    private CustomForm coatColorForm(Player player, String errorType, EquineHorse equineHorse) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Coat Color");

        if (hasError) {
            form.label("§cYou must select a Coat Color");
        }

        form.dropdown("Coat Color", CoatColor.getCoatColorNames(), equineHorse.getCoatColor().ordinal())
                .validResultHandler(result -> {
                    // if we showed a label, dropdown is at index 1; otherwise at 0
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    CoatColor chosen = CoatColor.values()[choiceIndex];
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, CoatColor coatColor) {

        if(coatColor == CoatColor.NONE) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), coatColorForm(player, "None Error", equineHorse));
            return;
        }

        equineHorse.setCoatColor(coatColor);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
