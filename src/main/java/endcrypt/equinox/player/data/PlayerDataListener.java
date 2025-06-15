package endcrypt.equinox.player.data;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.utils.ColorUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDataListener implements Listener {

    private final EquinoxEquestrian plugin;
    public PlayerDataListener(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!plugin.isDatabaseLoaded()) {
            event.getPlayer().kick(ColorUtils.color("<prefix><red>The database is not loaded yet! Please wait for the server to load the database.",
                    Placeholder.parsed("prefix", plugin.getPrefix())));
        }

        plugin.getPlayerDataManager().load(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        plugin.getPlayerDataManager().save(event.getPlayer());
    }
}
