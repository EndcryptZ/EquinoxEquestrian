package endcrypt.equinox.equine.groom;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.api.events.EquinePlayerSelectHorseEvent;
import endcrypt.equinox.equine.items.Item;
import endcrypt.equinox.utils.ColorUtils;
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

public class EquineGroomListener implements Listener {

    private final EquinoxEquestrian plugin;

    public EquineGroomListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHorseSelect(EquinePlayerSelectHorseEvent event) {
        Player player = event.getPlayer();
        if(plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player) != null) {
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

        if(plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player) == null) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Item item = plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).getItem();
        AbstractHorse playerHorse = plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).getHorse();
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


        plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).setGroomTimes(plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).getGroomTimes() + 1);
        plugin.getEquineManager().getEquineGroomManager().damageGroomItem(player, heldItem);

        player.sendMessage(ColorUtils.color("<prefix><gray>You have used <white><item> <gray>on <white><italic><horse> <gray>(<white><times>/2<gray>).",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("item", item.getName()),
                Placeholder.parsed("horse", horse.getName()),
                Placeholder.parsed("times", String.valueOf(plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).getGroomTimes()))));

        if(plugin.getEquineManager().getEquineGroomManager().getGroomMap().get(player).getGroomTimes() == 2) {
            player.openInventory(plugin.getMenuManager().getHorseMenuManager().getGroomMenu().menu(player, playerHorse));
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            plugin.getEquineManager().getEquineGroomManager().resetGroom(player);
        }


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getEquineManager().getEquineGroomManager().resetGroom(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getEquineManager().getEquineGroomManager().resetGroom(event.getPlayer());
    }
}
