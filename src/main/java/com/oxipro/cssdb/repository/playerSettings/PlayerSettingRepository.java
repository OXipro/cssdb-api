package com.oxipro.cssdb.repository.playerSettings;

import com.oxipro.cssdb.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSettingRepository {

    private final Database db;

    public PlayerSettingRepository(Database db) {
        this.db = db;
    }

    public void initTable() {

        String sql = "CREATE TABLE IF NOT EXISTS player_settings (" +
                "uuid VARCHAR(36) NOT NULL," +
                "setting_key VARCHAR(64) NOT NULL," +
                "setting_value TEXT," +
                "PRIMARY KEY (uuid, setting_key)" +
                ")";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> load(UUID uuid) {
        Map<String, String> settings = new HashMap<>();

        String sql = "SELECT setting_key, setting_value FROM player_settings WHERE uuid=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                settings.put(rs.getString("setting_key"), rs.getString("setting_value"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return settings;
    }

    public String get(UUID uuid, String key) {

        String sql = "SELECT setting_value FROM player_settings WHERE uuid=? AND setting_key=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.setString(2, key);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("setting_value");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void set(UUID uuid, String key, String value) {

        String sql = "INSERT INTO player_settings (uuid, setting_key, setting_value) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE setting_value=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.setString(2, key);
            ps.setString(3, value);
            ps.setString(4, value);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(UUID uuid, Map<String, String> settings) {

        String sql = "INSERT INTO player_settings (uuid, setting_key, setting_value) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE setting_value=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            for (Map.Entry<String, String> entry : settings.entrySet()) {

                ps.setString(1, uuid.toString());
                ps.setString(2, entry.getKey());
                ps.setString(3, entry.getValue());
                ps.setString(4, entry.getValue());

                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID uuid) {

        String sql = "DELETE FROM player_settings WHERE uuid=?";

        try (Connection c = db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}