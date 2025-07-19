package endcrypt.equinox.equine.waste;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EquineWasteListener implements Listener {


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
