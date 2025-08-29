package endcrypt.equinox.equine.hunger;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.EquineUtils;
import endcrypt.equinox.equine.nbt.Keys;
import endcrypt.equinox.utils.ColorUtils;
import endcrypt.equinox.utils.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EquineHungerListener implements Listener {


    private final EquinoxEquestrian plugin;
    public EquineHungerListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    List<Material> allowedFoodsFromHand = Arrays.asList(Material.HAY_BLOCK, Material.DRIED_KELP_BLOCK);

    @EventHandler
    public void onHorseEat(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;

        if (!allowedFoodsFromHand.contains(item.getType())) return;

        if (!EquineUtils.isLivingEquineHorse(horse)) {
            event.getPlayer().sendMessage(MessageUtils.cantInteractWithNotEquineHorse());
            horse.remove();
            return;
        }

        if (Keys.readBoolean(horse, Keys.IS_IN_WATER_TASK)) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<prefix><red>This horse is currently busy drinking water.",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }

        if (Keys.readBoolean(horse, Keys.IS_IN_FOOD_TASK)) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<prefix><red>This horse is currently busy foraging for food.",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }


        double hungerPercentage = Keys.readDouble(horse, Keys.HUNGER_PERCENTAGE);
        hungerPercentage = Math.min(100, hungerPercentage + 2);
        Keys.writeDouble(horse, Keys.HUNGER_PERCENTAGE, hungerPercentage);

        String formattedItemName = Arrays.stream(item.getType().name().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        horse.getWorld().playSound(horse.getLocation(), Sound.ENTITY_HORSE_EAT, 1.0f, 1.0f);

        event.getPlayer().sendMessage(ColorUtils.color(
                "<prefix><green>You fed <horse><reset> <green><item> and restored 2% hunger! Current hunger: <hunger>%",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name())),
                Placeholder.parsed("item", formattedItemName),
                Placeholder.parsed("hunger", String.format("%.2f", hungerPercentage))
        ));

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }


        event.getPlayer().swingMainHand();
        event.setCancelled(true);
    }
}
