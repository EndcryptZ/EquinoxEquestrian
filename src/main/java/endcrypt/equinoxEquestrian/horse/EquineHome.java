package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.IntStream;

public class EquineHome {

    private EquinoxEquestrian plugin;
    public EquineHome(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void setHome(Player player, String type) {
        List<Entity> leashedList = EquineUtils.getLeashedEntities(player);

        if (leashedList.size() > 1) {
            player.sendMessage("§cPlease only leash one horse at a time.");
            return;
        }

        if (leashedList.isEmpty()) {
            player.sendMessage("§cYou don't have a leashed horse to set home.");
            return;
        }

        LivingEntity leashedEntity = (LivingEntity) leashedList.get(0);

        if (!(leashedEntity instanceof AbstractHorse)) {
            player.sendMessage("§cYou can only lunge a horse! The entity you're leashed to is a " + leashedEntity.getType() + ".");
            return;
        }

        AbstractHorse horse = (AbstractHorse) leashedEntity;

            if(type.equalsIgnoreCase("pasture")) {
                NBT.modifyPersistentData(horse, nbt -> {
                    nbt.setString("HAS_PASTURE", "true");
                    nbt.setDouble("PASTURE_X", player.getLocation().getX());
                    nbt.setDouble("PASTURE_Y", player.getLocation().getY());
                    nbt.setDouble("PASTURE_Z", player.getLocation().getZ());
                nbt.setString("PASTURE_WORLD", player.getWorld().getName());
            });

            player.sendMessage("Sucessfully set leashed horse's pasture to your current location!");
        }

        if(type.equalsIgnoreCase("stall")) {
            NBT.modifyPersistentData(horse, nbt -> {
                nbt.setString("HAS_STALL", "true");
                nbt.setDouble("STALL_X", player.getLocation().getX());
                nbt.setDouble("STALL_Y", player.getLocation().getY());
                nbt.setDouble("STALL_Z", player.getLocation().getZ());
                nbt.setString("STALL_WORLD", player.getWorld().getName());
            });

            player.sendMessage("Sucessfully set leashed horse's pasture to your current location!");
        }


    }
}
