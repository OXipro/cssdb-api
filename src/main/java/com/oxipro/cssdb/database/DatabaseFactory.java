package com.oxipro.cssdb.database;

import com.oxipro.cssdb.config.DBConfig;
import com.oxipro.cssdb.config.DBProvider;

/**
 * Creates the right {@link Database} implementation from a {@link DBConfig},
 * based on its (explicit or autodetected) {@link DBProvider}.
 */
public final class DatabaseFactory {

    private DatabaseFactory() {
    }

    public static Database create(DBConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }

        DBProvider provider = config.getProvider();

        switch (provider) {
            case POSTGRESQL:
                return new PostgreSQLDatabase(config);
            case MYSQL:
                return new MySQLDatabase(config);
            default:
                throw new IllegalStateException("Unsupported DB provider: " + provider);
        }
    }
}
