package endcrypt.equinox.equine.updater.horse;

import lombok.Getter;

@Getter
public enum UpdateAction {

    ENTITY_LOAD("Entity Load"),
    ENTITY_UNLOAD("Entity Unload"),
    TELEPORT("Teleport"),
    PLUGIN_DISABLE("Plugin Shutdown Event");

    private final String name;

    UpdateAction(String name) {
        this.name = name;
    }
}
