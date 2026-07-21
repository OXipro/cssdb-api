package com.oxipro.cssdb.database;

import com.oxipro.cssdb.config.DBConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSQLDatabase implements Database {

    private final DBConfig config;
    private HikariDataSource dataSource;

    public PostgreSQLDatabase(DBConfig config) {
        this.config = config;
    }

    @Override
    public void connect() {
        HikariConfig hikari = new HikariConfig();

        hikari.setJdbcUrl(config.getJdbcUrl());
        hikari.setUsername(config.getUsername());
        hikari.setPassword(config.getPassword());

        hikari.setMaximumPoolSize(10);
        hikari.setMinimumIdle(2);
        hikari.setPoolName("CSSDB-PostgreSQL");

        hikari.setConnectionTimeout(30000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000);
        hikari.setKeepaliveTime(0);

        hikari.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(hikari);
    }

    @Override
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}