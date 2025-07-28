package endcrypt.equinox.permissions;

import lombok.Getter;

@Getter
public enum PermissionsEnum {

    PERMISSION_HORSE_LIMIT("equinox.horse.limit");


    private final String permission;
    PermissionsEnum(String permission) {
        this.permission = permission;
    }
}