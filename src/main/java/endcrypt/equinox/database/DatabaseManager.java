package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.dao.DatabaseHorses;
import endcrypt.equinox.database.dao.DatabasePlayer;
import endcrypt.equinox.database.dao.DatabaseTrustedPlayers;
import endcrypt.equinox.database.dao.DatabaseWaste;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.sql.*;

@Getter
public class DatabaseManager implements Listener {
    private Connection connection = null;
    private final EquinoxEquestrian plugin;
    private DatabasePlayer databasePlayer;
    private DatabaseTrustedPlayers databaseTrustedPlayers;
    private DatabaseHorses databaseHorses;
    private DatabaseWaste databaseWaste;

    public DatabaseManager(EquinoxEquestrian plugin) {
        this.plugin = plugin;

        try {
            ConfigurationSection dbConfig = plugin.getConfigManager().getConfigMain().getDatabaseSection();
            if (dbConfig == null) {
                throw new SQLException("Database configuration section not found in config.yml");
            }

            String dbType = dbConfig.getString("type", "sqlite");

            if ("mysql".equalsIgnoreCase(dbType)) {
                String host = dbConfig.getString("host", "localhost");
                int port = dbConfig.getInt("port", 3306);
                String database = dbConfig.getString("database");
                String username = dbConfig.getString("username");
                String password = dbConfig.getString("password");

                String url = String.format("jdbc:mysql://%s:%d/%s", host, port, database);
                this.connection = DriverManager.getConnection(url, username, password);
            } else {
                // Default to SQLite
                String path = plugin.getDataFolder().getAbsolutePath() + "/database.db";
                this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            }

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            databasePlayer = new DatabasePlayer(plugin);
            databaseTrustedPlayers = new DatabaseTrustedPlayers(plugin);
            databaseHorses = new DatabaseHorses(plugin);
            databaseWaste = new DatabaseWaste(plugin);
            plugin.getLogger().info("Connected to database!");
            plugin.getPlayerDataManager().loadAllOnlinePlayers();
            plugin.setDatabaseLoaded(true);
        }, 1L);
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}