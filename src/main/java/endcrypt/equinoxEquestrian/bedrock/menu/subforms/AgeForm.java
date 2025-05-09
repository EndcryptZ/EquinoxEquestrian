package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class AgeForm {

    private EquinoxEquestrian plugin;
    public AgeForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), ageForm(player, equineHorse));
    }

    private CustomForm ageForm(Player player, EquineHorse equineHorse) {
        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Age");

        form.slider("Age", 4, 25, 1, equineHorse.getAge())
                .validResultHandler(result -> {
                    initializeSubmission(player, equineHorse, (int) result.asSlider(0));
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, int age) {

        equineHorse.setAge(age);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
