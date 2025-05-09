package endcrypt.equinoxEquestrian.menu.build;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.menu.build.select.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BuildMenuManager {

    private final EquinoxEquestrian plugin;

    private final BuildMenu buildMenu;

    private final DisciplineSelectMenu disciplineSelectMenu;
    private final BreedSelectMenu breedSelectMenu;
    private final CoatColorSelectMenu coatColorSelectMenu;
    private final CoatModifierSelectMenu coatModifierSelectMenu;
    private final GenderSelectMenu genderSelectMenu;
    private  final TraitSelectMenu traitSelectMenu;

    private final Map<Player, EquineHorse> playerEquineHorseInput = new HashMap<>();
    private final Map<Player, EquineHorse> playerEquineSubMenuInput = new HashMap<>();
    private final Map<Player, Double> playerCost = new HashMap<>();

    public BuildMenuManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.buildMenu = new BuildMenu(plugin);
        this.disciplineSelectMenu = new DisciplineSelectMenu(plugin);
        this.breedSelectMenu = new BreedSelectMenu(plugin);
        this.coatColorSelectMenu = new CoatColorSelectMenu(plugin);
        this.coatModifierSelectMenu = new CoatModifierSelectMenu(plugin);
        this.genderSelectMenu = new GenderSelectMenu(plugin);
        this.traitSelectMenu = new TraitSelectMenu(plugin);
    }

    public BuildMenu getBuildMenu() {
        return buildMenu;
    }

    public BreedSelectMenu getBreedSelectMenu() {
        return breedSelectMenu;
    }

    public CoatColorSelectMenu getCoatColorSelectMenu() {
        return coatColorSelectMenu;
    }

    public CoatModifierSelectMenu getCoatModifierSelectMenu() {
        return coatModifierSelectMenu;
    }

    public DisciplineSelectMenu getDisciplineSelectMenu() {
        return disciplineSelectMenu;
    }

    public GenderSelectMenu getGenderSelectMenu() {
        return genderSelectMenu;
    }

    public Map<Player, Double> getPlayerCost() {
        return playerCost;
    }

    public Map<Player, EquineHorse> getPlayerEquineHorseInput() {
        return playerEquineHorseInput;
    }

    public Map<Player, EquineHorse> getPlayerEquineSubMenuInput() {
        return playerEquineSubMenuInput;
    }

    public TraitSelectMenu getTraitSelectMenu() {
        return traitSelectMenu;
    }
}
