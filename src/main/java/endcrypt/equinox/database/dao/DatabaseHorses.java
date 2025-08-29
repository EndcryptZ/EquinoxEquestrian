package endcrypt.equinox.database.dao;

import de.tr7zw.changeme.nbtapi.NBT;
import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.DatabaseUtils;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;

import java.sql.*;
import java.util.*;

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
        put("is_pregnant", "TINYINT(1)"); // Boolean-like value, 0 = false, 1 = true
        put("is_in_heat", "TINYINT(1)");  // Boolean-like value, 0 = false, 1 = true
        put("hunger_percentage", "REAL"); // 0–100, decimal safe
        put("last_hunger_update", "BIGINT"); // Epoch millis
        put("thirst_percentage", "REAL"); // 0–100, decimal safe
        put("last_thirst_update", "BIGINT"); // Epoch millis
        put("walk_speed", "REAL");
        put("trot_speed", "REAL");
        put("canter_speed", "REAL");
        put("gallop_speed", "REAL");
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
            boolean isPregnant = nbt.getBoolean(Keys.IS_PREGNANT.getKey());
            boolean isInHeat = nbt.getBoolean(Keys.IS_IN_HEAT.getKey());
            double hungerPercentage = nbt.getDouble(Keys.HUNGER_PERCENTAGE.getKey());
            long lastHungerUpdate = nbt.getLong(Keys.LAST_HUNGER_UPDATE.getKey());
            double thirstPercentage = nbt.getDouble(Keys.THIRST_PERCENTAGE.getKey());
            long lastThirstUpdate = nbt.getLong(Keys.LAST_THIRST_UPDATE.getKey());
            double walkSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());
            double trotSpeed = nbt.getDouble(Keys.TROT_SPEED.getKey());
            double canterSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());
            double gallopSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());

            boolean isMySQL;
            try {
                isMySQL = connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to detect database type", e);
            }

            String sql = (isMySQL ? "INSERT INTO " : "INSERT OR REPLACE INTO ") + "EQUINE_HORSES (" +
                    "uuid, owner_uuid, display_name, discipline, breed_1, breed_2, prominent_breed, coat_color, coat_modifier, " +
                    "gender, age, height, trait_1, trait_2, trait_3, claim_time, birth_time, owner_name, base_speed, base_jump_power, " +
                    "skull_id, last_world, last_location_x, last_location_y, last_location_z, is_pregnant, is_in_heat, hunger_percentage, " +
                    "last_hunger_update, thirst_percentage, last_thirst_update, walk_speed, trot_speed, canter_speed, gallop_speed" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                ps.setBoolean(26, isPregnant);
                ps.setBoolean(27, isInHeat);
                ps.setDouble(28, hungerPercentage);
                ps.setLong(29, lastHungerUpdate);
                ps.setDouble(30, thirstPercentage);
                ps.setLong(31, lastThirstUpdate);
                ps.setDouble(32, walkSpeed);
                ps.setDouble(33, trotSpeed);
                ps.setDouble(34, canterSpeed);
                ps.setDouble(35, gallopSpeed);

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to insert horse into database: " + uuid);
                e.printStackTrace();
                return false;
            }
        });
    }

    public void removeHorse(UUID uuid) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM EQUINE_HORSES WHERE uuid = ?")) {
                preparedStatement.setString(1, uuid.toString());
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

    public EquineLiveHorse getHorse(UUID uuid) {
        EquineLiveHorse equineLiveHorse = null;
        String sql = "SELECT * FROM EQUINE_HORSES WHERE uuid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, uuid.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    equineLiveHorse = DatabaseUtils.mapResultSetToHorse(resultSet);
                    return equineLiveHorse;
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get horse: " + e.getMessage());
        }
        return equineLiveHorse;
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
            boolean isPregnant = nbt.getBoolean(Keys.IS_PREGNANT.getKey());
            boolean isInHeat = nbt.getBoolean(Keys.IS_IN_HEAT.getKey());
            double hungerPercentage = nbt.getDouble(Keys.HUNGER_PERCENTAGE.getKey());
            long lastHungerUpdate = nbt.getLong(Keys.LAST_HUNGER_UPDATE.getKey());
            double thirstPercentage = nbt.getDouble(Keys.THIRST_PERCENTAGE.getKey());
            long lastThirstUpdate = nbt.getLong(Keys.LAST_THIRST_UPDATE.getKey());
            double walkSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());
            double trotSpeed = nbt.getDouble(Keys.TROT_SPEED.getKey());
            double canterSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());
            double gallopSpeed = nbt.getDouble(Keys.WALK_SPEED.getKey());


            boolean isMySQL;
            try {
                isMySQL = connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql");
            } catch (SQLException e) {
                throw new RuntimeException("Failed to detect database type", e);
            }

            String sql = (isMySQL ? "UPDATE " : "UPDATE OR REPLACE ") + "EQUINE_HORSES SET " +
                    "owner_uuid = ?, display_name = ?, discipline = ?, breed_1 = ?, breed_2 = ?, prominent_breed = ?, " +
                    "coat_color = ?, coat_modifier = ?, gender = ?, age = ?, height = ?, trait_1 = ?, trait_2 = ?, " +
                    "trait_3 = ?, claim_time = ?, birth_time = ?, owner_name = ?, base_speed = ?, base_jump_power = ?, " +
                    "skull_id = ?, last_world = ?, last_location_x = ?, last_location_y = ?, last_location_z = ?, " +
                    "is_pregnant = ?, is_in_heat = ?, hunger_percentage = ?, last_hunger_update = ?, thirst_percentage = ?, " +
                    "last_thirst_update = ?, walk_speed = ?, trot_speed = ?, canter_speed = ?, gallop_speed = ? " +
                    "WHERE uuid = ?";

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
                ps.setBoolean(25, isPregnant);
                ps.setBoolean(26, isInHeat);
                ps.setDouble(27, hungerPercentage);
                ps.setLong(28, lastHungerUpdate);
                ps.setDouble(29, thirstPercentage);
                ps.setLong(30, lastThirstUpdate);
                ps.setDouble(31, walkSpeed);
                ps.setDouble(32, trotSpeed);
                ps.setDouble(33, canterSpeed);
                ps.setDouble(34, gallopSpeed);
                ps.setString(35, uuid);

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to update horse in database: " + uuid);
                e.printStackTrace();
                return false;
            }
        });
    }


}
