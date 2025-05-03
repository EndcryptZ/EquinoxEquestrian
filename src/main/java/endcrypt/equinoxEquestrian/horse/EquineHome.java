package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();

        if(horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have not selected a horse!"));
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
        AbstractHorse horse = plugin.getPlayerManager().getPlayerData(player).getSelectedHorse();

        if (horse == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have not selected a horse!"));
            return;
        }

        Location loc = null;

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
            player.sendMessage(ChatColor.RED + "No saved " + type.toLowerCase() + " location for your selected horse!");
            return;
        }

        if (loc != null) {
            player.teleport(loc);
            horse.teleport(loc);
        }
    }



}
