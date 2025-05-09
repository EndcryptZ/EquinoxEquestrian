package endcrypt.equinoxEquestrian.equine.crosstie;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.api.events.EquineCrossTieLeashEvent;
import endcrypt.equinoxEquestrian.api.events.EquineCrossTieLeashRemovedEvent;
import endcrypt.equinoxEquestrian.equine.EquineUtils;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class EquineCrossTieListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineCrossTieListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractHorse(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();

        if (!horse.isLeashed()) {
            return;
        }

        Entity leashHolder = horse.getLeashHolder();

        if(!(leashHolder instanceof LeashHitch)) {
            return;
        }

        if (heldItem.getType() != Material.LEAD) {
            return;
        }

        if (EquineUtils.isCrossTied(horse)) {
            return;
        }

        if (!EquineUtils.isLivingEquineHorse(horse)) {
            player.sendMessage(ColorUtils.color("<red>You're trying to cross-tie a Vanilla Horse. Please try it with an Equine Horse"));
            return;
        }

        // Get the horse's location and direction
        Location horseLocation = horse.getLocation();
        Vector direction = horseLocation.getDirection().normalize();  // Get the direction the horse is facing

        // Calculate the bat's spawn location (a bit in front of the horse and slightly higher for the neck)
        Location batSpawnLocation = horseLocation.clone().add(direction.multiply(0.3));  // Move 1.5 blocks in front
        batSpawnLocation.setY(batSpawnLocation.getY() + 0.9);  // Adjust Y-axis to the height of the neck

        // Spawn the bat at the calculated location
        Bat bat = player.getWorld().spawn(batSpawnLocation, Bat.class);

        // Set the bat's leash holder to the player (simulating cross-tie)
        bat.setLeashHolder(player);

        // Make the bat invisible, invulnerable, and non-collidable
        bat.setAI(false); // Disable AI so the bat doesn't move
        bat.setInvulnerable(true); // Make the bat invulnerable (god mode)
        bat.setCollidable(false); // Make the bat non-collidable
        bat.setInvisible(true);
        bat.setAwake(true);

        NBT.modifyPersistentData(bat, nbt -> {
            nbt.setString("LEASHED_HORSE", horse.getUniqueId().toString());
        });
        Bukkit.getPluginManager().callEvent(new EquineCrossTieLeashEvent(horse));

        event.setCancelled(true);
    }

    @EventHandler
    public void onCrossTieRemoved(EquineCrossTieLeashRemovedEvent event) {
        NBT.modifyPersistentData(event.getHorse(), nbt -> {
            nbt.setString("EQUINE_IS_CROSS_TIED", "false");
        });

        event.getHorse().setJumpStrength(EquineUtils.getBaseJumpPower(event.getHorse()));
        event.getHorse().setAI(true);
    }

    @EventHandler
    public void onCrossTie(EquineCrossTieLeashEvent event) {
        NBT.modifyPersistentData(event.getHorse(), nbt -> {
            nbt.setString("EQUINE_IS_CROSS_TIED", "true");
        });

        event.getHorse().setJumpStrength(0);
        event.getHorse().setAI(false);
    }
}
