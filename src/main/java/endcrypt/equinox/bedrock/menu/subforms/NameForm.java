package endcrypt.equinox.bedrock.menu.subforms;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class NameForm {

    private EquinoxEquestrian plugin;

    public NameForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), nameForm(player, "", equineHorse));
    }

    private CustomForm nameForm(Player player, String errorType, EquineHorse equineHorse) {
        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Name");

        if (errorType.equals("Minimum Error")) {
            form.label("§cName must be at least 3 characters long");
        } else if (errorType.equals("Maximum Error")) {
            form.label("§cName must be at most 16 characters long");
        }

        form.input("Name", "", equineHorse.getName())
                .validResultHandler(result -> {
                    // If there's a label, input is at index 1; otherwise, index 0
                    String input = errorType.isEmpty() ? result.asInput(0) : result.asInput(1);
                    initializeSubmission(player, equineHorse, input);
                });

        return form.build();
    }


    private void initializeSubmission(Player player, EquineHorse equineHorse, String name) {

        if (name.length() < 3) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), nameForm(player, "Minimum Error", equineHorse));
            return;
        }

        if (name.length() > 16) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), nameForm(player, "Maximum Error", equineHorse));
            return;
        }

        equineHorse.setName(name);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
