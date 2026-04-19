package com.oxipro.cssdb.support;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBSupport {

    void connect();

    Connection getConnection() throws SQLException;

    void shutdown();
}