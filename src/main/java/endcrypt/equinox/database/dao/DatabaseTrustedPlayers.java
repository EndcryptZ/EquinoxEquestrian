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

    private final Map<String, String> EQUINE_TRUSTED_PLAYERS_SCHEMA = new LinkedHashMap<>() {{
        put("horse_uuid", "TEXT NOT NULL");
        put("player_uuid", "TEXT NOT NULL");
        put("PRIMARY KEY", "(horse_uuid, player_uuid)");
    }};

    public DatabaseTrustedPlayers(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.connection = plugin.getDatabaseManager().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS EQUINE_TRUSTED_PLAYERS (" +
                    "horse_uuid TEXT NOT NULL, " +
                    "player_uuid TEXT NOT NULL, " +
                    "PRIMARY KEY (horse_uuid, player_uuid)" +
                    ")");

            DatabaseUtils.updateTableSchema("EQUINE_TRUSTED_PLAYERS", EQUINE_TRUSTED_PLAYERS_SCHEMA);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create player database tables: " + e.getMessage());
        }
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
