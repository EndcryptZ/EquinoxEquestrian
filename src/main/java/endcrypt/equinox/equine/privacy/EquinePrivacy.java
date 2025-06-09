package endcrypt.equinox.equine.privacy;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.entity.AbstractHorse;

public class EquinePrivacy {

    public void setPrivacy(boolean isPublic, AbstractHorse horse) {
        NBT.modifyPersistentData(horse, nbt -> {
            nbt.setString(Keys.IS_PUBLIC.getKey(), isPublic ? "true" : "false");
        });
    }
}
