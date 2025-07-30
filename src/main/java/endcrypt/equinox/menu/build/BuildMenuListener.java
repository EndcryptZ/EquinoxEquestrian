package endcrypt.equinox.menu.build;

import com.samjakob.spigui.menu.SGMenu;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BuildMenuListener implements Listener {

    private final EquinoxEquestrian plugin;

    public BuildMenuListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // Events
    @EventHandler (priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        final String originalMessage = ColorUtils.stripColor(LegacyComponentSerializer.legacySection().deserialize(event.getMessage()));
        if(!plugin.getBuildMenuManager().getPlayerEquineHorseInput().containsKey(event.getPlayer())) {
            return;
        }


        if(originalMessage.equalsIgnoreCase("cancel")) {
            plugin.getBuildMenuManager().getBuildMenu().openWithParameters(event.getPlayer(),
                    plugin.getBuildMenuManager().getPlayerEquineHorseInput().get(event.getPlayer())
            );
            plugin.getBuildMenuManager().getPlayerEquineHorseInput().remove(event.getPlayer());
            event.setCancelled(true);
            return;
        }

        if(originalMessage.length() < 2) {
            event.getPlayer().sendMessage(ColorUtils.color("<prefix><red>Name too short! Please keep it above 1 character.",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            event.setCancelled(true);
            return;
        }

        if(originalMessage.length() > 30) {
            event.getPlayer().sendMessage(ColorUtils.color("<prefix><red>Name too long! Please keep it under 16 characters.",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
            event.setCancelled(true);
            return;
        }

        plugin.getBuildMenuManager().getPlayerEquineHorseInput().get(event.getPlayer()).setName(LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacySection().deserialize(event.getMessage())));
        plugin.getBuildMenuManager().getBuildMenu().openWithParameters(event.getPlayer(),
                plugin.getBuildMenuManager().getPlayerEquineHorseInput().get(event.getPlayer())
        );
        plugin.getBuildMenuManager().getPlayerEquineHorseInput().remove(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(!plugin.getBuildMenuManager().getPlayerEquineHorseInput().containsKey(event.getPlayer())) {
            return;
        }
        event.getPlayer().sendMessage(ColorUtils.color("<prefix><red>Commands are disabled during Horse Creation!",
                Placeholder.parsed("prefix", plugin.getPrefix())));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!plugin.getBuildMenuManager().getPlayerEquineHorseInput().containsKey(event.getPlayer())) {
            return;
        }

        plugin.getBuildMenuManager().getPlayerEquineHorseInput().remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof SGMenu) {
            SGMenu menu = (SGMenu) event.getInventory().getHolder();
            if (menu.getName().contains("Select")) { // assuming your sub-menus have "Select" in their title
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Player player = (Player) event.getPlayer();
                    plugin.getBuildMenuManager().getBuildMenu().openWithParameters(player, plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().get(event.getPlayer()));
                    plugin.getBuildMenuManager().getPlayerEquineSubMenuInput().remove(event.getPlayer());
                }, 1L);
            }
        }
    }
}
