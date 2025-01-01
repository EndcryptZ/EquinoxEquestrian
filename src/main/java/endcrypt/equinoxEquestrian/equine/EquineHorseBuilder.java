package endcrypt.equinoxEquestrian.equine;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import endcrypt.equinoxEquestrian.equine.enums.Breed;
import endcrypt.equinoxEquestrian.equine.enums.Discipline;
import endcrypt.equinoxEquestrian.equine.enums.Gender;
import endcrypt.equinoxEquestrian.equine.enums.Trait;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class EquineHorseBuilder {

    private final String name;

    public EquineHorseBuilder(String name) {
        this.name = name;
    }

    // Method to spawn the horse at a player's location
    public void spawnHorse(Player player, Discipline discipline, Breed breed, Gender gender, Trait[] traits) {
        World world = player.getWorld();
        Location location = player.getLocation();

        // Spawn the horse
        Horse horse = (Horse) world.spawnEntity(location, EntityType.HORSE);

        // Set the horse's custom name
        horse.setCustomName(name);
        horse.setCustomNameVisible(true);

        // Optional: Set horse as tameable
        horse.setTamed(true);
        horse.setOwner(player);

        ReadWriteNBT entityNbt = NBT.createNBTObject();
        NBT.get(horse, entityNbt::mergeCompound);
        NBT.modify(horse, nbt -> {
            nbt.setBoolean("EQUINE_HORSE", true);
            nbt.setString("EQUINE_DISCIPLINE", discipline.name());
            nbt.setString("EQUINE_BREED", breed.name());
            nbt.setString("EQUINE_GENDER", gender.name());

            IntStream.range(0, traits.length)
                            .forEach(i -> {
                                nbt.setString("EQUINE_TRAIT_" + i, traits[i].name());
                            });

            nbt.mergeCompound(entityNbt);
        });

    }
}
