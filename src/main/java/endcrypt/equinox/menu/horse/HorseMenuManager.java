package endcrypt.equinox.menu.horse;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.menu.horse.internal.HorseInfoMenu;
import endcrypt.equinox.menu.horse.internal.HorseListMenu;
import endcrypt.equinox.menu.horse.internal.ListOrganizerMenu;
import endcrypt.equinox.menu.horse.submenus.*;

public class HorseMenuManager {

    private final EquinoxEquestrian plugin;
    private final HorseMenu horseMenu;
    private final InventoryHorseMenu inventoryHorseMenu;
    private final HomeMenu homeMenu;
    private final GroomMenu groomMenu;
    private final HealthMenu healthMenu;
    private final AutoVetMenu autoVetMenu;
    private final HorseListMenu horseListMenu;
    private final HorseInfoMenu horseInfoMenu;
    private final ListOrganizerMenu listOrganizerMenu;

    public HorseMenuManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        horseMenu = new HorseMenu(plugin);
        inventoryHorseMenu = new InventoryHorseMenu(plugin);
        homeMenu = new HomeMenu(plugin);
        groomMenu = new GroomMenu(plugin);
        healthMenu = new HealthMenu(plugin);
        autoVetMenu = new AutoVetMenu(plugin);

        horseListMenu = new HorseListMenu(plugin);
        horseInfoMenu = new HorseInfoMenu(plugin);
        listOrganizerMenu = new ListOrganizerMenu(plugin);
    }

    public HorseMenu getHorseMenu() {
        return horseMenu;
    }

    public ListOrganizerMenu getListOrganizerMenu() {
        return listOrganizerMenu;
    }

    public HorseInfoMenu getHorseInfoMenu() {
        return horseInfoMenu;
    }

    public AutoVetMenu getAutoVetMenu() {
        return autoVetMenu;
    }

    public GroomMenu getGroomMenu() {
        return groomMenu;
    }

    public HealthMenu getHealthMenu() {
        return healthMenu;
    }

    public HomeMenu getHomeMenu() {
        return homeMenu;
    }

    public HorseListMenu getHorseListMenu() {
        return horseListMenu;
    }

    public InventoryHorseMenu getInventoryHorseMenu() {
        return inventoryHorseMenu;
    }
}
