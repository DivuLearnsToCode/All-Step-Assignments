package com.gdb.db;

import com.gdb.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Opens SQLite connections. Each getConnection() call returns a new connection to close. */
public class JdbcConnectionProvider implements ConnectionProvider {

    private final String jdbcUrl;

    // 📝 STEP 1
    public JdbcConnectionProvider(String jdbcUrl) {
        if (jdbcUrl == null || jdbcUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("JDBC URL cannot be empty");
        }
        this.jdbcUrl = jdbcUrl;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DataAccessException(
                    "SQLite JDBC driver not found. Put sqlite-jdbc-*.jar on the classpath (lib/*).", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
