package com.gdb.db;

import com.gdb.exceptions.DataAccessException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Creates the accounts and transactions tables from config/schema.sql. Safe to run repeatedly. */
public class SchemaInitializer {

    private static final String CLASSPATH_RESOURCE = "config/schema.sql";
    private static final String FILESYSTEM_PATH = "src/main/resources/config/schema.sql";

    // 📝 STEP 2
    public static void initialize(ConnectionProvider provider) {
        List<String> statements = loadStatements();

        try (Connection connection = provider.getConnection();
             Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                statement.execute(sql);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Schema initialization failed: " + e.getMessage(), e);
        }
    }

    /** Reads the script and splits it into individual statements. */
    private static List<String> loadStatements() {
        String script = readScript();
        // drop "--" comment lines, then split on ";"
        String withoutComments = script.lines()
                .filter(line -> !line.trim().startsWith("--"))
                .collect(Collectors.joining("\n"));

        List<String> statements = new ArrayList<>();
        for (String part : withoutComments.split(";")) {
            if (!part.trim().isEmpty()) {
                statements.add(part.trim());
            }
        }
        if (statements.isEmpty()) {
            throw new DataAccessException("schema.sql contains no statements");
        }
        return statements;
    }

    private static String readScript() {
        try (InputStream in = SchemaInitializer.class.getClassLoader().getResourceAsStream(CLASSPATH_RESOURCE)) {
            if (in != null) {
                return read(in);
            }
        } catch (IOException e) {
            // fall through to the file system
        }
        try (InputStream in = new FileInputStream(FILESYSTEM_PATH)) {
            return read(in);
        } catch (IOException e) {
            throw new DataAccessException(
                    "Could not find schema.sql on the classpath or at " + FILESYSTEM_PATH, e);
        }
    }

    private static String read(InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
