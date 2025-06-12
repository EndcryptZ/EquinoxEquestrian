package endcrypt.equinox.database;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.equine.EquineLiveHorse;
import endcrypt.equinox.equine.nbt.Keys;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import de.tr7zw.changeme.nbtapi.NBT;

import java.sql.*;
import java.util.*;

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
            databasePlayer = new DatabasePlayer(plugin);
            databaseTrustedPlayers = new DatabaseTrustedPlayers(plugin);
            databaseHorses = new DatabaseHorses(plugin);

            plugin.getLogger().info("Connected to database!");

        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
        }
    }


    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}