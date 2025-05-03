package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.horse.enums.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.sql.SQLException;
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
        horse.setCustomName(equineHorse.getName());
        horse.setCustomNameVisible(true);

        // Optional: Set horse as tamed
        horse.setTamed(true);
        horse.setOwner(player);

        horse.setColor(equineHorse.getCoatColor().getHorseColor());

        if(equineHorse.getCoatModifier() != CoatModifier.NONE) {
            horse.setStyle(equineHorse.getCoatModifier().getHorseCoatModifier());
        }

        horse.getAttribute(Attribute.SCALE).setBaseValue(equineHorse.getHeight().getSize());

        try {
            plugin.getDatabaseManager().addLiveHorse(horse);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NBT.modifyPersistentData(horse, nbt -> {


            long currentTime = System.currentTimeMillis();
            nbt.setLong("EQUINE_BIRTH_TIME", currentTime - (MILLIS_PER_YEAR * equineHorse.getAge()));


            nbt.setString("EQUINE_HORSE", "true");
            nbt.setString("EQUINE_OWNER_UUID", player.getUniqueId().toString());
            nbt.setString("EQUINE_OWNER_NAME", player.getName());
            nbt.setString("EQUINE_DISCIPLINE", equineHorse.getDiscipline().name());
            nbt.setString("EQUINE_BREED", equineHorse.getBreed().name());
            nbt.setString("EQUINE_GENDER", equineHorse.getGender().name());
            nbt.setInteger("EQUINE_AGE", equineHorse.getAge());

            nbt.setDouble("EQUINE_HEIGHT", equineHorse.getHeight().getHands());

            nbt.setDouble("EQUINE_BASE_SPEED", horse.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
            nbt.setDouble("EQUINE_BASE_JUMP_POWER", horse.getJumpStrength());

            IntStream.range(0, equineHorse.getTraits().length)
                            .forEach(i -> nbt.setString("EQUINE_TRAIT_" + i, equineHorse.getTraits()[i].name()));


            nbt.setString("EQUINE_IS_CROSS_TIED", "false");

        });
    }
}
