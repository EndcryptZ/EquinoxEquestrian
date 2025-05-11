package endcrypt.equinox.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.attributes.CoatModifier;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

        // Add Horse to database
        try {
            plugin.getDatabaseManager().addLiveHorse(horse);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NBT.modifyPersistentData(horse, nbt -> {


            long currentTime = System.currentTimeMillis();

            nbt.setString("EQUINE_HORSE", "true");

            nbt.setString("EQUINE_DISCIPLINE", equineHorse.getDiscipline().name());
            nbt.setString("EQUINE_BREED", equineHorse.getBreed().name());
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

    private String randomSkullId() {
        List<String> idList = Arrays.asList("49654", "7280", "49652", "49651", "1154", "3920", "3919", "2912", "7649");
        Random random = new Random();
        int randomID = random.nextInt(idList.size());
        String headID = idList.get(randomID);
        return headID;
    }
}
