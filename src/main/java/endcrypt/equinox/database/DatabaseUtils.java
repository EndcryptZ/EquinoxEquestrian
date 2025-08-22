package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.attributes.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DatabaseUtils {

    private static final EquinoxEquestrian plugin = EquinoxEquestrian.instance;
    private final static Connection connection = plugin.getDatabaseManager().getConnection();

    public static EquineLiveHorse mapResultSetToHorse(ResultSet resultSet) {
        try {
            EquineLiveHorse horse = new EquineLiveHorse();
            horse.setUuid(UUID.fromString(resultSet.getString("uuid")));
            horse.setOwnerUUID(resultSet.getString("owner_uuid"));
            horse.setName(resultSet.getString("display_name"));
            horse.setDiscipline(Discipline.getDisciplineByName(resultSet.getString("discipline")));

            List<Breed> breeds = new ArrayList<>();
            String breed1 = resultSet.getString("breed_1");
            String breed2 = resultSet.getString("breed_2");
            if (breed1 != null) breeds.add(Breed.getBreedByName(breed1));
            if (breed2 != null) breeds.add(Breed.getBreedByName(breed2));
            horse.setBreeds(breeds);

            horse.setProminentBreed(Breed.getBreedByName(resultSet.getString("prominent_breed")));
            horse.setCoatColor(CoatColor.getByName(resultSet.getString("coat_color")));
            horse.setCoatModifier(CoatModifier.getByName(resultSet.getString("coat_modifier")));
            horse.setGender(Gender.valueOf(resultSet.getString("gender")));
            horse.setAge(resultSet.getInt("age"));
            horse.setHeight(Height.getByHands(resultSet.getDouble("height")));

            List<Trait> traits = new ArrayList<>();
            String trait1 = resultSet.getString("trait_1");
            String trait2 = resultSet.getString("trait_2");
            String trait3 = resultSet.getString("trait_3");
            if (trait1 != null) traits.add(Trait.getTraitByName(trait1));
            if (trait2 != null) traits.add(Trait.getTraitByName(trait2));
            if (trait3 != null) traits.add(Trait.getTraitByName(trait3));
            horse.setTraits(traits);

            horse.setClaimTime(resultSet.getLong("claim_time"));
            horse.setBirthTime(resultSet.getLong("birth_time"));
            horse.setOwnerName(resultSet.getString("owner_name"));
            horse.setBaseSpeed(resultSet.getDouble("base_speed"));
            horse.setBaseJumpPower(resultSet.getDouble("base_jump_power"));
            horse.setSkullId(resultSet.getString("skull_id"));

            String worldName = resultSet.getString("last_world");
            if (worldName != null) {
                World world = Bukkit.getWorld(worldName);
                double x = resultSet.getDouble("last_location_x");
                double y = resultSet.getDouble("last_location_y");
                double z = resultSet.getDouble("last_location_z");
                horse.setLastLocation(new Location(world, x, y, z));
            }

            horse.setPregnant(resultSet.getBoolean("is_pregnant"));
            horse.setInHeat(resultSet.getBoolean("is_in_heat"));

            horse.setHungerPercentage(resultSet.getDouble("hunger_percentage"));
            horse.setLastHungerUpdate(resultSet.getLong("last_hunger_update"));
            horse.setThirstPercentage(resultSet.getDouble("thirst_percentage"));
            horse.setLastThirstUpdate(resultSet.getLong("last_thirst_update"));

            horse.setWalkSpeed(resultSet.getDouble("walk_speed"));
            horse.setTrotSpeed(resultSet.getDouble("trot_speed"));
            horse.setCanterSpeed(resultSet.getDouble("canter_speed"));
            horse.setGallopSpeed(resultSet.getDouble("gallop_speed"));

            return horse;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to map result set to horse: " + e.getMessage());
            return null;
        }
    }

    public static void updateTableSchema(String tableName, Map<String, String> desiredSchema) {
        Set<String> existingColumns = new HashSet<>();

        try {
            // Get database type
            boolean isMySQL = connection.getMetaData()
                    .getDatabaseProductName()
                    .toLowerCase()
                    .contains("mysql");

            // Get existing column names from the table
            try (Statement stmt = connection.createStatement()) {
                String columnQuery;
                if (isMySQL) {
                    columnQuery = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE TABLE_NAME = '" + tableName + "'";
                } else {
                    columnQuery = "PRAGMA table_info(" + tableName + ")";
                }

                try (ResultSet rs = stmt.executeQuery(columnQuery)) {
                    while (rs.next()) {
                        // MySQL uses COLUMN_NAME, SQLite uses 'name' from PRAGMA
                        String columnName = isMySQL ?
                                rs.getString("COLUMN_NAME") :
                                rs.getString("name");
                        existingColumns.add(columnName.toLowerCase());
                    }
                }
            }

            // Add only real columns (skip constraints like PRIMARY KEY)
            try (Statement stmt = connection.createStatement()) {
                for (Map.Entry<String, String> entry : desiredSchema.entrySet()) {
                    String columnName = entry.getKey();
                    String columnDefinition = entry.getValue();

                    // Skip constraint entries
                    if (columnName.equalsIgnoreCase("PRIMARY KEY")
                            || columnName.startsWith("__constraint_")) {
                        continue;
                    }

                    if (!existingColumns.contains(columnName.toLowerCase())) {
                        String sql = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition;

                        // MySQL doesn't allow multiple ADD COLUMN in single ALTER TABLE
                        stmt.execute(sql);
                        plugin.getLogger().info("Added new column '" + columnName + "' to table '" + tableName + "'");
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to update table schema for table '" + tableName + "': " + e.getMessage());
        }
    }


    public static String getColumnType(String baseType, boolean isMySQL) {
        return switch (baseType.toUpperCase()) {
            case "TEXT" -> isMySQL ? "VARCHAR(255)" : "TEXT";
            case "UUID" -> isMySQL ? "VARCHAR(36)" : "TEXT";
            case "LONG", "BIGINT" -> "BIGINT"; // both MySQL and SQLite accept this
            case "DOUBLE", "REAL" -> "DOUBLE PRECISION"; // MySQL accepts this, SQLite maps it to REAL internally
            case "INTEGER" -> "INTEGER"; // both support this
            default -> baseType; // fallback (e.g., REAL, BOOLEAN, etc.)
        };
    }

    public static Map<String, String> getSchemaForDatabase(Map<String, String> baseSchema, boolean isMySQL) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : baseSchema.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals("PRIMARY KEY")) {
                value = getColumnType(value, isMySQL);
            }
            result.put(key, value);
        }
        return result;
    }

}
