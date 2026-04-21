package com.oxipro.cssdb.cache;

import com.oxipro.cssdb.repository.stats.StatsRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsCache {

    private final StatsRepository repository;
    private final Map<UUID, Map<String, Integer>> cache = new HashMap<>();

    public StatsCache(StatsRepository repository) {
        this.repository = repository;
    }

    public void load(UUID uuid) {
        cache.put(uuid, new HashMap<>(repository.load(uuid)));
    }

    private Map<String, Integer> getMap(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> new HashMap<>(repository.load(u)));
    }

    public int get(UUID uuid, String key) {
        return getMap(uuid).getOrDefault(key, 0);
    }

    public void set(UUID uuid, String key, int value) {
        getMap(uuid).put(key, value);
    }

    public void increment(UUID uuid, String key, int amount) {
        Map<String, Integer> map = getMap(uuid);
        map.put(key, map.getOrDefault(key, 0) + amount);
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

    public boolean isLoaded(UUID uuid) {
        return cache.containsKey(uuid);
    }
}