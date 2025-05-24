package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.nbt.Keys;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import de.tr7zw.changeme.nbtapi.NBT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager implements Listener { 
    private final Connection connection;
    private final EquinoxEquestrian plugin;

    public DatabaseManager(EquinoxEquestrian plugin, String path) throws SQLException {
        this.plugin = plugin;
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS PLAYERS (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "token int NOT NULL DEFAULT 0)");

            statement.execute("CREATE TABLE IF NOT EXISTS EQUINE_HORSES (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "owner_uuid VARCHAR(255) NOT NULL, " +
                    "discipline TEXT, " +
                    "breed_1 TEXT, " +
                    "breed_2 TEXT, " +
                    "prominent_breed TEXT, " +
                    "gender TEXT, " +
                    "age INTEGER, " +
                    "height DOUBLE, " +
                    "trait_1 TEXT, " +
                    "trait_2 TEXT, " +
                    "trait_3 TEXT, " +
                    "claim_time LONG, " +
                    "birth_time LONG, " +
                    "owner_name TEXT, " +
                    "base_speed DOUBLE, " +
                    "base_jump_power DOUBLE, " +
                    "skull_id TEXT, " +
                    ")");
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
            String discipline = nbt.getString(Keys.DISCIPLINE.getKey());
            String breed1 = nbt.getString(Keys.BREED_PREFIX.getKey() + "0");
            String breed2 = nbt.getString(Keys.BREED_PREFIX.getKey() + "1");
            String prominentBreed = nbt.getString(Keys.PROMINENT_BREED.getKey());
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

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO EQUINE_HORSES (uuid, owner_uuid, discipline, breed_1, breed_2, prominent_breed, gender, " +
                            "age, height, trait_1, trait_2, trait_3, claim_time, birth_time, owner_name, base_speed, " +
                            "base_jump_power, skull_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, uuid);
                ps.setString(2, ownerUuid);
                ps.setString(3, discipline);
                ps.setString(4, breed1);
                ps.setString(5, breed2);
                ps.setString(6, prominentBreed);
                ps.setString(7, gender);
                ps.setInt(8, age);
                ps.setDouble(9, height);
                ps.setString(10, trait1);
                ps.setString(11, trait2);
                ps.setString(12, trait3);
                ps.setLong(13, claimTime);
                ps.setLong(14, birthTime);
                ps.setString(15, ownerName);
                ps.setDouble(16, baseSpeed);
                ps.setDouble(17, baseJump);
                ps.setString(18, skullId);

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

    public List<UUID> getPlayerHorses(Player player) throws SQLException {
        List<UUID> horseUUIDs = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM EQUINE_HORSES WHERE owner_uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                horseUUIDs.add(UUID.fromString(resultSet.getString("uuid")));
            }
        }
        return horseUUIDs;
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
    
    
}