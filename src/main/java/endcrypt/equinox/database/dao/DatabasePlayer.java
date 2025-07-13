package endcrypt.equinox.database.dao;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.DatabaseUtils;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabasePlayer {

    private final Connection connection;
    private final EquinoxEquestrian plugin;

    private final Map<String, String> BASE_SCHEMA = new LinkedHashMap<>() {{
        put("uuid", "UUID");  // Will be converted depending on MySQL/SQLite
        put("token", "INTEGER");  // Using INTEGER for cross-compatibility
        put("level", "INTEGER");  // Player level
        put("exp", "REAL");       // Use REAL in case you want fractional exp (e.g., 10.5)
    }};

    public DatabasePlayer(EquinoxEquestrian plugin) {
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
                statement.execute("CREATE TABLE IF NOT EXISTS PLAYERS (" +
                        "uuid " + uuidType + " PRIMARY KEY, " +
                        "token INTEGER NOT NULL DEFAULT 0" +
                        ")");
            }

            // Let utility handle the rest (future schema additions)
            DatabaseUtils.updateTableSchema("PLAYERS", schema);

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create player database tables: " + e.getMessage());
        }
    }

    public void addPlayer(Player player) {
        try {
            if (!playerExists(player)) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PLAYERS (uuid) VALUES (?)")) {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM PLAYERS WHERE uuid = ?")) {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE PLAYERS SET token = ? WHERE uuid = ?")) {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT token FROM PLAYERS WHERE uuid = ?")) {
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

    public void setExp(Player player, double exp) {
        String query = "UPDATE PLAYERS SET exp = ? WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, exp);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to update exp for player " + player.getName() + ": " + e.getMessage());
        }
    }

    public void setLevel(Player player, int level) {
        String query = "UPDATE PLAYERS SET level = ? WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, level);
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to update level for player " + player.getName() + ": " + e.getMessage());
        }
    }

    public double getExp(Player player) {
        String query = "SELECT exp FROM PLAYERS WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("exp");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to fetch exp for player " + player.getName() + ": " + e.getMessage());
        }

        return 0.0; // Default value if not found
    }

    public int getLevel(Player player) {
        String query = "SELECT level FROM PLAYERS WHERE uuid = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("level");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to fetch level for player " + player.getName() + ": " + e.getMessage());
        }

        return 1; // Default value if not found
    }
}
