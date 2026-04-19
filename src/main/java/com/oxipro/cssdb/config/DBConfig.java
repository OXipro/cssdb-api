package com.oxipro.cssdb.config;

import java.util.HashMap;
import java.util.Map;

public class DBConfig {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String customJdbcUrl;
    private final Map<String, String> properties;

    public DBConfig(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, null, new HashMap<>());
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl) {
        this(host, port, database, username, password, customJdbcUrl, new HashMap<>());
    }

    public DBConfig(String host, int port, String database, String username, String password, String customJdbcUrl, Map<String, String> properties) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.customJdbcUrl = customJdbcUrl;
        this.properties = properties;
    }

    public String getJdbcUrl() {
        if (customJdbcUrl != null) {
            return applyPlaceholders(customJdbcUrl);
        }

        String base = "jdbc:mysql://" + host + ":" + port + "/" + database;

        if (properties.isEmpty()) {
            return base + "?useSSL=false&autoReconnect=true";
        }

        StringBuilder sb = new StringBuilder(base);
        sb.append("?");

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        sb.setLength(sb.length() - 1);

        return sb.toString();
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}