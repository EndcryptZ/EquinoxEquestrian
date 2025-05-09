package endcrypt.equinoxEquestrian.bedrock.menu.subforms;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.attributes.Discipline;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

public class DisciplineForm {

    private EquinoxEquestrian plugin;
    public DisciplineForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), disciplineForm(player, "", equineHorse));
    }

    private CustomForm disciplineForm(Player player, String errorType, EquineHorse equineHorse) {
        boolean hasError = "None Error".equalsIgnoreCase(errorType);

        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Discipline");

        if (hasError) {
            form.label("§cYou must select a Discipline");
        }

        form.dropdown("Discipline", Discipline.getDisciplineNames(), equineHorse.getDiscipline().ordinal())
                .validResultHandler(result -> {
                    // if we showed a label, dropdown is at index 1; otherwise at 0
                    int choiceIndex = result.asDropdown(hasError ? 1 : 0);
                    Discipline chosen = Discipline.values()[choiceIndex];
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Discipline discipline) {

        if(discipline == Discipline.NONE) {
            plugin.getFloodgateApi().sendForm(player.getUniqueId(), disciplineForm(player, "None Error", equineHorse));
            return;
        }

        equineHorse.setDiscipline(discipline);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }
}
