package com.oxipro.cssdb.repository.playerSettings;

import com.oxipro.cssdb.support.IDBSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PlayerSettingRepository {

    private final IDBSupport db;

    public PlayerSettingRepository(IDBSupport db) {
        this.db = db;
    }

    public void initTable() {

        String sql = "CREATE TABLE IF NOT EXISTS player_settings (" +
                "uuid VARCHAR(36) PRIMARY KEY" +
                ")";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureRow(UUID uuid) {

        String sql = "INSERT IGNORE INTO player_settings (uuid) VALUES (?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ensureColumn(String column) {

        String sql = "ALTER TABLE player_settings ADD COLUMN IF NOT EXISTS `" + column + "` TEXT";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getSetting(UUID uuid, PlayerSetting setting) {

        String sql = "SELECT `" + setting.dbKey + "` FROM player_settings WHERE uuid=?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setSetting(UUID uuid, PlayerSetting setting, String value) {

        ensureRow(uuid);
        ensureColumn(setting.dbKey);

        String sql = "UPDATE player_settings SET `" + setting.dbKey + "`=? WHERE uuid=?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);
            ps.setString(2, uuid.toString());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(UUID uuid) {

        String sql = "DELETE FROM player_settings WHERE uuid=?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}