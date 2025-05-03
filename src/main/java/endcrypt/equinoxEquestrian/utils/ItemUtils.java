package endcrypt.equinoxEquestrian.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ItemUtils {

    public static void itemMessage(JavaPlugin plugin, ItemStack item, String defaultItemName, String message, List<String> defaultLore, List<String> loreMessage) {
        ItemMeta itemMeta = item.getItemMeta();

        assert itemMeta != null;
        List<String> veryDefaultLore = itemMeta.getLore();

        if(loreMessage != null) {
            itemMeta.setLore(loreMessage);
        }

        itemMeta.setDisplayName(message);

        item.setItemMeta(itemMeta);


        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            itemMeta.setDisplayName(defaultItemName);

            if(defaultLore != null) {
                itemMeta.setLore(defaultLore);
            } else {
                itemMeta.setLore(veryDefaultLore);
            }

            item.setItemMeta(itemMeta);
        }, 20L);
    }
}
