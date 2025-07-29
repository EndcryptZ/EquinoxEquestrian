package endcrypt.equinox.database.dao;

import endcrypt.equinox.EquinoxEquestrian;
import endcrypt.equinox.database.DatabaseUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DatabaseWaste {

    private final Connection connection;
    private final EquinoxEquestrian plugin;

    private final Map<String, String> BASE_SCHEMA = new LinkedHashMap<>() {{
        put("type", "TEXT");                    // 'Poo' or 'Pee'
        put("x", "INTEGER");                    // X coordinate
        put("y", "INTEGER");                    // Y coordinate
        put("z", "INTEGER");                    // Z coordinate
        put("world", "TEXT");                  // World name
        put("PRIMARY KEY", "(x, y, z, world)"); // Composite key
    }};

    public DatabaseWaste(EquinoxEquestrian plugin) {
        this.plugin = plugin;
        this.connection = plugin.getDatabaseManager().getConnection();

        try {
            boolean isMySQL = connection.getMetaData()
                    .getDatabaseProductName()
                    .toLowerCase()
                    .contains("mysql");

            Map<String, String> schema = DatabaseUtils.getSchemaForDatabase(BASE_SCHEMA, isMySQL);

            StringBuilder createTable = new StringBuilder(
                    "CREATE TABLE IF NOT EXISTS EQUINE_WASTE_BLOCKS (");

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

            DatabaseUtils.updateTableSchema("EQUINE_WASTE_BLOCKS", schema);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to create waste blocks database table: " + e.getMessage());
        }
    }

    public void addWasteBlock(String type, Location location) {
        String sql;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String dbType = metaData.getDatabaseProductName().toLowerCase();

            if (dbType.contains("mysql")) {
                sql = "INSERT IGNORE INTO EQUINE_WASTE_BLOCKS (type, x, y, z, world) VALUES (?, ?, ?, ?, ?)";
            } else {
                sql = "INSERT OR IGNORE INTO EQUINE_WASTE_BLOCKS (type, x, y, z, world) VALUES (?, ?, ?, ?, ?)";
            }

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, type);
                ps.setInt(2, location.getBlockX());
                ps.setInt(3, location.getBlockY());
                ps.setInt(4, location.getBlockZ());
                ps.setString(5, location.getWorld().getName());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to add waste block: " + e.getMessage());
        }
    }

    public void removeWasteBlock(Location location) {
        String sql = "DELETE FROM EQUINE_WASTE_BLOCKS WHERE x = ? AND y = ? AND z = ? AND world = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, location.getBlockX());
            ps.setInt(2, location.getBlockY());
            ps.setInt(3, location.getBlockZ());
            ps.setString(4, location.getWorld().getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to remove waste block: " + e.getMessage());
        }
    }

    public boolean hasWasteBlock(Location location) {
        String sql = "SELECT 1 FROM EQUINE_WASTE_BLOCKS WHERE x = ? AND y = ? AND z = ? AND world = ? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, location.getBlockX());
            ps.setInt(2, location.getBlockY());
            ps.setInt(3, location.getBlockZ());
            ps.setString(4, location.getWorld().getName());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if at least one result exists
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to check waste block: " + e.getMessage());
            return false;
        }
    }

    public List<Location> getPeeAndPooLocationsInChunk(Chunk chunk) {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT x, y, z FROM EQUINE_WASTE_BLOCKS WHERE world = ? AND x BETWEEN ? AND ? AND z BETWEEN ? AND ?";

        World world = chunk.getWorld();
        int minX = chunk.getX() << 4;
        int minZ = chunk.getZ() << 4;
        int maxX = minX + 15;
        int maxZ = minZ + 15;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, world.getName());
            ps.setInt(2, minX);
            ps.setInt(3, maxX);
            ps.setInt(4, minZ);
            ps.setInt(5, maxZ);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int x = rs.getInt("x");
                    int y = rs.getInt("y");
                    int z = rs.getInt("z");
                    locations.add(new Location(world, x, y, z));
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to load pee/poo locations for chunk: " + e.getMessage());
        }

        return locations;
    }
}
