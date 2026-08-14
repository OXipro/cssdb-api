package com.oxipro.cssdb.config;

import com.oxipro.cmu.configlang.api.config.IConfigFile;

import java.util.HashMap;
import java.util.Map;

public class DBConfig {

    /** Default base path used to read values from an {@link IConfigFile}. */
    public static final String DEFAULT_CONFIG_BASE_PATH = "database";

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String customJdbcUrl;
    private final Map<String, String> properties;
    private final DBProvider provider;

    public DBConfig(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, null, new HashMap<>(), DBProvider.MYSQL);
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl) {
        this(host, port, database, username, password, customJdbcUrl, new HashMap<>(), DBProvider.MYSQL);
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl, Map<String, String> properties) {
        this(host, port, database, username, password, customJdbcUrl, properties, DBProvider.MYSQL);
    }

    public DBConfig(String host, int port, String database, String username, String password, DBProvider provider) {
        this(host, port, database, username, password, null, new HashMap<>(), provider);
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl, DBProvider provider) {
        this(host, port, database, username, password, customJdbcUrl, new HashMap<>(), provider);
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl, Map<String, String> properties, DBProvider provider) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.customJdbcUrl = customJdbcUrl;
        this.properties = properties;
        this.provider = provider != null ? provider : DBProvider.MYSQL;
    }

    /**
     * Builds a {@link DBConfig} from a configlang {@link IConfigFile}, reading values
     * under the default "database" base path (database.host, database.port, ...).
     * <p>
     * The provider is resolved as follows:
     * <ol>
     *     <li>if "database.provider" is set, it is used (autodetected from its value)</li>
     *     <li>otherwise, if "database.jdbcUrl" is set, the provider is autodetected from it</li>
     *     <li>otherwise, it defaults to {@link DBProvider#MYSQL}</li>
     * </ol>
     */
    public static DBConfig fromConfigFile(IConfigFile config) {
        return fromConfigFile(config, DEFAULT_CONFIG_BASE_PATH);
    }

    /**
     * Same as {@link #fromConfigFile(IConfigFile)}, but reads values under a custom base path.
     *
     * @param config   the loaded configlang config file
     * @param basePath e.g. "database" for keys like "database.host"
     */
    public static DBConfig fromConfigFile(IConfigFile config, String basePath) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }

        String prefix = (basePath == null || basePath.isBlank()) ? "" : basePath + ".";

        String host = blankToNull(config.getString(prefix + "host"));
        int port = config.getInt(prefix + "port");
        String database = blankToNull(config.getString(prefix + "database"));
        String username = blankToNull(config.getString(prefix + "username"));
        String password = blankToNull(config.getString(prefix + "password"));
        String customJdbcUrl = blankToNull(config.getString(prefix + "jdbcUrl"));
        String providerRaw = blankToNull(config.getString(prefix + "provider"));

        DBProvider provider;
        if (providerRaw != null) {
            provider = DBProvider.detect(providerRaw);
        } else if (customJdbcUrl != null) {
            provider = DBProvider.detectOrDefault(customJdbcUrl, DBProvider.MYSQL);
        } else {
            provider = DBProvider.MYSQL;
        }

        return new DBConfig(host, port, database, username, password, customJdbcUrl, new HashMap<>(), provider);
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    public String getJdbcUrl() {
        if (customJdbcUrl != null) {
            return applyPlaceholders(customJdbcUrl);
        }

        String base = defaultJdbcPrefix() + host + ":" + port + "/" + database;

        Map<String, String> effectiveProperties = properties.isEmpty() ? defaultProperties() : properties;

        if (effectiveProperties.isEmpty()) {
            return base;
        }

        StringBuilder sb = new StringBuilder(base);
        sb.append("?");

        for (Map.Entry<String, String> entry : effectiveProperties.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        sb.setLength(sb.length() - 1);

        return sb.toString();
    }

    private String defaultJdbcPrefix() {
        switch (provider) {
            case POSTGRESQL:
                return "jdbc:postgresql://";
            case MYSQL:
            default:
                return "jdbc:mysql://";
        }
    }

    private Map<String, String> defaultProperties() {
        if (provider == DBProvider.MYSQL) {
            Map<String, String> defaults = new HashMap<>();
            defaults.put("useSSL", "false");
            defaults.put("autoReconnect", "true");
            return defaults;
        }

        return new HashMap<>();
    }

    private String applyPlaceholders(String url) {
        return url
                .replace("{host}", host)
                .replace("{port}", String.valueOf(port))
                .replace("{database}", database);
    }

    public DBConfig addProperty(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public DBProvider getProvider() {
        return provider;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
