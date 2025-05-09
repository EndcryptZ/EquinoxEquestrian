package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.api.events.EquinePlayerSelectHorseEvent;
import endcrypt.equinoxEquestrian.horse.enums.Item;
import endcrypt.equinoxEquestrian.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquineGroom implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineGroom(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
                Placeholder.parsed("horse", horse.getName())));

        player.sendMessage(ColorUtils.color("<prefix><gray>Right-Click <white><italic><horse> <gray>to cancel.",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", horse.getName())));
        return true;
    }

    @EventHandler
    public void onHorseSelect(EquinePlayerSelectHorseEvent event) {
        Player player = event.getPlayer();
        if(groomMap.get(player) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractHorse(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player player)) {
            return;
        }

        if(!(event.getEntity() instanceof AbstractHorse horse)) {
            return;
        }

        if(groomMap.get(player) == null) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Item item = groomMap.get(player).getItem();
        AbstractHorse playerHorse = groomMap.get(player).getHorse();
        event.setCancelled(true);

        if(heldItem.getType() == Material.AIR) {
            player.sendMessage(ColorUtils.color("<prefix><red>That is not <item>!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("item", item.getName())
                    ));
            event.setCancelled(true);
            return;
        }


        if (playerHorse != horse) {
            player.sendMessage(ColorUtils.color("<prefix><red>You are not grooming this horse!",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }

        String isEqualItem = NBT.get(heldItem, nbt -> (String) nbt.getString(item.getNbt()));
        if(!isEqualItem.equalsIgnoreCase("true")) {
            player.sendMessage(ColorUtils.color("<prefix><red>That is not <item>!",
                    Placeholder.parsed("prefix", plugin.getPrefix()),
                    Placeholder.parsed("item", item.getName())
            ));
            return;
        }


        groomMap.get(player).setGroomTimes(groomMap.get(player).getGroomTimes() + 1);
        damageGroomItem(player, heldItem);

        player.sendMessage(ColorUtils.color("<prefix><gray>You have used <white><item> <gray>on <white><italic><horse> <gray>(<white><times>/2<gray>).",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("item", item.getName()),
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("times", String.valueOf(groomMap.get(player).getGroomTimes()))));

        if(groomMap.get(player).getGroomTimes() == 2) {
            player.openInventory(plugin.getHorseMenu().getGroomMenu().menu(player, playerHorse));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            resetGroom(player);
        }


    }

    private void damageGroomItem(Player player, ItemStack item) {

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


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        resetGroom(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        resetGroom(event.getPlayer());
    }


}
