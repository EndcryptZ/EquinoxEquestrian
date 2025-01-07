package endcrypt.equinoxEquestrian.utils;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static void itemMessage(JavaPlugin plugin, ItemStack item, String defaultItemName, String message, List<String> defaultLore, List<String> loreMessage) {
        ItemMeta itemMeta = item.getItemMeta();

        if(loreMessage != null) {
            itemMeta.setLore(loreMessage);
        }

        itemMeta.setDisplayName(message);

        item.setItemMeta(itemMeta);


        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                itemMeta.setDisplayName(defaultItemName);

                if(defaultLore != null) {
                    itemMeta.setLore(defaultLore);
                } else {
                    List<String> lore = new ArrayList<>();
                    lore.clear();
                    itemMeta.setLore(lore);
                }

                item.setItemMeta(itemMeta);
            }
        }, 20L);
    }
}
