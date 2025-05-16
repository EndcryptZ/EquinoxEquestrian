package endcrypt.equinox.bedrock.menu.subforms;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineHorse;
import endcrypt.equinox.equine.attributes.Height;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class HeightForm {

    private EquinoxEquestrian plugin;
    public HeightForm(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void openForm(Player player, EquineHorse equineHorse) {
        plugin.getFloodgateApi().sendForm(player.getUniqueId(), heightForm(player, equineHorse));
    }

    private CustomForm heightForm(Player player, EquineHorse equineHorse) {
        CustomForm.Builder form = CustomForm.builder()
                .title("Horse Height");

        Double minHeight = equineHorse.getBreeds()[0].getMinimumHands();
        Double maxHeight = equineHorse.getBreeds()[0].getMaximumHands();

        List<Height> heightOptions = new ArrayList<>();
        List<String> handsOptions = new ArrayList<>();

        int selectedIndex = 0;

        for (Height height : Height.values()) {
            double hands = height.getHands();
            if (hands >= minHeight && hands <= maxHeight) {
                handsOptions.add(height.getHandsString());
                heightOptions.add(height);
                if (equineHorse.getHeight() == height) {
                    selectedIndex = heightOptions.size() - 1;
                }
            }
        }

        form.dropdown("Height", handsOptions, selectedIndex)
                .validResultHandler(result -> {
                    int choiceIndex = result.asDropdown(0);
                    Height chosen = heightOptions.get(choiceIndex);
                    initializeSubmission(player, equineHorse, chosen);
                });

        return form.build();
    }

    private void initializeSubmission(Player player, EquineHorse equineHorse, Height height) {

        equineHorse.setHeight(height);
        plugin.getBedrockBuildForm().openMainFormWithParameters(player, equineHorse);
    }



}
