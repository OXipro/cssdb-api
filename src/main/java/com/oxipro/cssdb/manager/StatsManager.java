package com.oxipro.cssdb.manager;

import com.oxipro.cssdb.repository.stats.StatsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsManager {

    private final StatsRepository repository;
    private final Map<UUID, Map<String, Integer>> cache = new HashMap<>();

    public StatsManager(StatsRepository repository) {
        this.repository = repository;
    }

    public void load(UUID uuid) {
        cache.put(uuid, repository.load(uuid));
    }

    private Map<String, Integer> getMap(UUID uuid) {
        return cache.computeIfAbsent(uuid, repository::load);
    }

    public int get(UUID uuid, String key) {
        return getMap(uuid).getOrDefault(key, 0);
    }

    public void set(UUID uuid, String key, int value) {
        getMap(uuid).put(key, value);
    }

    public void add(UUID uuid, String key, int amount) {
        set(uuid, key, get(uuid, key) + amount);
    }

    public void save(UUID uuid) {
        Map<String, Integer> stats = cache.get(uuid);
        if (stats != null) {
            repository.save(uuid, stats);
        }
    }

    public void unload(UUID uuid) {
        save(uuid);
        cache.remove(uuid);
    }
}