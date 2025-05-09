package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.Gender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class GenderForm {

    private EquinoxEquestrian plugin;
    public GenderForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), genderForm(player, "", equineHorse));
    }

    private CustomForm genderForm(Player player, String errorType, EquineHorse equineHorse) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Gender");

        if (hasError) {
            form.label("§cYou must select a Gender");
        }

        form.dropdown("Gender", Gender.getGenderNames(), equineHorse.getGender().ordinal())
                .validResultHandler(result -> {
                    // if we showed a label, dropdown is at index 1; otherwise at 0
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    Gender chosen = Gender.values()[choiceIndex];
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Gender gender) {

        if(gender == Gender.NONE) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), genderForm(player, "None Error", equineHorse));
            return;
        }

        equineHorse.setGender(gender);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
