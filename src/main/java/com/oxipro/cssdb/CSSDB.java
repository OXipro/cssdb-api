package com.oxipro.cssdb;

import com.oxipro.cssdb.support.IDBSupport;
import com.oxipro.cssdb.repository.stats.StatsRepository;
import com.oxipro.cssdb.repository.playerSettings.PlayerSettingRepository;

public class CSSDB {

    private final IDBSupport dbSupport;

    private StatsRepository statsRepository;
    private PlayerSettingRepository playerSettingsRepository;

    public CSSDB(IDBSupport dbSupport) {
        this.dbSupport = dbSupport;
    }

    public void init() {
        dbSupport.connect();

        statsRepository = new StatsRepository(dbSupport);
        playerSettingsRepository = new PlayerSettingRepository(dbSupport);

        statsRepository.initTable();
        playerSettingsRepository.initTable();
    }

    public void shutdown() {
        dbSupport.shutdown();
    }

    public StatsRepository getStatsRepository() {
        return statsRepository;
    }

    public PlayerSettingRepository getPlayerSettingsRepository() {
        return playerSettingsRepository;
    }
}