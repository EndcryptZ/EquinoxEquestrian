package endcrypt.equinoxEquestrian.menu.build;

import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.item.ItemBuilder;
import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.equine.*;
import endcrypt.equinoxEquestrian.equine.attributes.*;
import endcrypt.equinoxEquestrian.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BuildMenu implements Listener {

    private final EquinoxEquestrian plugin;
    public BuildMenu(EquinoxEquestrian plugin) {
        this.plugin = plugin;

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
        SGButton ageButton = ageButton(equineHorse);
        SGButton heightButton = heightButton(equineHorse);
        SGButton traitsButton = traitsButton(equineHorse);
        SGButton buyButton = buyButton(player, equineHorse);
        SGButton tokenButton = tokenButton(player);
        SGButton costButton = costButton(player, equineHorse);

        gui.setButton(0, nameButton);
        gui.setButton(1, disciplineButton);
        gui.setButton(2, breedButton);
        gui.setButton(3, coatColorButton);
        gui.setButton(4, coatModifierButton);
        gui.setButton(5, genderButton);
        gui.setButton(6, ageButton);
        gui.setButton(7, heightButton);
        gui.setButton(8, traitsButton);
        gui.setButton(22, buyButton);

        gui.setButton(25, tokenButton);
        gui.setButton(26, costButton);
        return gui.getInventory();
    }

    private Inventory defaultMenu(Player player) {
        String name = "";
        Discipline discipline = Discipline.NONE;
        Breed breed = Breed.NONE;
        CoatColor coatColor = CoatColor.NONE;
        CoatModifier coatModifier = CoatModifier.NONE;
        Gender gender = Gender.NONE;
        Height height = null;
        int age = 4;

        for(Height loopedHeight : Height.values()) {
            if(loopedHeight.getHands() == breed.getMinimumHands()) {
                height = loopedHeight;
                break;
            }
        }

        Trait[] traits = {Trait.NONE, Trait.NONE, Trait.NONE};

        EquineHorse equineHorse = new EquineHorse(name, discipline, breed, coatColor, coatModifier, gender, age, height, traits);

        return createMenu(player, equineHorse);
    }

    private Inventory menuWithParameters(Player player, EquineHorse equineHorse) {
        double minHand = equineHorse.getBreed().getMinimumHands();
        double maxHand = equineHorse.getBreed().getMaximumHands();
        double currentHand = equineHorse.getHeight().getHands();


        if(minHand > currentHand) {
            equineHorse.setHeight(Height.getByHands(minHand));
        }

        if(maxHand < currentHand) {
            equineHorse.setHeight(Height.getByHands(maxHand));
        }

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


                    plugin.getBuildMenuManager().getPlayerEquineHorseInput().put(player, equineHorse);

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

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getDisciplineSelectMenu().disciplineMenu(player, equineHorse));

                });
    }

    private SGButton breedButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fBreed")
                        .lore(
                                ChatColor.WHITE + equineHorse.getBreed().getName()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getBreedSelectMenu().breedMenu(player, equineHorse));

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

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getCoatColorSelectMenu().coatColorMenu(player, equineHorse));

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

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getCoatModifierSelectMenu().coatModifierMenu(player, equineHorse));

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

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getGenderSelectMenu().genderMenu(player, equineHorse));

                });
    }

    private SGButton ageButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fAge")
                        .lore(
                                ChatColor.WHITE + String.valueOf(equineHorse.getAge())
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    int age = equineHorse.getAge();

                    ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
                    if (event.getClick().isLeftClick()) {
                        if (age == 25) {
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            return;
                        }


                        equineHorse.setAge(age + 1);
                        age = equineHorse.getAge(); // Get updated age

                        // Set the new lore
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.WHITE + String.valueOf(age));
                        itemMeta.setLore(lore);

                        // Apply the updated meta to the item
                        event.getCurrentItem().setItemMeta(itemMeta);
                    }

                    if (event.getClick().isRightClick()) {
                        if (age == 4) {
                            ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§fAge", ChatColor.RED + "The minimum age you can set is 4.", null, null);
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            return;
                        }

                        equineHorse.setAge(age - 1);
                        age = equineHorse.getAge(); // Get updated age

                        // Set the new lore
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.WHITE + String.valueOf(age));
                        itemMeta.setLore(lore);

                        // Apply the updated meta to the item
                        event.getCurrentItem().setItemMeta(itemMeta);
                    }
                });
    }


    private SGButton heightButton(EquineHorse equineHorse) {

        return new SGButton(
                new ItemBuilder(Material.PAPER)
                        .name("&fHeight")
                        .lore(
                                ChatColor.WHITE + equineHorse.getHeight().getHandsString()
                        )
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {
                    Player player = (Player) event.getWhoClicked();

                    Height height = equineHorse.getHeight();

                    ItemMeta itemMeta = event.getCurrentItem().getItemMeta();
                    if (event.getClick().isLeftClick()) {

                        if(Height.getNextHeight(height.getSize()) != null) {

                            // Cancel if the next height exceeds the maximum height of the breed
                            if(Height.getNextHeight(height.getSize()).getHands() > equineHorse.getBreed().getMaximumHands()) {
                                return;
                            }

                            equineHorse.setHeight(Height.getNextHeight(height.getSize()));
                            height = equineHorse.getHeight(); // Get updated height

                        }


                        // Set the new lore
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.WHITE + equineHorse.getHeight().getHandsString());
                        itemMeta.setLore(lore);

                        // Apply the updated meta to the item
                        event.getCurrentItem().setItemMeta(itemMeta);
                    }

                    if (event.getClick().isRightClick()) {

                        if(Height.getPreviousHeight(height.getSize()) != null) {

                            // Cancel if the previous height exceeds the minimum height of the breed
                            if(Height.getPreviousHeight(height.getSize()).getHands() < equineHorse.getBreed().getMinimumHands()) {
                                return;
                            }

                            equineHorse.setHeight(Height.getPreviousHeight(height.getSize()));
                            height = equineHorse.getHeight(); // Get updated height

                        }

                        // Set the new lore
                        List<String> lore = new ArrayList<>();
                        lore.add(ChatColor.WHITE + equineHorse.getHeight().getHandsString());
                        itemMeta.setLore(lore);

                        // Apply the updated meta to the item
                        event.getCurrentItem().setItemMeta(itemMeta);
                    }
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

                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().put(player, equineHorse);
                    player.openInventory(plugin.getBuildMenuManager().getTraitSelectMenu().traitMenu(player, equineHorse));

                });
    }

    private SGButton buyButton(Player player, EquineHorse equineHorse) {
        return new SGButton(
                new ItemBuilder(Material.OAK_BUTTON)
                        .name("&7[&aBuy&7]")
                        .build()
        )
                .withListener((InventoryClickEvent event) -> {

                    List<String> missingAttributes = new ArrayList<>();

                    if (equineHorse.getName() == "") missingAttributes.add("Name");
                    if (equineHorse.getDiscipline() == Discipline.NONE) missingAttributes.add("Discipline");
                    if (equineHorse.getBreed() == Breed.NONE) missingAttributes.add("Breed");
                    if (equineHorse.getCoatColor() == CoatColor.NONE) missingAttributes.add("Coat Color");
                    if (equineHorse.getGender() == Gender.NONE) missingAttributes.add("Gender");
                    if (Arrays.asList(equineHorse.getTraits()).contains(Trait.NONE)) missingAttributes.add("Trait(s)");

                    if (!missingAttributes.isEmpty()) {
                        List<String> loreMessage = new ArrayList<>();
                        loreMessage.add(ChatColor.RED + "Missing: " + String.join(", ", missingAttributes));
                        ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§7[§aBuy§7]", ChatColor.RED + "Horse details are incomplete!", null, loreMessage);
                        return;
                    }
                    if(plugin.getPlayerDataManager().getPlayerData(player).getTokens() < 1) {
                        if (plugin.getEcon().getBalance(player) < plugin.getBuildMenuManager().getPlayerCost().get(player)) {
                            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
                            List<String> loreMessage = new ArrayList<>();
                            loreMessage.add("§eYour balance: §a$§f" + plugin.getEcon().getBalance(player));
                            loreMessage.add("§eCost: §a$§f" + plugin.getBuildMenuManager().getPlayerCost().get(player));

                            ItemUtils.itemMessage(plugin, event.getCurrentItem(), "§7[§aBuy§7]", ChatColor.RED + "You don't have enough money!", null, loreMessage);
                            return;
                        }
                        plugin.getEcon().withdrawPlayer(player, plugin.getBuildMenuManager().getPlayerCost().get(player));
                    } else {
                        plugin.getPlayerDataManager().getPlayerData(player).setTokens(plugin.getPlayerDataManager().getPlayerData(player).getTokens() - 1);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7You used a token!"));
                    }

                    EquineHorseBuilder horseBuilder = new EquineHorseBuilder(plugin);
                    player.closeInventory();
                    plugin.getBuildMenuManager().getPlayerEquineHorseInput().remove(player);
                    plugin.getBuildMenuManager().getPlayerCost().remove(player);
                    horseBuilder.spawnHorse(player, equineHorse);
                });
    }

    private SGButton tokenButton(Player player) {

        return new SGButton(
                new ItemBuilder(Material.GOLD_INGOT)
                        .name("&f[&eToken&f]")
                        .lore("&70")
                        .build()
        );
    }

    private SGButton costButton(Player player, EquineHorse equineHorse) {

        int namePrice = 1000;
        int disciplinePrice = equineHorse.getDiscipline().getPrice();
        int coatColorPrice = 1000;
        int coatStylePrice = 1000;
        int genderPrice = equineHorse.getGender().getPrice();
        int agePrice = 1000;
        int heightPrice = 1000;
        int traitsPrice = equineHorse.getTraits()[0].getPrice() + equineHorse.getTraits()[1].getPrice() + equineHorse.getTraits()[2].getPrice();

        double price = namePrice + disciplinePrice + coatColorPrice + coatStylePrice + genderPrice + agePrice + heightPrice + traitsPrice;
        plugin.getBuildMenuManager().getPlayerCost().put(player, price);

        return new SGButton(
                new ItemBuilder(Material.MAP)
                        .name("Cost")
                        .lore("&a$&f" + price)
                        .build()
        );
    }

}
