package endcrypt.equinox.equine.waste;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.dao.DatabaseWaste;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EquineWasteListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineWasteListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void wasteBlockInteractionEvent(PlayerInteractEvent event) {
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(event.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }


        Block block = event.getClickedBlock();
        Location blockLocation = event.getClickedBlock().getLocation();
        DatabaseWaste wasteDatabase = plugin.getDatabaseManager().getDatabaseWaste();
        if (!wasteDatabase.hasWasteBlock(blockLocation)) return;

        Player player = event.getPlayer();
        if (!isUsingShovel(event)) {
            player.sendMessage(ColorUtils.color("<red>You need a shovel to clean up horse waste."));
            return;
        }

        String holoId = "Waste" + blockLocation.getWorld().getName() + block.getX() + block.getY() + block.getZ();
        wasteDatabase.removeWasteBlock(blockLocation);
        plugin.getHologramManager().removeHolo(holoId);
        event.getClickedBlock().setType(Material.AIR);

        int exp;
        if (Math.random() < 0.8) { // 80% chance
            exp = new Random().nextInt(2) + 1; // 1-2
        } else {
            exp = new Random().nextInt(3) + 3; // 3-5
        }

        plugin.getEquineManager().getEquineLeveling().addExp(player, exp, true);
    }

    @EventHandler
    public void onWasteBlockBreak(BlockBreakEvent event) {
        Block above = event.getBlock().getRelative(BlockFace.UP);
        if (plugin.getDatabaseManager().getDatabaseWaste().hasWasteBlock(above.getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ColorUtils.color("<red>Please clean the horse waste above before breaking this block."));
            return;
        }


        if (!plugin.getDatabaseManager().getDatabaseWaste().hasWasteBlock(event.getBlock().getLocation())) return;


        event.setCancelled(true);
        event.getPlayer().sendMessage(ColorUtils.color("<red>Use a shovel and right-click to clean up horse waste."));
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        List<Block> toRemove = new ArrayList<>();

        for (Block block : event.blockList()) {
            Block above = block.getRelative(BlockFace.UP);

            if (plugin.getDatabaseManager().getDatabaseWaste().hasWasteBlock(above.getLocation())) {
                toRemove.add(block);
                toRemove.add(above);
            }
        }

        // prevent the blocks from being affected
        event.blockList().removeAll(toRemove);
    }

    // Hologram Loader
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        plugin.getDatabaseManager().getDatabaseWaste().getPeeAndPooLocationsInChunk(event.getChunk()).forEach(location -> {
            plugin.getLogger().info("Trying to load waste holo at " + location);
            Block block = location.getBlock();
            Location blockLocation = block.getLocation();
            String holoId = "Waste" + blockLocation.getWorld().getName() + block.getX() + block.getY() + block.getZ();
            String holoText = block.getType().equals(Material.YELLOW_CARPET) ? "<yellow>Pee" : "<gold>Poop";
            Location holoLoc = block.getLocation().add(0.5, 1, 0.5);
            plugin.getHologramManager().createTemporaryHolo(holoId, holoText, holoLoc);
        });
    }

    private boolean isUsingShovel(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return false;

        ItemStack item = event.getItem();
        if (item == null) return false;

        Material type = item.getType();
        return type == Material.WOODEN_SHOVEL
                || type == Material.STONE_SHOVEL
                || type == Material.IRON_SHOVEL
                || type == Material.GOLDEN_SHOVEL
                || type == Material.DIAMOND_SHOVEL
                || type == Material.NETHERITE_SHOVEL;
    }

}
