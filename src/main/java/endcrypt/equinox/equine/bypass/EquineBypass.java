package endcrypt.equinox.equine.bypass;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EquineBypass {

    private static final ArrayList<Player> bypassPlayersList = new ArrayList<>();

    public static void add(Player player) {
        bypassPlayersList.add(player);
    }

    public static void remove(Player player) {
        bypassPlayersList.remove(player);
    }
    public static boolean hasBypass(Player player) {
        return bypassPlayersList.contains(player);
    }

}
