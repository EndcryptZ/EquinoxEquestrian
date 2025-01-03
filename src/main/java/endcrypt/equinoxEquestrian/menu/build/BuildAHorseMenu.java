package endcrypt.equinoxEquestrian.menu.build;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorse;
import endcrypt.equinoxEquestrian.equine.EquineHorseBuilder;
import endcrypt.equinoxEquestrian.equine.enums.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BuildAHorseMenu implements Listener {

    private final Map<Player, EquineHorse> playerEquineHorseInput = new HashMap<>();

    private final Map<Player, Double> playerCost = new HashMap<>();


    String[] horseNames = {
            "Thunder", "Spirit", "Shadow", "Majesty", "Blaze",
            "Storm", "Aurora", "Dakota", "Scout", "Cinnamon",
            "Rusty", "Whisper", "Apollo", "Hunter", "Luna",
            "Nova", "Sable", "Falcon", "Echo", "Zephyr",
            "Midnight", "Ranger", "Starlight", "Comet", "Sundance",
            "Ash", "Flicka", "Windy", "Jet", "Sky",
            "Silver", "Stormy", "Dancer", "Rain", "Willow",
            "Breeze", "Mustang", "Copper", "Tornado", "Rio",
            "Phoenix", "Gypsy", "Lightning", "Rogue", "Bandit",
            "Cherokee", "Raven", "Onyx", "Clover", "Mystic"
    };


    private final EquinoxEquestrian plugin;
    public BuildAHorseMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openDefault(Player player){


        player.openInventory(defaultMenu(player));
    }

    public void openWithParameters(Player player, EquineHorse equineHorse) {

        player.openInventory(menuWithParameters(player, equineHorse));
    }

    private Inventory createMenu(Player player, EquineHorse equineHorse) {
        SGMenu gui = plugin.getSpiGUI().create("Build a Horse", 3, "Build a Horse");

        SGButton nameButton = nameButton(equineHorse);
        SGButton disciplineButton = disciplineButton(equineHorse);
        SGButton breedButton = breedButton(equineHorse);
        SGButton coatColorButton = coatColorButton(equineHorse);
        SGButton coatModifierButton = coatModifierButton(equineHorse);
        SGButton genderButton = genderButton(equineHorse);
        SGButton traitsButton = traitsButton(equineHorse);
        SGButton buyButton = buyButton(player, equineHorse);
        SGButton costButton = costButton(equineHorse);

        gui.setButton(0, nameButton);
        gui.setButton(1, disciplineButton);
        gui.setButton(2, breedButton);
        gui.setButton(3, coatColorButton);
        gui.setButton(4, coatModifierButton);
        gui.setButton(5, genderButton);
        gui.setButton(8, traitsButton);
        gui.setButton(22, buyButton);

        gui.setButton(26, costButton);
        return gui.getInventory();
    }

    private Inventory defaultMenu(Player player) {
        Random randomName = new Random();
        String randomHorseName = horseNames[randomName.nextInt(horseNames.length)];
        Discipline discipline = Discipline.ALL_ROUND;
        Breed breed = Breed.AEGIDIENBERGER;
        CoatColor coatColor = CoatColor.WHITE;
        CoatModifier coatModifier = CoatModifier.NONE;
        Gender gender = Gender.STALLION;
        Trait[] traits = {Trait.AGGRESSIVE, Trait.AGILE, Trait.ADVENTUROUS};

        EquineHorse equineHorse = new EquineHorse(randomHorseName, discipline, breed, coatColor, coatModifier, gender, traits);

        return createMenu(player, equineHorse);
    }

    private Inventory menuWithParameters(Player player, EquineHorse equineHorse) {
        return createMenu(player, equineHorse);
    }



    // Main Buttons
    private SGButton nameButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fName")
                        .lore(
                                ChatColor.WHITE + equineHorse.getName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();


                    playerEquineHorseInput.put(player, equineHorse);

                    player.closeInventory();

                    player.sendTitle("§6§lHorse Creation", "§fSee chat.", 10, 40, 10);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Enter the name you'd like for your horse in chat."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7To cancel, type 'Cancel' in chat."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Name length: &a2&f to &a16 &fcharacters."));

                });
    }

    private SGButton disciplineButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fDiscipline")
                        .lore(
                                ChatColor.WHITE + equineHorse.getDiscipline().getDisciplineName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getDisciplineSelectMenu().disciplineMenu(player, equineHorse));

                });
    }

    private SGButton breedButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fBreed")
                        .lore(
                                ChatColor.WHITE + equineHorse.getBreed().getBreedName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getBreedSelectMenu().breedMenu(player, equineHorse));

                });
    }

    private SGButton coatColorButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fCoat Color")
                        .lore(
                                ChatColor.WHITE + equineHorse.getCoatColor().getCoatColorName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getCoatColorSelectMenu().coatColorMenu(player, equineHorse));

                });
    }

    private SGButton coatModifierButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fCoat Modifier")
                        .lore(
                                ChatColor.WHITE + equineHorse.getCoatModifier().getCoatModifierName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getCoatModifierSelectMenu().coatModifierMenu(player, equineHorse));

                });
    }

    private SGButton genderButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fGender")
                        .lore(
                                ChatColor.WHITE + equineHorse.getGender().getGenderName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getGenderSelectMenu().genderMenu(player, equineHorse));

                });
    }

    private SGButton traitsButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fTraits")
                        .lore(
                                ChatColor.WHITE + "(1) " + equineHorse.getTraits()[0].getTraitName(),
                                ChatColor.WHITE + "(2) " + equineHorse.getTraits()[1].getTraitName(),
                                ChatColor.WHITE + "(3) " + equineHorse.getTraits()[2].getTraitName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getTraitSelectMenu().traitMenu(player, equineHorse));

                });
    }

    private SGButton buyButton(Player player, EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.OAK_BUTTON)
                        .name("&7[&aBuy&7]")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineHorseBuilder horseBuilder = new EquineHorseBuilder();
                    horseBuilder.spawnHorse(player, equineHorse);
                    player.closeInventory();
                    playerEquineHorseInput.remove(player);
                });
    }

    private SGButton costButton(EquineHorse equineHorse) {

        int namePrice = 1000;
        int disciplinePrice = equineHorse.getDiscipline().getPrice();
        int coatColorPrice = 1000;
        int coatStylePrice = 1000;
        int genderPrice = equineHorse.getGender().getPrice();
        int agePrice = 1000;
        int heightPrice = 1000;
        int traitsPrice = equineHorse.getTraits()[0].getPrice() + equineHorse.getTraits()[1].getPrice() + equineHorse.getTraits()[2].getPrice();

        double price = namePrice + disciplinePrice + coatColorPrice + coatStylePrice + genderPrice + agePrice + heightPrice + traitsPrice;

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("Cost")
                        .lore("&a$&f" + price)
                        .build()
        );
    }


    // Events
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        if(!playerEquineHorseInput.containsKey(event.getPlayer())) {
            return;
        }

        if(event.getMessage().equalsIgnoreCase("cancel")) {
            openWithParameters(event.getPlayer(),
                    playerEquineHorseInput.get(event.getPlayer())
            );
            playerEquineHorseInput.remove(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if(event.getMessage().length() < 2) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cName too short! Please keep it above 1 character."));
            event.setCancelled(true);
            return;
        }

        if(event.getMessage().length() > 16) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cName too long! Please keep it under 16 characters."));
            event.setCancelled(true);
            return;
        }

        playerEquineHorseInput.get(event.getPlayer()).setName(event.getMessage());
        openWithParameters(event.getPlayer(),
                playerEquineHorseInput.get(event.getPlayer())
        );
        playerEquineHorseInput.remove(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(!playerEquineHorseInput.containsKey(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cCommands are disabled during Horse Creation!"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!playerEquineHorseInput.containsKey(event.getPlayer())) {
            return;
        }

        playerEquineHorseInput.remove(event.getPlayer());
    }
}
