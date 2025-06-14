package endcrypt.equinox.database.dao;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.DatabaseUtils;
import endcrypt.equinox.equine.EquineLiveHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class DatabaseTrustedPlayers {

    private final Connection connection;
    private final EquinoxEquestrian plugin;

    private final Map<String, String> BASE_SCHEMA = new LinkedHashMap<>() {{
        put("horse_uuid", "UUID");
        put("player_uuid", "UUID");
        put("PRIMARY KEY", "(horse_uuid, player_uuid)");
    }};

    public DatabaseTrustedPlayers(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.connection = plugin.getDatabaseManager().getConnection();

        try {
            boolean isMySQL = connection.getMetaData()
                    .getDatabaseProductName()
                    .toLowerCase()
                    .contains("mysql");

            Map<String, String> schema = DatabaseUtils.getSchemaForDatabase(BASE_SCHEMA, isMySQL);

            // Create table with appropriate schema
            StringBuilder createTable = new StringBuilder(
                    "CREATE TABLE IF NOT EXISTS EQUINE_TRUSTED_PLAYERS (");

            for (Map.Entry<String, String> entry : schema.entrySet()) {
                if (entry.getKey().equals("PRIMARY KEY")) {
                    createTable.append(entry.getKey()).append(" ").append(entry.getValue());
                } else {
                    createTable.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
                }
            }
            createTable.append(")");

            try (Statement statement = connection.createStatement()) {
                statement.execute(createTable.toString());
            }

            DatabaseUtils.updateTableSchema("EQUINE_TRUSTED_PLAYERS", schema);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create trusted players database tables: " + e.getMessage());
        }
    }



    public void addTrustedPlayer(AbstractHorse horse, Player player) {
        // Get database type from connection metadata
        String sql;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String dbType = metaData.getDatabaseProductName().toLowerCase();

            // Use appropriate syntax based on database type
            if (dbType.contains("mysql")) {
                sql = "INSERT IGNORE INTO EQUINE_TRUSTED_PLAYERS (horse_uuid, player_uuid) VALUES (?, ?)";
            } else {
                sql = "INSERT OR IGNORE INTO EQUINE_TRUSTED_PLAYERS (horse_uuid, player_uuid) VALUES (?, ?)";
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, horse.getUniqueId().toString());
                ps.setString(2, player.getUniqueId().toString());
                ps.executeUpdate();
            }
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

    public boolean isTrustedToHorse(AbstractHorse horse, OfflinePlayer player) {
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

    public void removeTrustedPlayer(AbstractHorse horse, OfflinePlayer player) {
        String sql = "DELETE FROM EQUINE_TRUSTED_PLAYERS WHERE horse_uuid = ? AND player_uuid = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, horse.getUniqueId().toString());
            ps.setString(2, player.getUniqueId().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove trusted player: " + e.getMessage());
        }
    }

    public List<EquineLiveHorse> getTrustedHorses(OfflinePlayer player) {
        List<EquineLiveHorse> horses = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT h.* FROM EQUINE_HORSES h " +
                        "INNER JOIN EQUINE_TRUSTED_PLAYERS t ON h.uuid = t.horse_uuid " +
                        "WHERE t.player_uuid = ?")) {
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
            plugin.getLogger().severe("Failed to get trusted horses: " + e.getMessage());
        }
        return horses;
    }
}
