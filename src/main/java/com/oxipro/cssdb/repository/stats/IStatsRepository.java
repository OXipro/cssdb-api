package com.oxipro.cssdb.repository.stats;

import java.util.UUID;

public interface IStatsRepository {

    void initTable();

    int getKills(UUID uuid);

    void setKills(UUID uuid, int kills);
}