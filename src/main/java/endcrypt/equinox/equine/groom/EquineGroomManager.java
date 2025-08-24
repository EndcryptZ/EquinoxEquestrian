package endcrypt.equinox.equine.groom;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.items.Item;
import endcrypt.equinox.utils.ColorUtils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EquineGroomManager implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineGroomManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    private final Map<Player, EquineGroomData> groomMap = new HashMap<>();


    public boolean initializeGroom(Player player, AbstractHorse horse, Item item) {

        boolean containPass = false;

        for(ItemStack itemStack : player.getInventory().getContents()) {

            if(itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }

            String isEqualItem = NBT.get(itemStack, nbt -> (String) nbt.getString(item.getNbt()));
            if(isEqualItem.equalsIgnoreCase("true")) {
                containPass = true;
                break;
            }
        }

        if(!containPass) {
            return false;
        }

        groomMap.put(player, new EquineGroomData(item, horse, 0));

        player.closeInventory();
        player.sendMessage(ColorUtils.color("<prefix><gray>Left-Click <white><italic><horse> <gray>to use item(s).",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))));

        player.sendMessage(ColorUtils.color("<prefix><gray>Right-Click <white><italic><horse> <gray>to cancel.",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name()))));
        return true;
    }


    public void damageGroomItem(Player player, ItemStack item) {

        int durabilityHealth = NBT.get(item, nbt -> (int) nbt.getInteger("EQUINE_ITEM_DURABILITY"));

        if (durabilityHealth == 1) {
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            return;
        }

        // Decrease durability
        NBT.modify(item, NBT -> {
            NBT.setInteger("EQUINE_ITEM_DURABILITY", durabilityHealth - 1);
        });

        // Update lore
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<Component> lore = meta.hasLore() ? meta.lore() : new ArrayList<>();

            // Ensure at least 2 lines exist
            while (lore.size() < 2) {
                lore.add(ColorUtils.color(""));
            }

            lore.set(1, ColorUtils.color("<gray>Durability: <white><item_durability>/2<gray>.",
                    Placeholder.parsed("item_durability", String.valueOf(durabilityHealth - 1))));
            meta.lore(lore);
            item.setItemMeta(meta);
        }
    }
    public void resetGroom(Player player) {
        groomMap.remove(player);
    }

    public boolean isPlayerGrooming(Player player) {
        if(groomMap.containsKey(player)) {
            return true;
        }

        return false;
    }

    public AbstractHorse getHorse(Player player) {
        return groomMap.get(player).getHorse();
    }

}
