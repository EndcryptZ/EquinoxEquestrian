package endcrypt.equinox.equine.home;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

public class EquineHome {

    private final EquinoxEquestrian plugin;
    public EquineHome(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    public void setHome(Player player, String type) {
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if(horse == null) {
            player.sendMessage(ColorUtils.color("<red>You have not selected a horse!"));
            return;
        }


            if(type.equalsIgnoreCase("pasture")) {
                NBT.modifyPersistentData(horse, nbt -> {
                    nbt.setString("HAS_PASTURE", "true");
                    nbt.setDouble("PASTURE_X", player.getLocation().getX());
                    nbt.setDouble("PASTURE_Y", player.getLocation().getY());
                    nbt.setDouble("PASTURE_Z", player.getLocation().getZ());
                nbt.setString("PASTURE_WORLD", player.getWorld().getName());
            });

            player.sendMessage("Sucessfully set selected horse's pasture to your current location!");
        }

        if(type.equalsIgnoreCase("stall")) {
            NBT.modifyPersistentData(horse, nbt -> {
                nbt.setString("HAS_STALL", "true");
                nbt.setDouble("STALL_X", player.getLocation().getX());
                nbt.setDouble("STALL_Y", player.getLocation().getY());
                nbt.setDouble("STALL_Z", player.getLocation().getZ());
                nbt.setString("STALL_WORLD", player.getWorld().getName());
            });

            player.sendMessage("Sucessfully set selected horse's pasture to your current location!");
        }


    }

    public void teleportHome(Player player, String type) {
        AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ColorUtils.color("<red>You have not selected a horse!"));
            return;
        }

        Location loc;

        if (type.equalsIgnoreCase("pasture") && NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_PASTURE")).equals("true")) {
            double x = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_X"));
            double y = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Y"));
            double z = NBT.getPersistentData(horse, nbt -> nbt.getDouble("PASTURE_Z"));
            String world = NBT.getPersistentData(horse, nbt -> nbt.getString("PASTURE_WORLD"));

            loc = new Location(plugin.getServer().getWorld(world), x, y, z);
            player.sendMessage("You and your horse have been teleported to its pasture!");
        } else if (type.equalsIgnoreCase("stall") && NBT.getPersistentData(horse, nbt -> nbt.getString("HAS_STALL")).equals("true")) {
            double x = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_X"));
            double y = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Y"));
            double z = NBT.getPersistentData(horse, nbt -> nbt.getDouble("STALL_Z"));
            String world = NBT.getPersistentData(horse, nbt -> nbt.getString("STALL_WORLD"));

            loc = new Location(plugin.getServer().getWorld(world), x, y, z);
            player.sendMessage("You and your horse have been teleported to its stall!");
        } else {
            player.sendMessage(ColorUtils.color("<red>You have not selected a horse!"));
            return;
        }

        player.teleport(loc);
        horse.teleport(loc);
    }



}
