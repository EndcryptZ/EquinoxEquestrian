package endcrypt.equinox.database.dao;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.DatabaseUtils;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHorses {

    private final Connection connection;
    private final EquinoxEquestrian plugin;


    private final Map<String, String> BASE_SCHEMA = new LinkedHashMap<>() {{
        put("uuid", "UUID");  // Will be converted based on database type
        put("owner_uuid", "UUID");
        put("display_name", "TEXT");
        put("discipline", "TEXT");
        put("breed_1", "TEXT");
        put("breed_2", "TEXT");
        put("prominent_breed", "TEXT");
        put("coat_color", "TEXT");
        put("coat_modifier", "TEXT");
        put("gender", "TEXT");
        put("age", "INTEGER");
        put("height", "REAL");  // Changed from DOUBLE to be compatible with both MySQL and SQLite
        put("trait_1", "TEXT");
        put("trait_2", "TEXT");
        put("trait_3", "TEXT");
        put("claim_time", "BIGINT");  // Changed from LONG to be compatible with both MySQL and SQLite
        put("birth_time", "BIGINT");  // Changed from LONG to be compatible with both MySQL and SQLite
        put("owner_name", "TEXT");
        put("base_speed", "REAL");  // Changed from DOUBLE to be compatible with both MySQL and SQLite
        put("base_jump_power", "REAL"); // Changed from DOUBLE to be compatible with both MySQL and SQLite
        put("skull_id", "TEXT");
        put("last_world", "TEXT");
        put("last_location_x", "REAL"); // Changed from DOUBLE to be compatible with both MySQL and SQLite
        put("last_location_y", "REAL"); // Changed from DOUBLE to be compatible with both MySQL and SQLite
        put("last_location_z", "REAL"); // Changed from DOUBLE to be compatible with both MySQL and SQLite
    }};




    public DatabaseHorses(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.connection = plugin.getDatabaseManager().getConnection();

        try {
            boolean isMySQL = connection.getMetaData()
                    .getDatabaseProductName()
                    .toLowerCase()
                    .contains("mysql");

            Map<String, String> schema = DatabaseUtils.getSchemaForDatabase(BASE_SCHEMA, isMySQL);

            // Create initial table with minimal columns
            try (Statement statement = connection.createStatement()) {
                String uuidType = isMySQL ? "VARCHAR(36)" : "TEXT";
                statement.execute("CREATE TABLE IF NOT EXISTS EQUINE_HORSES (" +
                        "uuid " + uuidType + " PRIMARY KEY, " +
                        "owner_uuid " + uuidType + " NOT NULL" +
                        ")");
            }

            DatabaseUtils.updateTableSchema("EQUINE_HORSES", schema);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create horses database tables: " + e.getMessage());
        }
    }

    public void addHorse(AbstractHorse horse) {
        boolean isSuccess = NBT.getPersistentData(horse, nbt -> {
            String uuid = horse.getUniqueId().toString();
            String ownerUuid = nbt.getString(Keys.OWNER_UUID.getKey());
            String displayName = horse.getCustomName();
            String discipline = nbt.getString(Keys.DISCIPLINE.getKey());
            String breed1 = nbt.getString(Keys.BREED_PREFIX.getKey() + "0");
            String breed2 = nbt.getString(Keys.BREED_PREFIX.getKey() + "1");
            String prominentBreed = nbt.getString(Keys.PROMINENT_BREED.getKey());
            String coatColor = nbt.getString(Keys.COAT_COLOR.getKey());
            String coatModifier = nbt.getString(Keys.COAT_MODIFIER.getKey());
            String gender = nbt.getString(Keys.GENDER.getKey());
            int age = nbt.getInteger(Keys.AGE.getKey());
            double height = nbt.getDouble(Keys.HEIGHT.getKey());
            String trait1 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "0");
            String trait2 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "1");
            String trait3 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "2");
            long claimTime = nbt.getLong(Keys.CLAIM_TIME.getKey());
            long birthTime = nbt.getLong(Keys.BIRTH_TIME.getKey());
            String ownerName = nbt.getString(Keys.OWNER_NAME.getKey());
            double baseSpeed = nbt.getDouble(Keys.BASE_SPEED.getKey());
            double baseJump = nbt.getDouble(Keys.BASE_JUMP.getKey());
            String skullId = nbt.getString(Keys.SKULL_ID.getKey());
            String lastWorld = nbt.getString(Keys.LAST_WORLD.getKey());
            double lastX = nbt.getDouble(Keys.LAST_LOCATION_X.getKey());
            double lastY = nbt.getDouble(Keys.LAST_LOCATION_Y.getKey());
            double lastZ = nbt.getDouble(Keys.LAST_LOCATION_Z.getKey());

            String sql;
            boolean isMySQL;
            try {
                isMySQL = connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (isMySQL) {
                sql = "INSERT INTO EQUINE_HORSES (" +
                        "uuid, owner_uuid, display_name, discipline, breed_1, breed_2, prominent_breed, coat_color, coat_modifier, " +
                        "gender, age, height, trait_1, trait_2, trait_3, claim_time, birth_time, owner_name, base_speed, base_jump_power, skull_id, last_world, last_location_x, last_location_y, last_location_z ) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT OR REPLACE INTO EQUINE_HORSES (" +
                        "uuid, owner_uuid, display_name, discipline, breed_1, breed_2, prominent_breed, coat_color, coat_modifier, " +
                        "gender, age, height, trait_1, trait_2, trait_3, claim_time, birth_time, owner_name, base_speed, base_jump_power, skull_id, last_world, last_location_x, last_location_y, last_location_z ) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, uuid);
                ps.setString(2, ownerUuid);
                ps.setString(3, displayName);
                ps.setString(4, discipline);
                ps.setString(5, breed1);
                ps.setString(6, breed2);
                ps.setString(7, prominentBreed);
                ps.setString(8, coatColor);
                ps.setString(9, coatModifier);
                ps.setString(10, gender);
                ps.setInt(11, age);
                ps.setDouble(12, height);
                ps.setString(13, trait1);
                ps.setString(14, trait2);
                ps.setString(15, trait3);
                ps.setLong(16, claimTime);
                ps.setLong(17, birthTime);
                ps.setString(18, ownerName);
                ps.setDouble(19, baseSpeed);
                ps.setDouble(20, baseJump);
                ps.setString(21, skullId);
                ps.setString(22, lastWorld);
                ps.setDouble(23, lastX);
                ps.setDouble(24, lastY);
                ps.setDouble(25, lastZ);

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to insert horse into database: " + uuid);
                return false;
            }
        });
    }

    public void removeHorse(AbstractHorse horse) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM EQUINE_HORSES WHERE uuid = ?")) {
                preparedStatement.setString(1, horse.getUniqueId().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove horse from database: " + e.getMessage());
        }
    }


    public List<EquineLiveHorse> getPlayerHorses(OfflinePlayer player) {
        List<EquineLiveHorse> horses = new ArrayList<>();
        String sql = "SELECT * FROM EQUINE_HORSES WHERE owner_uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    EquineLiveHorse horse = DatabaseUtils.mapResultSetToHorse(resultSet);
                    if (horse != null) {
                        horses.add(horse);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get player horses: " + e.getMessage());
        }
        return horses;
    }

    public boolean horseExists(AbstractHorse horse) {
        String sql = "SELECT * FROM EQUINE_HORSES WHERE uuid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check horse existence: " + e.getMessage());
        }
        return false;
    }

    public void updateHorse(AbstractHorse horse) {
        boolean isSuccess = NBT.getPersistentData(horse, nbt -> {
            String uuid = horse.getUniqueId().toString();
            String ownerUuid = nbt.getString(Keys.OWNER_UUID.getKey());
            String displayName = horse.getCustomName();
            String discipline = nbt.getString(Keys.DISCIPLINE.getKey());
            String breed1 = nbt.getString(Keys.BREED_PREFIX.getKey() + "0");
            String breed2 = nbt.getString(Keys.BREED_PREFIX.getKey() + "1");
            String prominentBreed = nbt.getString(Keys.PROMINENT_BREED.getKey());
            String coatColor = nbt.getString(Keys.COAT_COLOR.getKey());
            String coatModifier = nbt.getString(Keys.COAT_MODIFIER.getKey());
            String gender = nbt.getString(Keys.GENDER.getKey());
            int age = nbt.getInteger(Keys.AGE.getKey());
            double height = nbt.getDouble(Keys.HEIGHT.getKey());
            String trait1 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "0");
            String trait2 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "1");
            String trait3 = nbt.getString(Keys.TRAIT_PREFIX.getKey() + "2");
            long claimTime = nbt.getLong(Keys.CLAIM_TIME.getKey());
            long birthTime = nbt.getLong(Keys.BIRTH_TIME.getKey());
            String ownerName = nbt.getString(Keys.OWNER_NAME.getKey());
            double baseSpeed = nbt.getDouble(Keys.BASE_SPEED.getKey());
            double baseJump = nbt.getDouble(Keys.BASE_JUMP.getKey());
            String skullId = nbt.getString(Keys.SKULL_ID.getKey());
            String lastWorld = nbt.getString(Keys.LAST_WORLD.getKey());
            double lastX = nbt.getDouble(Keys.LAST_LOCATION_X.getKey());
            double lastY = nbt.getDouble(Keys.LAST_LOCATION_Y.getKey());
            double lastZ = nbt.getDouble(Keys.LAST_LOCATION_Z.getKey());

            String sql;
            boolean isMySQL;
            try {
                isMySQL = connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (isMySQL) {
                sql = "UPDATE EQUINE_HORSES SET " +
                        "owner_uuid = ?, display_name = ?, discipline = ?, breed_1 = ?, breed_2 = ?, prominent_breed = ?, " +
                        "coat_color = ?, coat_modifier = ?, gender = ?, age = ?, height = ?, trait_1 = ?, trait_2 = ?, " +
                        "trait_3 = ?, claim_time = ?, birth_time = ?, owner_name = ?, base_speed = ?, base_jump_power = ?, " +
                        "skull_id = ?, last_world = ?, last_location_x = ?, last_location_y = ?, last_location_z = ? " +
                        "WHERE uuid = ?";
            } else {
                sql = "UPDATE OR REPLACE EQUINE_HORSES SET " +
                        "owner_uuid = ?, display_name = ?, discipline = ?, breed_1 = ?, breed_2 = ?, prominent_breed = ?, " +
                        "coat_color = ?, coat_modifier = ?, gender = ?, age = ?, height = ?, trait_1 = ?, trait_2 = ?, " +
                        "trait_3 = ?, claim_time = ?, birth_time = ?, owner_name = ?, base_speed = ?, base_jump_power = ?, " +
                        "skull_id = ?, last_world = ?, last_location_x = ?, last_location_y = ?, last_location_z = ? " +
                        "WHERE uuid = ?";
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, ownerUuid);
                ps.setString(2, displayName);
                ps.setString(3, discipline);
                ps.setString(4, breed1);
                ps.setString(5, breed2);
                ps.setString(6, prominentBreed);
                ps.setString(7, coatColor);
                ps.setString(8, coatModifier);
                ps.setString(9, gender);
                ps.setInt(10, age);
                ps.setDouble(11, height);
                ps.setString(12, trait1);
                ps.setString(13, trait2);
                ps.setString(14, trait3);
                ps.setLong(15, claimTime);
                ps.setLong(16, birthTime);
                ps.setString(17, ownerName);
                ps.setDouble(18, baseSpeed);
                ps.setDouble(19, baseJump);
                ps.setString(20, skullId);
                ps.setString(21, lastWorld);
                ps.setDouble(22, lastX);
                ps.setDouble(23, lastY);
                ps.setDouble(24, lastZ);
                ps.setString(25, uuid);

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to update horse in database: " + uuid);
                return false;
            }
        });
    }

    private String getDatabaseType(String baseType) throws SQLException {
        boolean isMySQL = connection.getMetaData()
                .getDatabaseProductName()
                .toLowerCase()
                .contains("mysql");

        return switch (baseType.toUpperCase()) {
            case "UUID" -> isMySQL ? "VARCHAR(36)" : "TEXT";
            case "TEXT" -> isMySQL ? "VARCHAR(255)" : "TEXT";
            case "LONG" -> isMySQL ? "BIGINT" : "INTEGER";
            case "DOUBLE" -> isMySQL ? "DOUBLE PRECISION" : "REAL";
            case "INTEGER" -> isMySQL ? "INT" : "INTEGER";
            default -> baseType;
        };
    }


}
