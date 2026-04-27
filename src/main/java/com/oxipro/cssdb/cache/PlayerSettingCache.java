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

    /**
     * Load a player settings into cache
     */
    public void load(UUID uuid) {
        cache.put(uuid, new HashMap<>(repository.load(uuid)));
    }

    /**
     * Save a player's settings
     */
    public void save(UUID uuid) {
        Map<String, String> settings = cache.get(uuid);
        if (settings != null) {
            repository.save(uuid, settings);
        }
    }

    /**
     * Remove player from cache + db sync
     */
    public void unload(UUID uuid) {
        save(uuid);
        cache.remove(uuid);
    }

    /**
     * Set a value (autoload if needed)
     */
    public void set(UUID uuid, String key, String value) {
        getOrLoad(uuid).put(key, value);
    }

    /**
     * Get value (null if absent)
     */
    public String get(UUID uuid, String key) {
        return getOrLoad(uuid).get(key);
    }

    /**
     * Get value with default
     */
    public String getOrDefault(UUID uuid, String key, String def) {
        return getOrLoad(uuid).getOrDefault(key, def);
    }

    /**
     * Check if loaded
     */
    public boolean isLoaded(UUID uuid) {
        return cache.containsKey(uuid);
    }

    /**
     * Internal: get or load automatically
     */
    private Map<String, String> getOrLoad(UUID uuid) {
        return cache.computeIfAbsent(uuid, u -> new HashMap<>(repository.load(u)));
    }
}