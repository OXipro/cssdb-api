package com.oxipro.cssdb;

import com.oxipro.cssdb.cache.PlayerSettingCache;
import com.oxipro.cssdb.cache.StatsCache;
import com.oxipro.cssdb.database.Database;
import com.oxipro.cssdb.repository.stats.StatsRepository;
import com.oxipro.cssdb.repository.playerSettings.PlayerSettingRepository;

public class CSSDB {

    private final Database database;

    private StatsRepository statsRepository;
    private PlayerSettingRepository playerSettingsRepository;

    private StatsCache statsCache;
    private PlayerSettingCache playerSettingCache;

    public CSSDB(Database database) {
        this.database = database;
    }

    public void init() {
        database.connect();

        this.statsRepository = new StatsRepository(database);
        this.playerSettingsRepository = new PlayerSettingRepository(database);

        this.statsCache = new StatsCache(statsRepository);
        this.playerSettingCache = new PlayerSettingCache(playerSettingsRepository);
    }

    public void disconnect() {
        database.disconnect();
    }

    public StatsCache getStatsCache() { return statsCache; }

    public PlayerSettingCache getPlayerSettingCache() { return playerSettingCache; }

    public StatsRepository getStatsRepository() {
        return statsRepository;
    }

    public PlayerSettingRepository getPlayerSettingsRepository() {
        return playerSettingsRepository;
    }
}