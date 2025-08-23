package endcrypt.equinox.equine.thirst;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineUtils;
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

public class EquineThirstListener implements Listener {

    private final EquinoxEquestrian plugin;
    public EquineThirstListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    List<Material> allowedDrinksFromHand = List.of(Material.WATER_BUCKET);

    @EventHandler
    public void onHorseEat(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse horse)) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;

        if (!allowedDrinksFromHand.contains(item.getType())) return;

        if (!EquineUtils.isLivingEquineHorse(horse)) {
            event.getPlayer().sendMessage(MessageUtils.cantInteractWithNotEquineHorse());
            horse.remove();
            return;
        }

        if (Keys.readPersistentData(horse, Keys.IS_IN_WATER_TASK)) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<prefix><red>This horse is currently busy drinking water.",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }

        if (Keys.readPersistentData(horse, Keys.IS_IN_FOOD_TASK)) {
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(
                    "<prefix><red>This horse is currently busy foraging for food.",
                    Placeholder.parsed("prefix", plugin.getPrefix())
            ));
            return;
        }


        double thirstPercentage = Keys.readPersistentData(horse, Keys.THIRST_PERCENTAGE);
        thirstPercentage = Math.min(100, thirstPercentage + 2);
        Keys.writePersistentData(horse, Keys.THIRST_PERCENTAGE, thirstPercentage);

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }

        String formattedItemName = Arrays.stream(item.getType().name().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        horse.getWorld().playSound(horse.getLocation(), Sound.ENTITY_WITCH_DRINK, 1.0f, 1.0f);

        event.getPlayer().sendMessage(ColorUtils.color(
                "<prefix><horse> <green>drank <item>, restoring 2% thirst! Current thirst: <thirst>%",
                Placeholder.parsed("prefix", plugin.getPrefix()),
                Placeholder.parsed("horse", MiniMessage.miniMessage().serialize(horse.name())),
                Placeholder.parsed("item", formattedItemName),
                Placeholder.parsed("thirst", String.format("%.2f", thirstPercentage))
        ));

        event.getPlayer().swingMainHand();
        event.setCancelled(true);
    }
}
