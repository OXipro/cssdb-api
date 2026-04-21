package com.oxipro.cssdb.cache;

import com.oxipro.cssdb.repository.playerSettings.PlayerSettingRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSettingCache {

    private final PlayerSettingRepository repository;
    private final Map<UUID, Map<String, String>> cache = new HashMap<>();

    public PlayerSettingCache(PlayerSettingRepository repository) {
        this.repository = repository;
    }

    public void load(UUID uuid) {
        cache.put(uuid, new HashMap<>(repository.load(uuid)));
    }

    private Map<String, String> getMap(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> new HashMap<>(repository.load(u)));
    }

    public String get(UUID uuid, String key) {
        return getMap(uuid).get(key);
    }

    public String getOrDefault(UUID uuid, String key, String def) {
        return getMap(uuid).getOrDefault(key, def);
    }

    public void set(UUID uuid, String key, String value) {
        getMap(uuid).put(key, value);
    }

    public void save(UUID uuid) {
        Map<String, String> settings = cache.get(uuid);
        if (settings != null) {
            repository.save(uuid, settings);
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