package endcrypt.equinox.equine.items.misc;

import endcrypt.equinox.EquinoxEquestrian;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WaterBucketListener implements Listener {

    private final EquinoxEquestrian plugin;
    public WaterBucketListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWaterBucket(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getType() != Material.WATER_BUCKET) return;

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() == Material.CAULDRON) return;

        BlockFace face = event.getBlockFace();
        Block placeBlock = clickedBlock.getRelative(face);

        // Only place if the block is air
        if (!placeBlock.getType().isAir()) return;

        // Decrease stack (if > 1), otherwise remove item
        ItemStack item = event.getItem();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().removeItem(item);
        }


        // Try to place water
        placeBlock.setType(Material.WATER);

        player.swingMainHand();
        event.setCancelled(true);
    }
}
