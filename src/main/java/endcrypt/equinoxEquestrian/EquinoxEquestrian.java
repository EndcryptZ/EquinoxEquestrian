package endcrypt.equinoxEquestrian;

import com.samjakob.spigui.SpiGUI;
import endcrypt.equinoxEquestrian.commands.MainCommand;
import endcrypt.equinoxEquestrian.commands.MainCommandTabCompleter;
import endcrypt.equinoxEquestrian.menu.build.BuildAHorseMenu;
import endcrypt.equinoxEquestrian.menu.build.select.BreedSelectMenu;
import endcrypt.equinoxEquestrian.menu.build.select.DisciplineSelectMenu;
import org.bukkit.plugin.java.JavaPlugin;

public final class EquinoxEquestrian extends JavaPlugin {

    private SpiGUI spiGUI;

    private BuildAHorseMenu buildAHorseMenu;
    private DisciplineSelectMenu disciplineSelectMenu;
    private BreedSelectMenu breedSelectMenu;


    @Override
    public void onEnable() {

        spiGUI = new SpiGUI(this);

        buildAHorseMenu = new BuildAHorseMenu(this);
        disciplineSelectMenu = new DisciplineSelectMenu(this);
        breedSelectMenu = new BreedSelectMenu(this);


        getServer().getPluginCommand("eq").setExecutor(new MainCommand(this));
        getServer().getPluginCommand("eq").setTabCompleter(new MainCommandTabCompleter());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BuildAHorseMenu getBuildAHorseMenu() {
        return buildAHorseMenu;
    }

    public DisciplineSelectMenu getDisciplineSelectMenu() {
        return disciplineSelectMenu;
    }

    public BreedSelectMenu getBreedSelectMenu() {
        return breedSelectMenu;
    }

    public SpiGUI getSpiGUI() {
        return spiGUI;
    }

    public String getPrefix() {
        return "§8[§eEquinox§8] ";
    }
}
