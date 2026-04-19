package com.oxipro.cssdb.repository.playerSettings;

public enum PlayerSetting {
    LANGUAGE_ISO("lang_iso");

    public final String dbKey;

    private PlayerSetting(String dbKey) {
        this.dbKey = dbKey;
    }
}
