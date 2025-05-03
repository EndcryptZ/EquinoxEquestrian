package endcrypt.equinoxEquestrian.horse;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import endcrypt.equinoxEquestrian.api.events.EquinePlayerSelectHorseEvent;
import endcrypt.equinoxEquestrian.horse.enums.Item;
import org.bukkit.ChatColor;
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
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Left-Click &f&o" + horse.getName() + " &7to use item(s)."));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7Right-Click &f&o" + horse.getName() + " &7to cancel."));
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
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cThat is not " + item.getName() + "!"));
            event.setCancelled(true);
            return;
        }


        if (playerHorse != horse) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cYou are not grooming this horse!"));
            return;
        }

        String isEqualItem = NBT.get(heldItem, nbt -> (String) nbt.getString(item.getNbt()));
        if(!isEqualItem.equalsIgnoreCase("true")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&cThat is not " + item.getName() + "!"));
            return;
        }


        groomMap.get(player).setGroomTimes(groomMap.get(player).getGroomTimes() + 1);
        damageGroomItem(player, heldItem);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getPrefix() + "&7You have used &f" + item.getName() + " &7on &f&o" + horse.getName() + " &7(&f" + groomMap.get(player).getGroomTimes() + "/2&7)."));

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
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

            // Ensure at least 2 lines exist
            while (lore.size() < 2) {
                lore.add("");
            }

            lore.set(1, "§fDurability: " + (durabilityHealth - 1));
            meta.setLore(lore);
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
