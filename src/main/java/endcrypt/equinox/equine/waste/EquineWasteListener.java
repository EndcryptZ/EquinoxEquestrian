package endcrypt.equinox.equine.waste;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.dao.DatabaseWaste;
import endcrypt.equinox.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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

        DatabaseWaste wasteDatabase = plugin.getDatabaseManager().getDatabaseWaste();
        if (!wasteDatabase.hasWasteBlock(event.getClickedBlock().getLocation())) return;

        Player player = event.getPlayer();
        if (!isUsingShovel(event)) {
            player.sendMessage(ColorUtils.color("<red>You need a shovel to clean up horse waste."));
            return;
        }

        wasteDatabase.removeWasteBlock(event.getClickedBlock().getLocation());
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
        if (!plugin.getDatabaseManager().getDatabaseWaste().hasWasteBlock(event.getBlock().getLocation())) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(ColorUtils.color("<red>Use a shovel and right-click to clean up horse waste."));
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
