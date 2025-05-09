package endcrypt.equinoxEquestrian.hooks.placeholderapi;

import endcrypt.equinoxEquestrian.EquinoxEquestrian;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    private final EquinoxEquestrian plugin;
    public Placeholders(EquinoxEquestrian plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); //
    }

    @Override
    public @NotNull String getIdentifier() {
        return "eq";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion(); //
    }

    @Override
    public boolean persist() {
        return true; //
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("selected_horse")) {
            AbstractHorse horse = plugin.getPlayerDataManager().getPlayerData((Player) player).getSelectedHorse();
            if (horse == null) {
                return "None"; //
            }

            return plugin.getPlayerDataManager().getPlayerData((Player) player).getSelectedHorse().getName(); //
        }

        return null; //
    }



}
