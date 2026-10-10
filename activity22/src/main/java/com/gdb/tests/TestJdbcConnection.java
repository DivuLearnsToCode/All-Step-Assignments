package com.gdb.tests;

import com.gdb.db.ConnectionProvider;
import com.gdb.db.JdbcConnectionProvider;
import com.gdb.db.SchemaInitializer;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestJdbcConnection {

    private static final String DB_FILE = "test_gdb.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;

    public static void main(String[] args) throws Exception {
        System.out.println("============================================================");
        System.out.println("  ACTIVITY 22 - JDBC CONNECTION & SCHEMA INITIALIZATION");
        System.out.println("============================================================");

        new File(DB_FILE).delete(); // start from an empty database
        ConnectionProvider provider = new JdbcConnectionProvider(DB_URL);

        try {
            testConnection(provider);
            testSchemaExecution(provider);
            testSchemaVerification(provider);
        } finally {
            new File(DB_FILE).delete(); // leave nothing behind
        }

        System.out.println("============================================================");
        System.out.println("  ALL ACTIVITY 22 JDBC CONNECTION TESTS PASSED!");
        System.out.println("============================================================");
    }

    private static void testConnection(ConnectionProvider provider) throws Exception {
        System.out.println("[TEST 1] Database Connection Establishment:");
        try (Connection connection = provider.getConnection()) {
            check(connection != null && !connection.isClosed(), "connection should be open");
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("  Connected to: " + meta.getURL());
            System.out.println("  Driver Name: " + meta.getDriverName());
            check(meta.getURL().equals(DB_URL), "connected URL should match the configured one");
        }
        System.out.println("  -> PASSED");
        System.out.println();
    }

    private static void testSchemaExecution(ConnectionProvider provider) {
        System.out.println("[TEST 2] Schema DDL Execution:");
        System.out.println("  Creating tables: accounts, transactions...");
        SchemaInitializer.initialize(provider);
        SchemaInitializer.initialize(provider); // IF NOT EXISTS: running twice must not fail
        System.out.println("  Tables initialized successfully.");
        System.out.println("  -> PASSED");
        System.out.println();
    }

    private static void testSchemaVerification(ConnectionProvider provider) throws Exception {
        System.out.println("[TEST 3] Schema Verification:");
        try (Connection connection = provider.getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            boolean accounts = tableExists(meta, "accounts");
            boolean transactions = tableExists(meta, "transactions");
            System.out.println("  Table 'accounts' exists: " + accounts);
            System.out.println("  Table 'transactions' exists: " + transactions);
            check(accounts && transactions, "both tables should exist");

            List<String> accountColumns = columns(meta, "accounts");
            check(accountColumns.containsAll(Arrays.asList("account_number", "account_holder_name", "age",
                    "balance", "account_type", "status", "pin", "opening_date")),
                    "accounts is missing columns: " + accountColumns);
            List<String> txnColumns = columns(meta, "transactions");
            check(txnColumns.containsAll(Arrays.asList("id", "timestamp", "account_number", "type", "amount",
                    "balance_after", "status", "remarks", "from_account", "to_account")),
                    "transactions is missing columns: " + txnColumns);
        }
        System.out.println("  -> PASSED");
        System.out.println();
    }

    private static boolean tableExists(DatabaseMetaData meta, String table) throws Exception {
        try (ResultSet rs = meta.getTables(null, null, table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static List<String> columns(DatabaseMetaData meta, String table) throws Exception {
        List<String> names = new ArrayList<>();
        try (ResultSet rs = meta.getColumns(null, null, table, null)) {
            while (rs.next()) {
                names.add(rs.getString("COLUMN_NAME"));
            }
        }
        return names;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
