package endcrypt.equinoxEquestrian.utils;

import org.bukkit.Material;

public class MaterialDisplayName {

    public static String getDisplayName(Material material) {
        String materialName = material.name();

        // 1. Replace underscores with spaces
        materialName = materialName.replace("_", " ");

        // 2. Convert to title case (first letter of each word capitalized)
        materialName = toTitleCase(materialName);

        return materialName;
    }


    private static String toTitleCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return java.util.Arrays.stream(str.split("\\s+")) // Split into words
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()) // Capitalize first letter
                .collect(java.util.stream.Collectors.joining(" ")); // Join back with spaces
    }

}
