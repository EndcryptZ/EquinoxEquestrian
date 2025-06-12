package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.sql.*;

@Getter
public class DatabaseManager implements Listener {
    private Connection connection = null;
    private final EquinoxEquestrian plugin;
    private DatabasePlayer databasePlayer;
    private DatabaseTrustedPlayers databaseTrustedPlayers;
    private DatabaseHorses databaseHorses;

    public DatabaseManager(EquinoxEquestrian plugin, String path) {
        this.plugin = plugin;
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            databasePlayer = new DatabasePlayer(plugin);
            databaseTrustedPlayers = new DatabaseTrustedPlayers(plugin);
            databaseHorses = new DatabaseHorses(plugin);
            plugin.getLogger().info("Connected to database!");
        }, 1L);

    }


    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}