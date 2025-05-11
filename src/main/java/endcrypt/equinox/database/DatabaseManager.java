package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

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
            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "token int NOT NULL DEFAULT 0)");
            statement.execute("CREATE TABLE IF NOT EXISTS livehorses (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "owner_uuid varchar(255) NOT NULL)");
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

    public void addHorse(AbstractHorse horse) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO livehorses (uuid, owner_uuid) VALUES (?, ?)")) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            preparedStatement.setString(2, horse.getOwner().getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public void removeHorse(AbstractHorse horse) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM livehorses WHERE uuid = ?")) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public List<UUID> getPlayerHorses(Player player) throws SQLException {
        List<UUID> horseUUIDs = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM livehorses WHERE owner_uuid = ?")) {
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

    public boolean horseExists(AbstractHorse horse) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM livehorses WHERE uuid = ?")) {
            preparedStatement.setString(1, horse.getUniqueId().toString());
            return preparedStatement.executeQuery().next();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}