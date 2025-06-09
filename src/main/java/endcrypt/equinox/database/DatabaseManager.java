package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.attributes.*;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import de.tr7zw.changeme.nbtapi.NBT;

import java.sql.*;
import java.util.*;

public class DatabaseManager implements Listener {
    private final Connection connection;
    private final EquinoxEquestrian plugin;

    private final Map<String, String> EQUINE_HORSES_SCHEMA = new LinkedHashMap<>() {{
        put("uuid", "TEXT PRIMARY KEY");
        put("owner_uuid", "VARCHAR(255) NOT NULL");
        put("display_name", "TEXT");
        put("discipline", "TEXT");
        put("breed_1", "TEXT");
        put("breed_2", "TEXT");
        put("prominent_breed", "TEXT");
        put("coat_color", "TEXT");
        put("coat_modifier", "TEXT");
        put("gender", "TEXT");
        put("age", "INTEGER");
        put("height", "DOUBLE");
        put("trait_1", "TEXT");
        put("trait_2", "TEXT");
        put("trait_3", "TEXT");
        put("claim_time", "LONG");
        put("birth_time", "LONG");
        put("owner_name", "TEXT");
        put("base_speed", "DOUBLE");
        put("base_jump_power", "DOUBLE");
        put("skull_id", "TEXT");
        put("last_world", "TEXT");
        put("last_location_x", "DOUBLE");
        put("last_location_y", "DOUBLE");
        put("last_location_z", "DOUBLE");
    }};

    private final Map<String, String> EQUINE_TRUSTED_PLAYERS_SCHEMA = new LinkedHashMap<>() {{
        put("horse_uuid", "TEXT NOT NULL");
        put("player_uuid", "TEXT NOT NULL");
        put("PRIMARY KEY", "(horse_uuid, player_uuid)");
    }};

