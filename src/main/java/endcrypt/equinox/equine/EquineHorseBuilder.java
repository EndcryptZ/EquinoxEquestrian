package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.IntStream;

public class EquineHorseBuilder {
    private final EquinoxEquestrian plugin;

    public EquineHorseBuilder(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private static final long MILLIS_PER_YEAR = 21 * 24 * 60 * 60 * 1000;

    // Method to spawn the horse at a player's location
    public void spawnHorse(Player player, EquineHorse equineHorse) {
        World world = player.getWorld();
        Location location = player.getLocation();

        // Spawn the horse
        Horse horse = (Horse) world.spawnEntity(location, EntityType.HORSE);

        // Set the horse's custom name
        horse.customName(ColorUtils.color(equineHorse.getName()));
        horse.setCustomNameVisible(true);

        // Optional: Set horse as tamed
        horse.setTamed(true);
        horse.setOwner(player);

        horse.setColor(equineHorse.getCoatColor().getHorseColor());

        if(equineHorse.getCoatModifier() != CoatModifier.NONE) {
            horse.setStyle(equineHorse.getCoatModifier().getHorseCoatModifier());
        }

        horse.getAttribute(Attribute.SCALE).setBaseValue(equineHorse.getHeight().getSize());

        // Add Horse to player data
        plugin.getDatabaseManager().addHorse(horse);

        NBT.modifyPersistentData(horse, nbt -> {


            long currentTime = System.currentTimeMillis();

            nbt.setString("EQUINE_HORSE", "true");
            nbt.setString("EQUINE_DISCIPLINE", equineHorse.getDiscipline().name());

            IntStream.range(0, equineHorse.getBreeds().length)
                    .forEach(i -> {
                    nbt.setString("EQUINE_BREED_" + i, equineHorse.getBreeds()[i].name());
                    Bukkit.getServer().broadcast(ColorUtils.color("Set EQUINE_BREED_" + i + " of " + horse.getName() + " to " + equineHorse.getBreeds()[i].name()));
                    });

            if(equineHorse.getBreeds().length > 1) {
                equineHorse.setProminentBreed(equineHorse.getBreeds()[new Random().nextInt(equineHorse.getBreeds().length)]);
                nbt.setString("EQUINE_PROMINENT_BREED", equineHorse.getProminentBreed().name());
            }

            nbt.setString("EQUINE_GENDER", equineHorse.getGender().name());
            nbt.setInteger("EQUINE_AGE", equineHorse.getAge());
            nbt.setDouble("EQUINE_HEIGHT", equineHorse.getHeight().getHands());
            IntStream.range(0, equineHorse.getTraits().length)
                            .forEach(i -> nbt.setString("EQUINE_TRAIT_" + i, equineHorse.getTraits()[i].name()));


            nbt.setLong("EQUINE_CLAIM_TIME", currentTime);
            nbt.setLong("EQUINE_BIRTH_TIME", currentTime - (MILLIS_PER_YEAR * equineHorse.getAge()));
            nbt.setString("EQUINE_OWNER_UUID", player.getUniqueId().toString());
            nbt.setString("EQUINE_OWNER_NAME", player.getName());
            nbt.setDouble("EQUINE_BASE_SPEED", horse.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
            nbt.setDouble("EQUINE_BASE_JUMP_POWER", horse.getJumpStrength());
            nbt.setString("EQUINE_SKULL_ID", randomSkullId());

            nbt.setString("EQUINE_IS_CROSS_TIED", "false");

        });
    }

    // Assigns a horse a random skull for menus
    private String randomSkullId() {
        List<String> idList = Arrays.asList("49654", "7280", "49652", "49651", "1154", "3920", "3919", "2912", "7649");
        Random random = new Random();
        int randomID = random.nextInt(idList.size());
        String headID = idList.get(randomID);
        return headID;
    }

    public EquineHorse randomHorse(String name) {
        Random random = new Random();

        Discipline discipline = Discipline.random();
        Breed[] breeds = Breed.random(random.nextInt(2) + 1);
        CoatColor coatColor = CoatColor.random();
        CoatModifier coatModifier = CoatModifier.random();
        Gender gender = Gender.random();
        int age = random.nextInt(10) + 1;
        Height height = Height.getRandomHeight(breeds[0]);
        Trait[] traits = Trait.random(random.nextInt(3) + 1);

        return new EquineHorse(name, discipline, breeds, coatColor, coatModifier, gender, age, height, traits);
    }
}
