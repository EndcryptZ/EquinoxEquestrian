package endcrypt.equinoxEquestrian.menu.build;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.EquineHorseBuilder;
import endcrypt.equinoxEquestrian.equine.enums.Breed;
import endcrypt.equinoxEquestrian.equine.enums.Discipline;
import endcrypt.equinoxEquestrian.equine.enums.Gender;
import endcrypt.equinoxEquestrian.equine.enums.Trait;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
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

    private final Map<Player, String> playerNameInput = new HashMap<>();
    private final Map<Player, Discipline> playerDisciplineInput = new HashMap<>();
    private final Map<Player, Breed> playerBreedInput = new HashMap<>();
    private final Map<Player, Gender> playerGenderInput = new HashMap<>();
    private final Map<Player, Integer> playerAgeInput = new HashMap<>();
    private final Map<Player, Trait[]> playerTraitsInput = new HashMap<>();

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

    public void openWithParameters(Player player, String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        player.openInventory(menuWithParameters(player, name, discipline, breed, gender, traits));
    }

    private Inventory createMenu(Player player, String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {
        SGMenu gui = plugin.getSpiGUI().create("Build a Horse", 3, "Build a Horse");

        SGButton nameButton = nameButton(name, discipline, breed, gender, traits);
        SGButton disciplineButton = disciplineButton(name, discipline, breed, gender, traits);
        SGButton breedButton = breedButton(name, discipline, breed, gender, traits);
        SGButton traitsButton = traitsButton(name, discipline, breed, gender, traits);
        SGButton buyButton = buyButton(player, name, discipline, breed, gender, traits);
        SGButton costButton = costButton(discipline, gender, traits);

        gui.setButton(0, nameButton);
        gui.setButton(1, disciplineButton);
        gui.setButton(2, breedButton);
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
        Gender gender = Gender.STALLION;
        Trait[] traits = {Trait.AGGRESSIVE, Trait.AGILE, Trait.ADVENTUROUS};

        return createMenu(player, randomHorseName, discipline, breed, gender, traits);
    }

    private Inventory menuWithParameters(Player player, String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {
        return createMenu(player, name, discipline, breed, gender, traits);
    }



    // Main Buttons
    private SGButton nameButton(String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fName")
                        .lore(
                                ChatColor.WHITE + name
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    playerNameInput.put(player, name);
                    playerDisciplineInput.put(player, discipline);
                    playerBreedInput.put(player, breed);
                    playerGenderInput.put(player, gender);
                    playerTraitsInput.put(player, traits);

                    player.closeInventory();

                    player.sendTitle("§6§lHorse Creation", "§fSee chat.", 10, 40, 10);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Enter the name you'd like for your horse in chat."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7To cancel, type 'Cancel' in chat."));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Name length: &a2&f to &a16 &fcharacters."));

                });
    }

    private SGButton disciplineButton(String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fBreed")
                        .lore(
                                ChatColor.WHITE + discipline.getDisciplineName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getDisciplineSelectMenu().disciplineMenu(player, name, breed, gender, traits));

                });
    }

    private SGButton breedButton(String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fBreed")
                        .lore(
                                ChatColor.WHITE + breed.getBreedName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    player.openInventory(plugin.getBreedSelectMenu().breedMenu(player, name, discipline, gender, traits));

                });
    }

    private SGButton traitsButton(String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fTraits")
                        .lore(
                                ChatColor.WHITE + "(1) " + traits[0].getTraitName(),
                                ChatColor.WHITE + "(2) " + traits[1].getTraitName(),
                                ChatColor.WHITE + "(3) " + traits[2].getTraitName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();


                });
    }

    private SGButton buyButton(Player player, String name, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {

        return new SGButton(
                new ItemBuilder(Material.OAK_BUTTON)
                        .name("&7[&aBuy&7]")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    EquineHorseBuilder horseBuilder = new EquineHorseBuilder(name);
                    horseBuilder.spawnHorse(player, discipline, breed, gender, traits);
                    clearInputs(player);
                });
    }

    private SGButton costButton(Discipline discipline, Gender gender, Trait[] traits) {

        int namePrice = 1000;
        int disciplinePrice = discipline.getPrice();
        int coatColorPrice = 1000;
        int coatStylePrice = 1000;
        int genderPrice = gender.getPrice();
        int agePrice = 1000;
        int heightPrice = 1000;
        int traitsPrice = traits[0].getPrice() + traits[1].getPrice() + traits[2].getPrice();

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
        if(!playerNameInput.containsKey(event.getPlayer())) {
            return;
        }

        if(event.getMessage().equalsIgnoreCase("cancel")) {
            openWithParameters(event.getPlayer(),
                    playerNameInput.get(event.getPlayer()),
                    playerDisciplineInput.get(event.getPlayer()),
                    playerBreedInput.get(event.getPlayer()),
                    playerGenderInput.get(event.getPlayer()),
                    playerTraitsInput.get(event.getPlayer())
            );
            clearInputs(event.getPlayer());
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

        openWithParameters(event.getPlayer(),
                event.getMessage(),
                playerDisciplineInput.get(event.getPlayer()),
                playerBreedInput.get(event.getPlayer()),
                playerGenderInput.get(event.getPlayer()),
                playerTraitsInput.get(event.getPlayer())
        );
        playerNameInput.remove(event.getPlayer());
        event.setCancelled(true);
    }

    private void clearInputs(Player player) {
        playerNameInput.remove(player);
        playerDisciplineInput.remove(player);
        playerBreedInput.remove(player);
        playerGenderInput.remove(player);
        playerAgeInput.remove(player);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(!playerNameInput.containsKey(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cCommands are disabled during Horse Creation!"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!playerNameInput.containsKey(event.getPlayer())) {
            return;
        }

        clearInputs(event.getPlayer());
    }

}