    public DatabaseManager(EquinoxEquestrian plugin, String path) throws SQLException {
        this.plugin = plugin;
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS PLAYERS (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "token int NOT NULL DEFAULT 0)");

            statement.execute("CREATE TABLE IF NOT EXISTS EQUINE_HORSES (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "owner_uuid VARCHAR(255) NOT NULL" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS EQUINE_TRUSTED_PLAYERS (" +
                    "horse_uuid TEXT NOT NULL, " +
                    "player_uuid TEXT NOT NULL, " +
                    "PRIMARY KEY (horse_uuid, player_uuid)" +
                    ")");

            updateTableSchema("EQUINE_HORSES", EQUINE_HORSES_SCHEMA);
            updateTableSchema("EQUINE_TRUSTED_PLAYERS", EQUINE_TRUSTED_PLAYERS_SCHEMA);
        }
    }

    private void updateTableSchema(String tableName, Map<String, String> desiredSchema) throws SQLException {
        Set<String> existingColumns = new HashSet<>();

        // Get existing column names from the table
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")")) {
            while (rs.next()) {
                existingColumns.add(rs.getString("name").toLowerCase());
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
                    stmt.execute(sql);
                    System.out.println("Added missing column '" + columnName + "' to table '" + tableName + "'");
                }
            }
        }
    }

    public void addPlayer(Player player) throws SQLException {
        if (!playerExists(player)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid) VALUES (?)")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                preparedStatement.executeUpdate();
            }
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

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO EQUINE_HORSES (" +
                            "uuid, owner_uuid, display_name, discipline, breed_1, breed_2, prominent_breed, coat_color, coat_modifier, " +
                            "gender, age, height, trait_1, trait_2, trait_3, claim_time, birth_time, owner_name, base_speed, base_jump_power, skull_id, last_world, last_location_x, last_location_y, last_location_z ) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, uuid);
                ps.setString(2, ownerUuid);
                ps.setString(3, displayName);
                ps.setString(4, discipline);
                ps.setString(5, breed1);
                ps.setString(6, breed2);
                ps.setString(7, prominentBreed);
                ps.setString(8, coatColor);        // added
                ps.setString(9, coatModifier);     // added
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
                e.printStackTrace();
                return false;
            }
        });
    }

    public void removeHorse(AbstractHorse horse) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM EQUINE_HORSES WHERE uuid = ?")) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }


    public List<EquineLiveHorse> getPlayerHorses(Player player) {
        List<EquineLiveHorse> horses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM EQUINE_HORSES WHERE owner_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
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
                    horse.setCoatColor(CoatColor.getByName(resultSet.getString("coat_color")));       // added
                    horse.setCoatModifier(CoatModifier.getByName(resultSet.getString("coat_modifier"))); // added
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

                    if (resultSet.getString("last_world") != null) {
                        World world = Bukkit.getWorld(resultSet.getString("last_world"));
                        double x = resultSet.getDouble("last_location_x");
                        double y = resultSet.getDouble("last_location_y");
                        double z = resultSet.getDouble("last_location_z");
                        horse.setLastLocation(new Location(world, x, y, z));
                    }
                    horses.add(horse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horses;
    }

    public void setTokenAmount(Player player, int tokenAmount) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET token = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, tokenAmount);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public int getTokenAmount(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("token");
            }
        }
        return 0;
    }

    public boolean playerExists(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            return preparedStatement.executeQuery().next();
        }
    }

    public boolean horseExists(AbstractHorse horse) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM EQUINE_HORSES WHERE uuid = ?")) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
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

            try (PreparedStatement ps = connection.prepareStatement(
                    "UPDATE EQUINE_HORSES SET " +
                            "owner_uuid = ?, display_name = ?, discipline = ?, breed_1 = ?, breed_2 = ?, prominent_breed = ?, " +
                            "coat_color = ?, coat_modifier = ?, gender = ?, age = ?, height = ?, trait_1 = ?, trait_2 = ?, " +
                            "trait_3 = ?, claim_time = ?, birth_time = ?, owner_name = ?, base_speed = ?, base_jump_power = ?, " +
                            "skull_id = ?, last_world = ?, last_location_x = ?, last_location_y = ?, last_location_z = ? " +
                            "WHERE uuid = ?")) {

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
                ps.setString(25, uuid); // WHERE uuid = ?

                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                System.err.println("Failed to update horse in database: " + uuid);
                e.printStackTrace();
                return false;
            }
        });
    }

    public void addTrustedPlayer(AbstractHorse horse, Player player) {
        String sql = "INSERT OR IGNORE INTO EQUINE_TRUSTED_PLAYERS (horse_uuid, player_uuid) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, horse.getUniqueId().toString());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to add trusted player: " + e.getMessage());
        }
    }

    public List<UUID> getTrustedPlayers(AbstractHorse horse) {
        List<UUID> trusted = new ArrayList<>();
        String sql = "SELECT player_uuid FROM EQUINE_TRUSTED_PLAYERS WHERE horse_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, horse.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                trusted.add(UUID.fromString(rs.getString("player_uuid")));
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get trusted players: " + e.getMessage());
        }
        return trusted;
    }

    public boolean isTrustedToHorse(AbstractHorse horse, Player player) {
        String sql = "SELECT * FROM EQUINE_TRUSTED_PLAYERS WHERE horse_uuid = ? AND player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, horse.getUniqueId().toString());
            ps.setString(2, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check trusted player: " + e.getMessage());
            return false;
        }
    }

    public void removeTrustedPlayer(AbstractHorse horse, Player player) {
        String sql = "DELETE FROM EQUINE_TRUSTED_PLAYERS WHERE horse_uuid = ? AND player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, horse.getUniqueId().toString());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove trusted player: " + e.getMessage());
        }
    }

    public List<EquineLiveHorse> getTrustedHorses(Player player) {
        List<EquineLiveHorse> horses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT h.* FROM EQUINE_HORSES h " +
                        "INNER JOIN EQUINE_TRUSTED_PLAYERS t ON h.uuid = t.horse_uuid " +
                        "WHERE t.player_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
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

                    if (resultSet.getString("last_world") != null) {
                        World world = Bukkit.getWorld(resultSet.getString("last_world"));
                        double x = resultSet.getDouble("last_location_x");
                        double y = resultSet.getDouble("last_location_y");
                        double z = resultSet.getDouble("last_location_z");
                        horse.setLastLocation(new Location(world, x, y, z));
                    }
                    horses.add(horse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horses;
    }


}