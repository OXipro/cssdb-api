package com.oxipro.cssdb.support.mysql;

import com.oxipro.cssdb.config.DBConfig;
import com.oxipro.cssdb.support.IDBSupport;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLSupport implements IDBSupport {

    private final DBConfig config;
    private HikariDataSource dataSource;

    public MySQLSupport(DBConfig config) {
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
        hikari.setIdleTimeout(30000);

        dataSource = new HikariDataSource(hikari);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}