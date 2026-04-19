package com.oxipro.cssdb.repository.stats;

import com.oxipro.cssdb.support.IDBSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsRepository {

    private final IDBSupport db;

    public StatsRepository(IDBSupport db) {
        this.db = db;
    }

    public void initTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid VARCHAR(36) NOT NULL," +
                "stat_key VARCHAR(64) NOT NULL," +
                "stat_value INT NOT NULL," +
                "PRIMARY KEY (uuid, stat_key)" +
                ")";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> load(UUID uuid) {
        Map<String, Integer> stats = new HashMap<>();

        String sql = "SELECT stat_key, stat_value FROM player_stats WHERE uuid=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stats.put(rs.getString("stat_key"), rs.getInt("stat_value"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;
    }

    public void save(UUID uuid, Map<String, Integer> stats) {
        String sql = "INSERT INTO player_stats (uuid, stat_key, stat_value) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE stat_value=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (Map.Entry<String, Integer> entry : stats.entrySet()) {

                ps.setString(1, uuid.toString());
                ps.setString(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.setInt(4, entry.getValue());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}