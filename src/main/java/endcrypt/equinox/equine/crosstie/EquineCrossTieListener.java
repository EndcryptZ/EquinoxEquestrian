package endcrypt.equinox.equine.crosstie;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerCrossTieLeashEvent;
import endcrypt.equinox.api.events.EquinePlayerCrossTieLeashRemovedEvent;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
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


        // Bug Fixing Method
        if (!horse.isLeashed()) {
            if(Keys.readPersistentData(horse, Keys.IS_CROSS_TIED)) {
                Bukkit.getPluginManager().callEvent(new EquinePlayerCrossTieLeashRemovedEvent(horse));
            }
            return;
        }

        Entity leashHolder = horse.getLeashHolder();

        if(!(leashHolder instanceof LeashHitch)) {
            return;
        }

        if (heldItem.getType() != Material.LEAD) {
            return;
        }

        if (!EquineUtils.hasPermissionToHorse(player, horse)) {
            player.sendMessage(MessageUtils.cantInteractWithHorse(horse));
            event.setCancelled(true);
            return;
        }

        if (Keys.readPersistentData(horse, Keys.IS_CROSS_TIED)) {
            event.setCancelled(true);
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
        bat.setPersistent(true); // Make the bat persistent so it doesn't get deleted when the player leaves the world
        bat.setAwake(true);

        NBT.modifyPersistentData(bat, nbt -> {
            nbt.setString("LEASHED_HORSE", horse.getUniqueId().toString());
        });
        Bukkit.getPluginManager().callEvent(new EquinePlayerCrossTieLeashEvent(player, horse));

        event.setCancelled(true);
    }

    @EventHandler
    public void onCrossTieRemoved(EquinePlayerCrossTieLeashRemovedEvent event) {
        NBT.modifyPersistentData(event.getHorse(), nbt -> {
            nbt.setString(Keys.IS_CROSS_TIED.getKey(), "false");
        });

        plugin.getEquineManager().getEquineCrossTie().remove(event.getHorse());
        event.getHorse().setJumpStrength(Keys.readPersistentData(event.getHorse(), Keys.BASE_JUMP));
        event.getHorse().setAI(true);
    }

    @EventHandler
    public void onCrossTie(EquinePlayerCrossTieLeashEvent event) {
        NBT.modifyPersistentData(event.getHorse(), nbt -> {
            nbt.setString(Keys.IS_CROSS_TIED.getKey(), "true");
        });

        plugin.getEquineManager().getEquineCrossTie().add(event.getHorse());
        event.getHorse().setJumpStrength(0);
        event.getHorse().setAI(false);
    }

    @EventHandler
    public void onCrosstieHorsesLoad(EntitiesLoadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(!(boolean) Keys.readPersistentData(horse, Keys.IS_CROSS_TIED)) {
                continue;
            }

            // Bug Fixing Method
            if (!horse.isLeashed()) {
                Bukkit.getPluginManager().callEvent(new EquinePlayerCrossTieLeashRemovedEvent(horse));
                continue;
            }

            plugin.getEquineManager().getEquineCrossTie().add(horse);
        }
    }

    @EventHandler
    public void onCrosstieHorsesUnload(EntitiesUnloadEvent event) {
        for(Entity entity : event.getEntities()) {
            if(!(entity instanceof AbstractHorse horse)) {
                continue;
            }

            if(!EquineUtils.isLivingEquineHorse(horse)) {
                continue;
            }

            if(!(boolean) Keys.readPersistentData(horse, Keys.IS_CROSS_TIED)) {
                continue;
            }

            // Bug Fixing Method
            if (!horse.isLeashed()) {
                Bukkit.getPluginManager().callEvent(new EquinePlayerCrossTieLeashRemovedEvent(horse));
            }

            plugin.getEquineManager().getEquineCrossTie().remove(horse);
        }
    }
}
