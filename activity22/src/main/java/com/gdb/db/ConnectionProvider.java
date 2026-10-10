package com.gdb.db;

import java.sql.Connection;
import java.sql.SQLException;

/** How the rest of the app gets a database connection, without knowing which database. */
public interface ConnectionProvider {

    Connection getConnection() throws SQLException;
}
