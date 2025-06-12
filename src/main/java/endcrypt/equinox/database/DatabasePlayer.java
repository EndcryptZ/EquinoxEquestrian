package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import org.bukkit.entity.Player;

import java.sql.*;

public class DatabasePlayer {

    private final Connection connection;
    private final EquinoxEquestrian plugin;
    public DatabasePlayer(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.connection = plugin.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS PLAYERS (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "token int NOT NULL DEFAULT 0)");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create player database tables: " + e.getMessage());
        }
    }

    public void addPlayer(Player player) {
        try {
            if (!playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid) VALUES (?)")) {
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to add player to database: " + e.getMessage());
        }
    }

    private boolean playerExists(Player player) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                return preparedStatement.executeQuery().next();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check player existence: " + e.getMessage());
        }
        return false;
    }

    public void setTokenAmount(Player player, int tokenAmount) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET token = ? WHERE uuid = ?")) {
                preparedStatement.setInt(1, tokenAmount);
                preparedStatement.setString(2, player.getUniqueId().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to set token amount: " + e.getMessage());
        }
    }

    public int getTokenAmount(Player player) {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM players WHERE uuid = ?")) {
                preparedStatement.setString(1, player.getUniqueId().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("token");
                }
            }
            return 0;
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to get token amount: " + e.getMessage());
        }
        return 0;
    }
}
