package endcrypt.equinox.utils;

import com.maximde.hologramlib.HologramLib;
import com.maximde.hologramlib.hologram.RenderMode;
import com.maximde.hologramlib.hologram.TextHologram;
import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HoloUtils {

    public static void createPersistentHolo(String id, String text, Location location) {
        TextHologram textHologram = new TextHologram(id, RenderMode.NEARBY);
        textHologram
                .setMiniMessageText(text)
                .update();

        EquinoxEquestrian.instance.getHologramManager().spawn(textHologram, location, true);


    }

    public static void removePersistentHolo(String id) {
        for (String ids : EquinoxEquestrian.instance.getHologramManager().getHologramIds()) {
            if(ids.equalsIgnoreCase(id)) {
                EquinoxEquestrian.instance.getHologramManager().remove(ids, true);
                return;
            }
        }

    }
}
