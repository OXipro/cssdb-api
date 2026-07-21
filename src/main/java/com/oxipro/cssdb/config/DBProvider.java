package com.oxipro.cssdb.config;

/**
 * Supported database providers for CSSDB.
 * <p>
 * A provider can either be set explicitly, or autodetected from any string
 * that hints at it (a JDBC URL, a config value like "postgres"/"mariadb", ...).
 */
public enum DBProvider {

    POSTGRESQL,
    MYSQL;

    /**
     * Tries to detect the provider from an arbitrary string: a JDBC URL
     * ("jdbc:postgresql://...", "jdbc:mysql://...", "jdbc:mariadb://..."),
     * or a plain identifier ("postgres", "postgresql", "mysql", "mariadb", ...).
     *
     * @param value the string to inspect
     * @return the detected provider
     * @throws IllegalArgumentException if value is null/blank or no known provider matches
     */
    public static DBProvider detect(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Cannot detect DB provider from an empty value");
        }

        String normalized = value.toLowerCase();

        if (normalized.contains("postgres")) {
            return POSTGRESQL;
        }

        if (normalized.contains("mysql") || normalized.contains("mariadb")) {
            return MYSQL;
        }

        throw new IllegalArgumentException("Unable to detect a supported DB provider from: " + value);
    }

    /**
     * Same as {@link #detect(String)}, but returns {@code fallback} instead of throwing
     * when the provider cannot be determined (value is null/blank/unrecognized).
     */
    public static DBProvider detectOrDefault(String value, DBProvider fallback) {
        try {
            return detect(value);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }
}
